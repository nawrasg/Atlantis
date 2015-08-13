<?php
header ( "Access-Control-Allow-Origin: *" );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';
require_once __DIR__ . '/classes/VigilanceMeteo.php';
require_once __DIR__ . '/classes/Weather.php';

$page_level = 1;
$settings = new Settings ();
$vigilance = new VigilanceMeteo ();
$weather = new Weather ();

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			$alarm = $settings->getSettings ( 'Alarm', 'status' );
			http_response_code(202);
			echo json_encode ( array (
					'alarm' => $alarm,
					'weather' => getWeather (),
					'rooms' => getRooms() 
			) );
			break;
		case 'PUT' :
			if (isset ( $_REQUEST ['alarm'] )) {
				if ($_REQUEST ['alarm'] == 'true') {
					$val = TRUE;
				} else {
					$val = FALSE;
				}
				$settings->setSettings ( 'Alarm', 'status', $val );
			}
			break;
	}
} else {
	http_response_code(403);
}

function getWeather() {
	$settings = new Settings ();
	$vigilance = new VigilanceMeteo ();
	$weather = new Weather ();
	return $weather->getCachedWeather ();
}

function getRooms() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_room' );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$arr [] = array (
				'id' => $data ['id'],
				'room' => $data ['room']
		);
	}
	$req->closeCursor ();
	return $arr;
}