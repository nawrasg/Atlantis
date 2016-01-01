<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';
require_once __DIR__ . '/classes/Player.php';
require_once __DIR__ . '/classes/Light.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			get ( $_GET );
			break;
		case 'POST' :
			get ( $_POST );
			break;
	}
}
function get($arr) {
	$settings = new Settings ();
	if ($settings->getSettings ( 'CallNotifier', 'status' )) {
		if (isset ( $arr ['message'] )) {
			message ( $arr );
		} else {
			call ( $arr );
		}
	}
}
function call($arr) {
	$settings = new Settings ();
	if($settings->getSettings('CallNotifier', 'voice')){
		(new Player())->sound(Player::NOTIFICATION);
		(new Player())->sound(Player::INCOMING_CALL);	
	}
	light_notification(true);
}
function message($arr) {
	$settings = new Settings ();
	if($settings->getSettings('CallNotifier', 'voice')){
		(new Player())->sound(Player::NOTIFICATION);
		(new Player())->sound(Player::INCOMING_MESSAGE);
	}
	light_notification(false);
}
function light_notification($call = true){
	$lights = Light::allLights();
	$status = $lights->isOn();
	for($i = 0; $i < 2; $i++){
		$lights->on(true);
		sleep(1);
		$lights->on(false);
		sleep(1);
	}
	
}