<?php
require_once __DIR__ . '/../classes/connexion.php';
require_once __DIR__ . '/../classes/Settings.php';

$settings = new Settings ();
$photos_number = $settings->getSettings ( 'Security', 'photosNumber' );
$photos_interval = $settings->getSettings ( 'Security', 'photosSeconds' );
$cameras = array ();
$bdd = getBDD ();
$req = $bdd->query ( 'SELECT * FROM at_cameras' );
while ( $data = $req->fetch () ) {
	$cameras [] = array (
			'id' => $data ['id'],
			'ip' => $data ['ip'],
			'image' => $data ['image'],
			'username' => $data ['username'],
			'password' => $data ['password'] 
	);
}
$size = count($cameras);
for($i = 0; $i < $photos_number; $i ++) {
	for($j = 0; $j < $size; $j++){
		saveImage ( $cameras [$j] );
	}
	sleep ( $photos_interval );
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
		$path = __DIR__ . "/../home/cameras/$id.png";
		$directory = __DIR__ . "/../home/cameras/$id";
		file_put_contents ( $path, $image );
		if (! file_exists ( $directory )) {
			mkdir ( $directory );
		}
		$current_date = date ( 'Y-m-d_H-i-s' );
		$image_history = $directory . "/$current_date.png";
		file_put_contents ( $image_history, $image );
	}
}