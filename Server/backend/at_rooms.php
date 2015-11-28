<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get () );
			break;
		case 'POST' :
			echo json_encode ( add ( $_REQUEST ) );
			break;
		case 'PUT' :
			
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
}
function get() {
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
function add($arr) {
	if (isset ( $arr ['label'] )) {
		$label = ucfirst ( strtolower ( $arr ['label'] ) );
		$bdd = getBDD ();
		$request = $bdd->exec ( "INSERT INTO at_room (`room`) VALUES('$label')" );
		if ($request == 1) {
			$id = $bdd->lastInsertId ();
			$query = $bdd->query ( "SELECT * FROM at_room WHERE id = '$id'" );
			$data = $query->fetch ();
			$room = array (
					'id' => $data ['id'],
					'room' => $data ['room'] 
			);
			http_response_code ( 202 );
			return $room;
		} else {
			http_response_code ( 400 );
		}
	} else if (isset ( $_FILES )) {
		$path = $_FILES ['file'] ['tmp_name'];
		$name = $_FILES ['file'] ['name'];
		$ext = pathinfo ( $name, PATHINFO_EXTENSION );
		$result = move_uploaded_file ( $path, './home/plan.' . $ext );
		if ($result) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function delete($arr) {
	if (isset ( $arr ['id'] )) {
		$bdd = getBDD ();
		$id = $arr ['id'];
		$request = $bdd->exec ( "UPDATE at_lights, at_plants, at_sensors_devices SET at_lights.room = NULL, at_plants.room = NULL, at_sensors_devices.room = NULL WHERE at_lights.room = '$id' OR at_plants.room = '$id' OR at_sensors_devices.room = '$id'" );
		$requestF = $bdd->exec ( "DELETE FROM at_room WHERE id = '$id'" );
		if ($requestF == 1) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}