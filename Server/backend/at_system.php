<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';

$page_level = 1;
$admin_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get () );
			http_response_code ( 202 );
			break;
		case 'PUT' :
			if (checkAPI ( $_REQUEST ['api'], $admin_level )) {
				update ( $_REQUEST );
			}
			break;
	}
}
function get() {
	$pid = (new Settings ())->getSettings ( 'Daemon', 'pid' );
	if ($pid == - 1) {
		$daemon = false;
	} else {
		exec ( "ps -p $pid", $result );
		if (count ( $result ) > 1) {
			$result = $result [1];
			$result = preg_replace ( "/\s+/", " ", $result );
			$data = explode ( " ", $result );
			if ($data [count ( $data ) - 1] == 'php') {
				$daemon = true;
			} else {
				$daemon = false;
			}
		} else {
			$daemon = false;
		}
	}
	$free_disk = disk_free_space ( '/' ) / disk_total_space ( '/' );
	$settings = new Settings ();
	$output = array (
			'daemon' => $daemon,
			'free_hdd' => $free_disk,
			'nightFrom' => $settings->getSettings ( 'Mode', 'nightFrom' ),
			'nightTo' => $settings->getSettings ( 'Mode', 'nightTo' ),
			'nightAuto' => $settings->getSettings ( 'Mode', 'nightAuto' ) 
	);
	return $output;
}
function update($arr) {
	if (isset ( $arr ['daemon'] )) {
		switch ($arr ['daemon']) {
			case 'start' :
				$filename = __DIR__ . '/script/atlantis.php';
				exec ( "nohup php $filename >/dev/null 2>&1 &" );
				http_response_code ( 202 );
				break;
			case 'stop' :
				(new Settings ())->setSettings ( 'Daemon', 'stop', true );
				http_response_code ( 202 );
				break;
		}
	} else if (isset ( $arr ['nightFrom'], $arr ['nightTo'] )) {
		(new Settings ())->setSettings ( 'Mode', 'nightFrom', $arr ['nightFrom'] );
		(new Settings ())->setSettings ( 'Mode', 'nightTo', $arr ['nightTo'] );
		http_response_code ( 202 );
	} else if (isset ( $arr ['nightAuto'] )) {
		$value = filter_var ( $arr ['nightAuto'], FILTER_VALIDATE_BOOLEAN );
		(new Settings ())->setSettings ( 'Mode', 'nightAuto', $value );
		http_response_code ( 202 );
	} else {
		http_response_code ( 404 );
	}
}