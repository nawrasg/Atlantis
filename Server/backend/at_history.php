<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Zwave.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get ( $_REQUEST ) );
			break;
	}
}
function get($arr) {
	$bdd = getBDD ();
	if (isset ( $arr ['to'] )) {
		$to = $arr ['to'];
	} else {
		$to = date ( 'Y-m-d' );
	}
	if (isset ( $arr ['from'] )) {
		$from = $arr ['from'];
	} else {
		$date = new DateTime ();
		$date->sub ( new DateInterval ( 'P3D' ) );
		$from = $date->format ( 'Y-m-d' );
	}
	if (isset ( $arr ['plant'] )) {
		$sensor = $arr ['plant'];
		$req = $bdd->query ( "SELECT *, AVG(moisture), AVG(air_temperature), AVG(soil_temperature), AVG(soil_conductivity), AVG(light) FROM at_plants_history  WHERE sensor = '$sensor' AND date BETWEEN $from AND $to GROUP BY date" );
		$output = array ();
		while ( $data = $req->fetch () ) {
			$output [] = array (
					'light_m' => $data ['AVG(light)'],
					'moisture_m' => $data ['AVG(moisture)'],
					'air_temperature_m' => $data ['AVG(air_temperature)'],
					'soil_temperature_m' => $data ['AVG(soil_temperature)'],
					'soil_conductivity_m' => $data ['AVG(soil_conductivity)'],
					'date' => $data ['date'] 
			);
		}
		http_response_code ( 202 );
		return $output;
	} else if (isset ( $arr ['sensor'] )) {
		$sensor = $arr ['sensor'];
		$req = $bdd->query ( "SELECT * FROM at_sensors_values WHERE sensor = '$sensor' AND date BETWEEN $from AND $to ORDER BY date, time" );
		$output = array ();
		while ( $data = $req->fetch () ) {
			$output [] = array (
					'value' => $data ['value'],
					'date' => $data ['date'],
					'time' => $data ['time'] 
			);
		}
		http_response_code ( 202 );
		return $output;
	} else {
		$req = $bdd->query ( 'SELECT * FROM at_plants' );
		$output_plants = array ();
		while ( $data = $req->fetch () ) {
			$output_plants [] = array (
					'id' => $data ['id'],
					'sensor' => $data ['sensor'],
					'title' => $data ['title'],
					'color' => $data ['color'],
					'room' => $data ['room'] 
			);
		}
		$req = $bdd->query ( 'SELECT at_sensors.*, at_sensors_devices.alias FROM at_sensors_values JOIN at_sensors ON at_sensors_values.sensor = at_sensors.id JOIN at_sensors_devices ON at_sensors.device = at_sensors_devices.device GROUP BY at_sensors.sensor' );
		$output_sensors = array ();
		while ( $data = $req->fetch () ) {
			$output_sensors [] = array (
					'id' => $data ['id'],
					'sensor' => $data ['sensor'],
					'type' => $data ['type'],
					'unit' => $data ['unit'],
					'alias' => $data ['alias'] 
			);
		}
		http_response_code ( 202 );
		return array (
				'plants' => $output_plants,
				'sensors' => $output_sensors 
		);
	}
}