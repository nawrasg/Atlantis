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
	if (isset ( $arr ['lat'], $arr ['long'])) {
		$mac = $arr ['api'];
		$lat = $arr ['lat'];
		$long = $arr ['long'];
		$speed = 0;
		$bearing = 0;
		$bdd = getBDD ();
		$req = $bdd->exec ( "INSERT INTO at_geo VALUES('$mac', '$lat', '$long', '$speed', '$bearing', NOW(), NOW(), NOW())" );
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
	$req = $bdd->query ( 'SELECT arr.*, IFNULL(arr2.nom, at_users.nom) nom FROM (SELECT t1.* FROM at_geo t1 LEFT JOIN at_geo t2 ON (t1.mac = t2.mac AND t1.timestamp < t2.timestamp) WHERE t2.timestamp IS NULL AND t1.date = CURDATE()) AS arr LEFT JOIN (SELECT at_devices.mac, at_users.nom FROM at_devices INNER JOIN at_users ON at_devices.username = at_users.id) AS arr2 ON arr.mac = arr2.mac LEFT JOIN at_users ON arr.mac = at_users.cle' );
	$result = $req->fetchAll(PDO::FETCH_ASSOC);
	$req->closeCursor();
	http_response_code ( 202 );
	$output = array (
			'atlantis' => $atlantis,
			'positions' => $result 
	);
	return $output;
}