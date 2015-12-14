<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, PUT' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';

$page_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo get ( $_GET );
			http_response_code ( 202 );
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
	}
} else {
	http_response_code ( 401 );
}
function get($arr) {
	$settings = new Settings ();
	if (isset ( $arr ['type'] )) {
		$type = $arr ['type'];
		switch ($type) {
			case 'Atlantis' :
				return json_encode ( $settings->getSectionSettings ( 'Atlantis' ) );
			case 'CallNotifier' :
				return json_encode ( $settings->getSectionSettings ( 'CallNotifier' ) );
		}
	} else {
		return $settings->getAllSettings ();
	}
}
function update($arr) {
	$settings = new Settings ();
	if (isset ( $arr ['section'] )) {
		switch ($arr ['section']) {
			case 'Atlantis' :
				if (isset ( $arr ['url'] ))
					$settings->setSettings ( 'Atlantis', 'url', $arr ['url'] );
				if (isset ( $arr ['dep'] ))
					$settings->setSettings ( 'Atlantis', 'dep', $arr ['dep'] );
				if (isset ( $arr ['city'] ))
					$settings->setSettings ( 'Atlantis', 'city', $arr ['city'] );
				if (isset ( $arr ['lat'] ))
					$settings->setSettings ( 'Atlantis', 'lat', $arr ['lat'] );
				if (isset ( $arr ['long'] ))
					$settings->setSettings ( 'Atlantis', 'long', $arr ['long'] );
				if (isset ( $arr ['radius'] ))
					$settings->setSettings ( 'Atlantis', 'radius', $arr ['radius'] );
				http_response_code ( 202 );
				return;
			case 'Notification' :
				if (isset ( $arr ['key'] )) {
					$settings->setSettings ( 'Notification', 'key', $arr ['key'] );
				}
				if (isset ( $arr ['appid'] )) {
					$settings->setSettings ( 'Weather', 'appid', $arr ['appid'] );
				}
				http_response_code ( 202 );
				return;
			case 'Zwave' :
				if (isset ( $arr ['ip'] ))
					$settings->setSettings ( 'Zwave', 'IP', $arr ['ip'] );
				if (isset ( $arr ['port'] ))
					$settings->setSettings ( 'Zwave', 'Port', $arr ['port'] );
				http_response_code ( 202 );
				return;
			case 'Hue' :
				if (isset ( $arr ['ip'] ))
					$settings->setSettings ( 'Hue', 'ip', $arr ['ip'] );
				if (isset ( $arr ['user'] ))
					$settings->setSettings ( 'Hue', 'user', $arr ['user'] );
				http_response_code ( 202 );
				return;
			case 'Files' :
				if (isset ( $arr ['music'] )) {
					$settings->setSettings ( 'Files', 'music', $arr ['music'] );
					http_response_code ( 202 );
				}
				return;
			case 'CallNotifier' :
				if (isset ( $arr ['key'], $arr ['value'] )) {
					$key = $arr ['key'];
					$value = filter_var ( $arr ['value'], FILTER_VALIDATE_BOOLEAN );
					$settings->setSettings ( 'CallNotifier', $key, $value );
				}
				return;
		}
	} else {
		http_response_code ( 404 );
	}
}
