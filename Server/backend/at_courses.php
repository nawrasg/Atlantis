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
			update ( $_REQUEST );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
}
function get() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_courses ORDER BY name' );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$arr [] = array (
				'id' => $data ['id'],
				'name' => $data ['name'],
				'quantity' => $data ['quantity'] 
		);
	}
	$req->closeCursor ();
	http_response_code ( 202 );
	return $arr;
}
function add($arr) {
	if (isset ( $arr ['name'] )) {
		$bdd = getBDD ();
		$name = $arr ['name'];
		$quantity = (isset ( $arr ['quantity'] )) ? $arr ['quantity'] : 1;
		$request = $bdd->exec ( "INSERT INTO at_courses VALUES('', '$name', '$quantity')" );
		if ($request == 1) {
			http_response_code ( 202 );
			$id = $bdd->lastInsertId ();
			$queryRequest = $bdd->query ( "SELECT * FROM at_courses WHERE id = '$id'" );
			$data = $queryRequest->fetch ();
			$newItem = array (
					'id' => $data ['id'],
					'name' => $data ['name'],
					'quantity' => $data ['quantity'] 
			);
			return $newItem;
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function update($arr) {
	if (isset ( $arr ['id'], $arr ['quantity'] )) {
		$id = $arr ['id'];
		$quantity = $arr ['quantity'];
		$bdd = getBDD ();
		$request = $bdd->exec ( "UPDATE at_courses SET quantity = '$quantity' WHERE id = '$id'" );
		if ($request == 1) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function delete($arr) {
	$bdd = getBDD ();
	if (isset ( $arr ['id'] )) {
		$id = $arr ['id'];
		$request = $bdd->exec ( "DELETE FROM at_courses WHERE id = '$id'" );
		if ($request == 1) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		$request = $bdd->exec ( "TRUNCATE at_courses" );
		if ($request > 0) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	}
}