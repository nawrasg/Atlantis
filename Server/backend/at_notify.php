<?php
header ( "Access-Control-Allow-Origin: *" );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/PushMessage.php';

$title = "";
$message = "";

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'POST' :
			notify ( $_REQUEST );
			break;
		case 'PUT' :
			command ( $_REQUEST );
			break;
	}
}
function command($arr) {
	if (isset ( $arr ['cmd'] )) {
		$push = new PushMessage ();
		$cmd = $arr ['cmd'];
		if (isset ( $arr ['id'] )) {
			$id = $arr ['id'];
			$push->sendMessageDevice ( 'at_commands', $cmd, $id );
			http_response_code ( 202 );
		} else {
			$push->sendMessageAll ( 'at_commands', $cmd );
			http_response_code ( 202 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function notify($arr) {
	if (isset ( $arr ['msg'] )) {
		if (isset ( $arr ['id'] )) {
			// TODO
		} else {
			$msg = $arr ['msg'];
			$push = new PushMessage ();
			$push->sendMessageAll ( 'Atlantis', $msg );
			http_response_code ( 202 );
		}
	} else {
		http_response_code ( 404 );
	}
}

if (isset ( $_GET ['api'] ) && checkAPI ( $_GET ['api'], $page_level )) {
	$push = new PushMessage ();
	$bdd = getBDD ();
	if (isset ( $_GET ['action'] )) {
		switch ($_GET ['action']) {
			case 'geo' :
				$message = "geo";
				$title = "at_commands";
				break;
			case 'geoi' :
				$message = "geoi";
				$title = "at_commands";
				break;
			case 'notifyall' :
				if (isset ( $_GET ['msg'], $_GET ['title'] )) {
					$message = $_GET ['msg'];
					$title = $_GET ['title'];
				}
				break;
		}
		$push->sendMessageAll ( $title, $message );
	} else {
		echo 404;
	}
} else if ($argc > 1) {
	$data = $argv [1];
	$result = explode ( ',', $data );
	$title = $result [0];
	$message = $result [1];
	$push = new PushMessage ();
	$push->sendMessageAll ( $title, $message );
} else {
	echo 401;
}
