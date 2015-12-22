<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Settings.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get () );
			http_response_code ( 202 );
			break;
		case 'POST' :
			
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
		case 'DELETE' :
			
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
	$output = array (
			'daemon' => $daemon,
			'free_hdd' => $free_disk 
	);
	return $output;
}
function update($arr) {
	if (isset ( $arr ['daemon'] )) {
		switch ($arr ['daemon']) {
			case 'start' :
				$filename = __DIR__ . '/script/atlantis.php';
				exec ( "nohup php $filename &" );
				http_response_code ( 202 );
				break;
			case 'stop' :
				(new Settings ())->setSettings ( 'Daemon', 'stop', true );
				http_response_code ( 202 );
				break;
		}
	} else {
		http_response_code ( 404 );
	}
}