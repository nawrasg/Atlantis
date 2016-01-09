<?php
header ( 'Access-Control-Allow-Origin: *' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			get ( $_GET );
			break;
	}
}
function get($arr) {
	if (isset ( $arr ['id'], $arr ['type'] )) {
		$id = $arr ['id'];
		$type = $arr ['type'];
		switch ($type) {
			case 'plant' :
				getPlant ( $id );
				break;
			case 'camera' :
				getCamera ( $id );
				break;
		}
	} else if (isset ( $arr ['type'] )) {
		$type = $arr ['type'];
		switch ($type) {
			case 'plan' :
				getPlan ();
				break;
		}
	}
}
function getPlan() {
	$path = __DIR__ . "/home/plan.png";
	if (file_exists ( $path )) {
		http_response_code ( 202 );
		$img = fopen ( $path, 'rb' );
		header ( "Content-Type: image/png" );
		header ( "Content-Length: " . filesize ( $path ) );
		header ( "Timestamp: " . filemtime ( $path ) );
		header ( "Source: Atlantis" );
		fpassthru ( $img );
	} else {
		http_response_code ( 404 );
	}
}
function getPlant($id) {
	$path = __DIR__ . "/home/plants/$id.png";
	if (file_exists ( $path )) {
		http_response_code ( 202 );
		$img = fopen ( $path, 'rb' );
		header ( "Content-Type: image/png" );
		header ( "Content-Length: " . filesize ( $path ) );
		header ( "Timestamp: " . filemtime ( $path ) );
		header ( "Source: Atlantis" );
		fpassthru ( $img );
	} else {
		http_response_code ( 404 );
	}
}
function getCamera($id) {
	$bdd = getBDD ();
	$req = $bdd->query ( "SELECT * FROM at_cameras WHERE id = $id" );
	$data = $req->fetch ();
	if (! $data) {
		http_response_code ( 404 );
		return;
	}
	saveImage ( $data );
	$path = __DIR__ . "/home/cameras/$id.png";
	if (file_exists ( $path )) {
		http_response_code ( 202 );
		$img = fopen ( $path, 'rb' );
		header ( "Content-Type: image/png" );
		header ( "Content-Length: " . filesize ( $path ) );
		header ( "Timestamp: " . filemtime ( $path ) );
		header ( "Source: Atlantis" );
		fpassthru ( $img );
	} else {
		http_response_code ( 404 );
	}
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
		$directory = __DIR__ . "/home/cameras/$id";
		file_put_contents ( $path, $image );
		if(!file_exists($directory)){
			mkdir($directory);
		}
		$current_date = date('Y-m-d_H-i-s');
		$image_history = $directory."/$current_date.png";
		file_put_contents($image_history, $image);
	}
}