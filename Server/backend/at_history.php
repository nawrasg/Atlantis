<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Zwave.php';

$page_level = 1;

if (isset ( $_GET ['api'] ) && checkAPI ( $_GET ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get ( $_REQUEST ) );
			break;
	}
}
function get($arr) {
	$bdd = getBDD ();
	if (isset ( $arr ['plant'] )) {
		$sensor = $arr ['plant'];
		$req = $bdd->query ( "SELECT *, AVG(moisture), AVG(air_temperature), AVG(soil_temperature), AVG(soil_conductivity), AVG(light) FROM at_plants_history  WHERE sensor = '$sensor' AND date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY date" );
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
		http_response_code ( 202 );
		return array (
				'plants' => $output_plants 
		);
	}
}