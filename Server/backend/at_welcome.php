<?php
header ( 'Access-Control-Allow-Origin: *' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Player.php';
require_once __DIR__ . '/classes/Settings.php';

$page_level = 0;

if (isset ( $_GET ['api'] ) && checkAPI ( $_GET ['api'], $page_level )) {
	$welcome_interval = (new Settings())->getSettings('Audio', 'welcome');
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'SELECT * FROM at_welcome WHERE mac = :mac' );
	$req->execute ( array (
			'mac' => $_GET ['api'] 
	) );
	$data = $req->fetch ();
	if ($data) {
		$diff = strtotime ( now ) - strtotime ( $data ['jour'] . " " . $data ['heure'] );
		$diff /= 3600;
		if ($diff > $welcome_interval) {
			welcome ( $_GET ['api'] );
		}
	}
	$req->closeCursor ();
} else {
	echo 401;
}
function welcome($api) {
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'UPDATE at_welcome SET jour = now(), heure = now() WHERE mac = :mac' );
	$req->execute ( array (
			'mac' => $api 
	) );
	$req->closeCursor ();
	$player = new Player ();
	$req = $bdd->prepare ( 'SELECT at_music.file FROM at_music JOIN at_welcome ON at_music.id = at_welcome.music WHERE at_welcome.mac = mac' );
	$req->execute ( array (
			'mac' => $mac 
	) );
	$data = $req->fetch ();
	$player->play ( $data ['file'] );
	$req->closeCursor ();
}