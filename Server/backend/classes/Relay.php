<?php
require_once __DIR__ . '/connexion.php';
require_once __DIR__ . '/Zwave.php';
class Relay {
	private $switches;
	public static function byRoom($room) {
		$switch = new Relay ();
		$switch->switches = array ();
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT at_sensors.id, at_sensors.sensor, at_sensors.device, at_sensors.protocol, at_sensors_devices.alias, at_sensors_devices.room FROM at_sensors INNER JOIN at_sensors_devices ON at_sensors.device = at_sensors_devices.device WHERE at_sensors.type = 'switchBinary' AND at_sensors.ignore = 0 AND at_sensors_devices.room = $room" );
		while ( $data = $req->fetch () ) {
			$switch->switches [] = array (
					'id' => $data ['id'],
					'sensor' => $data ['sensor'],
					'device' => $data ['device'],
					'protocol' => $data ['protocol'],
					'alias' => $data ['alias'],
					'room' => $data ['room'] 
			);
		}
		return $switch;
	}
	public function on($value) {
		$zwave = new Zwave ();
		foreach ( $this->switches as $switch ) {
			switch ($switch ['protocol']) {
				case 'zwave' :
					$value == true ? $val = 'on' : $val = 'false';
					$zwave->command ( $sensor ['sensor'], $val );
					break;
			}
		}
	}
}