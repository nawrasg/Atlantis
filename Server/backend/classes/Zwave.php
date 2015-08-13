<?php
require_once __DIR__ . '/connexion.php';

class Zwave {
	var $url, $port;

	public function __construct() {
		$settings = new Settings ();
		$this->url = $settings->getSettings ( "Zwave", "IP" );
		$this->port = $settings->getSettings ( "Zwave", "Port" );
	}

	private function loadRawData($id = NULL) {
		// For this to work, file_get_contents requires that allow_url_fopen is enabled
		if ($id == NULL) {
			$link = 'http://' . $this->url . ':' . $this->port . '/ZAutomation/api/v1/devices';
		} else {
			$link = 'http://' . $this->url . ':' . $this->port . '/ZAutomation/api/v1/devices/' . $id;
		}
		$json = file_get_contents ( $link );
		$arr = json_decode ( $json );
		if ($arr->{'code'} == 200) {
			return $arr;
		} else {
			return $arr->{'code'};
		}
	}

	public function GetTimestamp($id) {
		$device = $this->loadRawData ( $id );
		$timestamp = $device->data->updateTime;
		return $timestamp;
	}

	public function command($id, $cmd) {
		$link = 'http://' . $this->url . ':' . $this->port . '/ZAutomation/api/v1/devices/' . $id . '/command/' . $cmd;
		$json = file_get_contents ( $link );
		$arr = json_decode ( $json );
		return $arr->{'code'};
	}

	public function load() {
		$bdd = getBDD ();
		$req = $bdd->query ( 'SELECT * FROM at_sensors WHERE `ignore` = 0 AND `protocol` = "zwave" ORDER BY device' );
		$arr = array ();
		while ( $data = $req->fetch () ) {
			$id = $data ['id'];
			$sensor = $data ['sensor'];
			$type = $data ['type'];
			$device = $this->loadRawData ( $sensor );
			$val = $device->{'data'}->{'metrics'}->{'level'};
			$update = $device->{'data'}->{'updateTime'};
			$arr [] = array (
					'id' => $id,
					'sensor' => $sensor,
					'device' => $data ['device'],
					'room' => $data ['room'],
					'type' => $type,
					'unit' => $data ['unit'],
					'date' => $data ['date'],
					'time' => $data ['time'],
					'value' => $val,
					'update' => $update 
			);
		}
		$req->closeCursor ();
		return json_encode ( $arr );
	}

	public function loadDevices() {
		$bdd = getBDD ();
		$req = $bdd->query ( 'SELECT *, at_sensors.id AS id FROM at_sensors INNER JOIN at_sensors_devices ON at_sensors.device = at_sensors_devices.device WHERE `ignore` = 0 AND `protocol` = "zwave" ORDER BY at_sensors.device' );
		$arr = array ();
		$lastDevice = "";
		while ( $data = $req->fetch () ) {
			if ($data ['device'] != $lastDevice) {
				$arr [] = array (
						'device' => $data ['device'],
						'type' => 'section',
						'alias' => $data ['alias'],
						'room' => $data ['room'] 
				);
				$lastDevice = $data ['device'];
			}
			$id = $data ['id'];
			$sensor = $data ['sensor'];
			$type = $data ['type'];
			$device = $this->loadRawData ( $sensor );
			$val = $device->{'data'}->{'metrics'}->{'level'};
			$update = $device->{'data'}->{'updateTime'};
			$arr [] = array (
					'id' => $id,
					'sensor' => $sensor,
					'device' => $data ['device'],
					'type' => $type,
					'unit' => $data ['unit'],
					'date' => $data ['date'],
					'time' => $data ['time'],
					'value' => $val,
					'update' => $update,
					'history' => $data ['history'],
					'ignore' => $data ['ignore'] 
			);
		}
		$req->closeCursor ();
		return $arr;
	}

	public function discover() {
		$arr = $this->loadRawData ();
		$devices = $arr->{'data'}->{'devices'};
		for($i = 0; $i < count ( $devices ); $i ++) {
			$sensor = $devices [$i]->{'id'};
			$device = $this->getRealDeviceName ( $sensor );
			if (isset ( $devices [$i]->{'metrics'}->{'probeTitle'} )) {
				$type = $devices [$i]->{'metrics'}->{'probeTitle'};
				$unit = $devices [$i]->{'metrics'}->{'scaleTitle'};
			} else {
				$type = $devices [$i]->{'deviceType'};
				$unit = "";
			}
			$this->insertDevice ( $sensor, $device, $type, $unit );
		}
		return 200;
	}

	private function getRealDeviceName($device) {
		$arr = explode ( '-', $device );
		return $arr [0];
	}

	private function insertDevice($sensor, $device, $type, $unit) {
		$bdd = getBDD ();
		$req = $bdd->prepare ( 'INSERT INTO at_sensors VALUES("", :sensor, :device, "zwave", :type, :unit, 0, now(), now(), 0)' );
		$req->execute ( array (
				'sensor' => $sensor,
				'device' => $device,
				'type' => $type,
				'unit' => $unit 
		) );
		$req->closeCursor ();
		$req = $bdd->prepare ( 'INSERT INTO at_sensors_devices VALUES("", :device, NULL, NULL)' );
		$req->execute ( array (
				'device' => $device 
		) );
		$req->closeCursor ();
		return 200;
	}

	public function getValue($id) {
		$sensor = $this->loadRawData ( $id );
		$value = $sensor->{'data'}->{'metrics'}->{'level'};
		return $value;
	}

	public function delete() {
		$bdd = getBDD ();
		$req = $bdd->exec ( 'TRUNCATE TABLE at_sensors' );
		$req2 = $bdd->exec ( 'TRUNCATE TABLE at_sensors_devices' );
		$req->closeCursor ();
		$req2->closeCursor ();
		return 200;
	}

	public function inclusion($start) {
		$status = ($start ? 1 : 0);
		$link = 'http://' . $this->url . ':' . $this->port . '/ZWaveAPI/Run/controller.AddNodeToNetwork(' . $status . ')';
		$result = file_get_contents ( $link );
		return $result;
	}

	public function exclusion($start) {
		$status = ($start ? 1 : 0);
		$link = 'http://' . $this->url . ':' . $this->port . '/ZWaveAPI/Run/controller.RemoveNodeFromNetwork(' . $status . ')';
		$result = file_get_contents ( $link );
		return $result;
	}
}
