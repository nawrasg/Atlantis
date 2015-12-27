<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 2;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'POST' :
			add ( $_REQUEST );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
}
function add($arr) {
	$mac = $arr ['api'];
	$gcm = $arr ['gcm'];
	$bdd = getBDD ();
	$req = $bdd->exec ( "INSERT INTO at_gcm VALUES('$mac', '$gcm')" );
	if ($req == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function delete($arr) {
	$mac = $arr ['mac'];
	$bdd = getBDD ();
	$req = $bdd->exec ( "DELETE FROM at_gcm WHERE mac = '$mac'" );
	if ($req == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}