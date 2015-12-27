<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/PushMessage.php';
require_once __DIR__ . '/classes/Settings.php';

$page_level = 2;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get () );
			break;
		case 'POST' :
			broadcast ( $_REQUEST );
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
	}
}
function broadcast($arr) {
	$push = new PushMessage ();
	if (isset ( $arr ['secret'] )) {
		$push->sendMessageAll ( 'at_commands', 'geoi' );
	} else {
		$push->sendMessageAll ( 'at_commands', 'geo' );
	}
	http_response_code ( 202 );
}
function update($arr) {
	if (isset ( $arr ['lat'], $arr ['long'] )) {
		$mac = $arr ['api'];
		$lat = $arr ['lat'];
		$long = $arr ['long'];
		$bdd = getBDD ();
		$req = $bdd->exec ( "INSERT INTO at_geo VALUES('$mac', '$lat', '$long', NOW(), NOW(), NOW())" );
		if ($req == 1) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function get() {
	$settings = new Settings ();
	$atlantis = array (
			'lat' => doubleval ( $settings->getSettings ( 'Atlantis', 'lat' ) ),
			'long' => doubleval ( $settings->getSettings ( 'Atlantis', 'long' ) ) 
	);
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT t1.* FROM at_geo t1 LEFT JOIN at_geo t2 ON (t1.mac = t2.mac AND t1.timestamp < t2.timestamp) WHERE t2.timestamp IS NULL AND t1.date = CURDATE()' );
	$result = array ();
	while ( $data = $req->fetch () ) {
		$result [] = array (
				'mac' => $data ['mac'],
				'lat' => $data ['lat'],
				'long' => $data ['long'],
				'date' => $data ['date'],
				'time' => $data ['time'],
				'timestamp' => $data ['timestamp'] 
		);
	}
	http_response_code ( 202 );
	$output = array (
			'atlantis' => $atlantis,
			'positions' => $result 
	);
	return $output;
}