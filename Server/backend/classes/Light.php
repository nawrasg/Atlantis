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
}