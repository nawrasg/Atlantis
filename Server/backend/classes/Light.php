<?php
require_once __DIR__ . '/connexion.php';
require_once __DIR__ . '/Hue.php';
class Light {
	private $lights;
	public static function byRoom($room) {
		$light = new Light ();
		$light->lights = array ();
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT * FROM at_lights WHERE room = $room" );
		while ( $data = $req->fetch () ) {
			$light->lights [] = array (
					'id' => $data ['id'],
					'name' => $data ['name'],
					'protocol' => $data ['protocol'],
					'ip' => $data ['ip'],
					'room' => $data ['room'],
					'uid' => $data ['uid'] 
			);
		}
		return $light;
	}
	public static function byID($id) {
		$light = new Light ();
		$light->lights = array ();
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT * FROM at_lights WHERE id = $id" );
		while ( $data = $req->fetch () ) {
			$light->lights [] = array (
					'id' => $data ['id'],
					'name' => $data ['name'],
					'protocol' => $data ['protocol'],
					'ip' => $data ['ip'],
					'room' => $data ['room'],
					'uid' => $data ['uid'] 
			);
		}
		return $light;
	}
	public static function allLights() {
		$light = new Light ();
		$light->lights = array ();
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT * FROM at_lights" );
		while ( $data = $req->fetch () ) {
			$light->lights [] = array (
					'id' => $data ['id'],
					'name' => $data ['name'],
					'protocol' => $data ['protocol'],
					'ip' => $data ['ip'],
					'room' => $data ['room'],
					'uid' => $data ['uid'] 
			);
		}
		return $light;
	}
	public function color($color) {
		$hue = new Hue ();
		foreach ( $this->lights as $light ) {
			$protocol = $light ['protocol'];
			switch ($protocol) {
				case 'hue' :
					$uid = $light ['uid'];
					$hue->setColor ( $uid, $color );
					break;
			}
		}
	}
	public function on($value) {
		$hue = new Hue ();
		foreach ( $this->lights as $light ) {
			$protocol = $light ['protocol'];
			switch ($protocol) {
				case 'hue' :
					$uid = $light ['uid'];
					$hue->on ( $uid, $value );
					break;
			}
		}
	}
	public function brightness($value) {
		$hue = new Hue ();
		foreach ( $this->lights as $light ) {
			$protocol = $light ['protocol'];
			switch ($protocol) {
				case 'hue' :
					$uid = $light ['uid'];
					$hue->setBrightness ( $uid, $value );
					break;
			}
		}
	}
	public function isOn() {
		$size = count ( $this->lights );
		$hue = new Hue ();
		if ($size != 1) {
			$status = array ();
			for($i = 0; $i < $size; $i ++) {
				$light = $this->lights [0];
				$protocol = $light ['protocol'];
				switch ($protocol) {
					case 'hue' :
						$uid = $light ['uid'];
						array_push ( $status, $hue->isOn ( $uid ) );
						break;
				}
			}
			return $status;
		}
		$light = $this->lights [0];
		$protocol = $light ['protocol'];
		switch ($protocol) {
			case 'hue' :
				$uid = $light ['uid'];
				return $hue->isOn ( $uid );
		}
	}
	public function status($pattern){
		$size_lights = count($this->lights);
		$size_pattern = count($pattern);
		$hue = new Hue();
		if($size_lights != $size_pattern){
			return false;
		}
		for($i = 0; $i < $size_lights; $i++){
			$light = $this->lights[$i]; 
			$protocol = $light['protocol'];
			switch($protocol){
				case 'hue':
					$uid = $light['uid'];
					$hue->on($uid, $status[$i]);
					break;
			}
		}
	}
}