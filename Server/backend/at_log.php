<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Log.php';

$page_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo get();
			http_response_code ( 202 );
			break;
		case 'PUT' :
			update($_GET);
			break;
		case 'DELETE':
			delete();
			break;
	}
} else {
	http_response_code ( 401 );
}

function get(){
	return (new Log())->read();
}

function update($arr){
	(new Log())->updateLevel($arr['level']);
}

function delete(){
	(new Log())->clear();	
}