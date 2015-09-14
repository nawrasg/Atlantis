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
			http_response_code ( 202 );
			echo json_encode ( array (
					'alarm' => $alarm,
					'weather' => getWeather (),
					'rooms' => getRooms () 
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
} else if ($argc > 1) {
	switch ($argv [1]) {
		case 'weather' :
			setWeather ();
			break;
	}
} else {
	http_response_code ( 403 );
}
function setWeather() {
	$settings = new Settings ();
	$weather = new Weather ();
	$city = $settings->getSettings ( 'Atlantis', 'city' );
	
	$temp = $weather->getTemperature ();
	$temp2 = $weather->getTemperature ( 2 );
	$code = $weather->getWeatherCode ();
	$code2 = $weather->getWeatherCode ( 2 );
	$description = $weather->getDescription ();
	$description2 = $weather->getDescription ( 2 );
	
	$data = $weather->getCachedWeather ();
	
	$data [0]->temperature = $temp;
	$data [0]->code = $code;
	$data [0]->description = $description;
	
	$data [1]->temperature = $temp2;
	$data [1]->code = $code2;
	$data [1]->description = $description2;
	
	$weather->setCachedWeather ( $data );
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