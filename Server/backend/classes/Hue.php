<?php
require_once __DIR__ . '/connexion.php';
require_once __DIR__ . '/Settings.php';

class Hue {
	var $user, $ip;

	public function __construct() {
		$settings = new Settings ();
		$this->ip = $settings->getSettings ( "Hue", "ip" );
		$this->user = $settings->getSettings ( "Hue", "user" );
	}

	public function loadRawData($id = NULL) {
		if ($id == NULL) {
			$link = 'http://' . $this->ip . '/api/' . $this->user . '/lights';
		} else {
			$link = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $id;
		}
		$json = file_get_contents ( $link );
		$arr = json_decode ( $json );
		return $arr;
	}

	public function discover($new = FALSE) {
		if ($new) {
		} else {
			$data = $this->loadRawData ();
			for($i = 1; $i < 51; $i ++) {
				$light = $data->$i;
				if ($light != NULL) {
					$name = $light->name;
					$uid = $light->uniqueid;
					$this->insertLight ( $name, $uid );
				}else{
					break;
				}
			}
		}
	}

	private function insertLight($name, $uid) {
		$bdd = getBDD ();
		$req = $bdd->prepare ( 'INSERT INTO at_lights VALUES("", :name, "hue", "light", NULL, NULL, :uid)' );
		$req->execute ( array (
				'name' => $name,
				'uid' => $uid 
		) );
		$req->closeCursor ();
	}

	public function loadLights() {
		$bdd = getBDD ();
		$req = $bdd->query ( 'SELECT * FROM at_lights WHERE `protocol` = "hue"' );
		$arr = array ();
		while ( $data = $req->fetch () ) {
			$details = $this->getLightDetails ( $data ['uid'] );
			$arr [] = array_merge ( array (
					'id' => $data ['id'],
					//'name' => $data ['name'],
					'protocol' => $data ['protocol'],
					'room' => $data ['room'],
					'uid' => $data ['uid'] 
			), $details );
		}
		$req->closeCursor ();
		return $arr;
	}
	
	private function sendHTTP($method, $url, $body){
		$ch = curl_init ( $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, TRUE );
		curl_setopt ( $ch, CURLOPT_CUSTOMREQUEST, $method );
		curl_setopt ( $ch, CURLOPT_POSTFIELDS, $body );
		$response = curl_exec ( $ch );
		if (! $response) {
			return 500;
		} else {
			return $response;
		}
	}

	public function on($uid, $status) {
		$lights = $this->loadRawData ();
		for($i = 1; $i < 4; $i ++) {
			$light = $lights->$i;
			if ($light != NULL) {
				$uniqueid = $light->uniqueid;
				if ($uniqueid == $uid) {
					$body = json_encode ( array (
							'on' => ($status == "true" ? TRUE : FALSE) 
					) );
					$url = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $i . '/state';
					return $this->sendHTTP("PUT", $url, $body);
				}
			}
		}
	}

	public function setBrightness($uid, $value) {
		$lights = $this->loadRawData ();
		for($i = 1; $i < 4; $i ++) {
			$light = $lights->$i;
			if ($light != NULL) {
				$uniqueid = $light->uniqueid;
				if ($uniqueid == $uid) {
					$body = json_encode ( array (
							'bri' => intval($value) 
					) );
					$url = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $i . '/state';
					return $this->sendHTTP("PUT", $url, $body);
				}
			}
		}
	}
		
	public function toggleLight($uid){
		$i = $this->getLightIndex($uid);
		$light = $this->loadRawData($i);
		$ct = $light->state->ct;
		if($ct != 155){
			$body = json_encode(array('ct' => 155));
		}else{
			$body = json_encode(array('ct' => 369));			
		}
		$url = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $i . '/state';
		$this->sendHTTP("PUT", $url, $body);
	}
	
	public function setColor($uid, $value){
		switch($value){
			case 'white':
				$body = json_encode(array('ct' => 155));
				break;
			case 'yellow':
				$body = json_encode(array('ct' => 369));
				break;
			case 'red':
				$body = json_encode(array('hue' => 0, 'sat' => 254));
				break;
			case 'blue':
				$body = json_encode(array('hue' => 46920, 'sat' => 254));
				break;
			case 'green':
				$body = json_encode(array('hue' => 25500, 'sat' => 254));
				break;
				
		}
		$i = $this->getLightIndex($uid);
		$url = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $i . '/state';
		$this->sendHTTP("PUT", $url, $body);
	}
	
	public function setName($uid, $name){
		$i = $this->getLightIndex($uid);
		$url = 'http://' . $this->ip . '/api/' . $this->user . '/lights/' . $i;
		$body = json_encode(array('name' => $name));
		$result = $this->sendHTTP("PUT", $url, $body);
	}
	
	private function getLightIndex($uid){
		$lights = $this->loadRawData ();
		for($i = 1; $i < 4; $i ++) {
			$light = $lights->$i;
			if ($light != NULL) {
				$uniqueid = $light->uniqueid;
				if ($uniqueid == $uid) {
					return  $i;
				}
			}
		}
	}
	
	public function toggle($uid){
		$i = $this->getLightIndex($uid);
		$light = $this->loadRawData($i);
		if($light->state->on){
			$this->on($uid, "false");
		}else{
			$this->on($uid, "true");
		}
	}

	private function getLightDetails($uid) {
		$lights = $this->loadRawData ();
		for($i = 1; $i < 4; $i ++) {
			$light = $lights->$i;
			if ($light != NULL) {
				$uniqueid = $light->uniqueid;
				if ($uniqueid == $uid) {
					$reachable = $light->state->reachable;
					$status = $light->state->on;
					$brightness = $light->state->bri;
					$ct = $light->state->ct;
					$name = $light->name;
					$arr = array (
							'reachable' => $reachable,
							'on' => $status,
							'brightness' => $brightness ,
							'ct' => $ct,
							'name' => $name
					);
					return $arr;
				}
			} else {
				return FALSE;
			}
		}
		return FALSE;
	}
	
	public function delete() {
		$bdd = getBDD ();
		$req = $bdd->exec ( 'TRUNCATE TABLE at_lights' );
		$req->closeCursor ();
		return 200;
	}
}