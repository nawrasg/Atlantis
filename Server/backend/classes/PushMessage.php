<?php
require_once __DIR__ . '/connexion.php';
require_once __DIR__ . '/GCMPushMessage.php';
require_once __DIR__ . '/Settings.php';
class PushMessage {
	private $apiKey = "";
	function __construct() {
		$settings = new Settings ();
		$this->apiKey = $settings->getSettings ( "Notification", "key" );
	}
	public function sendMessage($title, $message, $level = 0) {
		$devices = array ();
		$gcpm = new GCMPushMessage ( $this->apiKey );
		$bdd = getBDD ();
		$req = $bdd->prepare ( 'SELECT at_gcm.gcm FROM at_gcm JOIN at_devices ON at_gcm.mac = at_devices.mac JOIN at_users ON at_devices.username = at_users.id WHERE at_users.type <= :type' );
		$req->execute ( array (
				'type' => $level 
		) );
		while ( $data = $req->fetch () ) {
			array_push ( $devices, $data ['gcm'] );
		}
		$req->closeCursor ();
		$gcpm->setDevices ( $devices );
		$response = $gcpm->send ( $message, array (
				'title' => $title 
		) );
		return $response;
	}
	public function sendMessageAll($title, $message) {
		$devices = array ();
		$gcpm = new GCMPushMessage ( $this->apiKey );
		$bdd = getBDD ();
		$req = $bdd->query ( 'SELECT gcm FROM at_gcm' );
		while ( $data = $req->fetch () ) {
			array_push ( $devices, $data ['gcm'] );
		}
		$req->closeCursor ();
		$gcpm->setDevices ( $devices );
		$response = $gcpm->send ( $message, array (
				'title' => $title 
		) );
		return $response;
	}
	public function sendMessageDevice($title, $message, $device) {
		$gcpm = new GCMPushMessage ( $this->apiKey );
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT at_gcm.gcm FROM at_gcm JOIN at_devices ON at_gcm.mac = at_devices.mac JOIN at_users ON at_devices.username = at_users.id WHERE at_gcm.mac = '$device' LIMIT 1" );
		$data = $req->fetch ();
		if (! $data) {
			return;
		}
		$gcpm->setDevices ( $data ['gcm'] );
		$response = $gcpm->send ( $message, array (
				'title' => $title 
		) );
		return $response;
	}
}
