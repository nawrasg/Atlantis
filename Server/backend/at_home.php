<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';
require_once __DIR__ . '/classes/Alarm.php';
require_once __DIR__ . '/classes/Mode.php';
require_once __DIR__ . '/classes/VigilanceMeteo.php';
require_once __DIR__ . '/classes/Weather.php';

$page_level = 1;
$settings = new Settings ();
$vigilance = new VigilanceMeteo ();

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			$alarm = $settings->getSettings ( 'Alarm', 'status' );
			http_response_code ( 202 );
			echo json_encode ( array (
					'alarm' => $alarm,
					'weather' => getWeather (),
					'rooms' => getRooms (),
					'mode' => (new Mode ())->getMode () 
			) );
			break;
		case 'PUT' :
			echo json_encode ( update ( $_REQUEST ) );
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
function update($arr) {
	if (isset ( $arr ['alarm'] )) {
		$alarm = new Alarm ();
		if ($arr ['alarm'] == 'true') {
			$val = true;
			$alarm->on ();
		} else {
			$val = false;
			$alarm->off ();
		}
		if ($alarm->isOn () == $val) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	}
	if (isset ( $arr ['mode'] )) {
		$mode_value = $arr ['mode'];
		$mode = new Mode ();
		switch ($mode_value) {
			case Mode::DAY :
			case Mode::NIGHT :
			case Mode::AWAY :
				$mode->setMode ( $mode_value );
				http_response_code ( 202 );
				return array (
						'mode' => $mode_value 
				);
		}
	}
}
function setWeather() {
	$weather = new Weather ();
	
	$temp = $weather->getTemperature ();
	$temp2 = $weather->getTemperature ( 2 );
	$code = $weather->getWeatherCode ();
	$code2 = $weather->getWeatherCode ( 2 );
	$icon = $weather->getWeatherIcon ();
	$icon2 = $weather->getWeatherIcon ( 2 );
	$description = $weather->getDescription ();
	$description2 = $weather->getDescription ( 2 );
	$id = $weather->getWeatherId ();
	$id2 = $weather->getWeatherId ( 2 );
	$date = date ( 'Y-m-d H:i:s' );
	
	$result = array ();
	$result [] = array (
			'code' => $code,
			'temperature' => $temp,
			'description' => $description,
			'icon' => $icon,
			'id' => $id,
			'update' => $date 
	);
	$result [] = array (
			'code' => $code2,
			'temperature' => $temp2,
			'description' => $description2,
			'icon' => $icon2,
			'id' => $id2,
			'update' => $date 
	);
	
	$weather->setCachedWeather ( $result );
}
function getWeather() {
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