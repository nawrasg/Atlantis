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
			echo json_encode ( get ( $_REQUEST ) );
			http_response_code ( 202 );
			break;
		case 'POST' :
			
			break;
		case 'PUT' :
			
			break;
		case 'DELETE' :
			
			break;
	}
}
function get($arr) {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_cameras' );
	$output = array ();
	while ( $data = $req->fetch () ) {
		saveImage ( $data );
		$output [] = array (
				'id' => $data ['id'],
				'ip' => $data ['ip'],
				'type' => $data ['type'],
				'image' => $data ['image'],
				'video' => $data ['video'],
				'username' => $data ['username'],
				'password' => $data ['password'],
				'alias' => $data ['alias'],
				'room' => $data ['room'] 
		);
	}
	return $output;
}
function saveImage($arr) {
	if (isset ( $arr ['ip'], $arr ['image'] )) {
		$url = 'http://' . $arr ['ip'] . '/' . $arr ['image'];
		$ch = curl_init ( $url );
		curl_setopt ( $ch, CURLOPT_HEADER, 0 );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, 1 );
		curl_setopt ( $ch, CURLOPT_BINARYTRANSFER, 1 );
		if (isset ( $arr ['username'], $arr ['password'] )) {
			$username = $arr ['username'];
			$password = $arr ['password'];
			curl_setopt ( $ch, CURLOPT_USERPWD, "$username:$password" );
			curl_setopt ( $ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC );
		}
		$image = curl_exec ( $ch );
		curl_close ( $ch );
		$id = $arr ['id'];
		$path = __DIR__ . "/home/cameras/$id.png";
		file_put_contents ( $path, $image );
	}
}