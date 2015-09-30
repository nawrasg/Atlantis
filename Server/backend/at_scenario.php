<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: content-type' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;

$input = file_get_contents ( 'php://input' );
if ($input) {
	$input = json_decode ( $input );
	$api = $input->api;
} else {
	$api = $_GET ['api'];
}
if (isset ( $api ) && checkAPI ( $api, $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'PUT' :
			add ( $input );
			break;
		case 'GET' :
			echo json_encode ( get ( $_GET ) );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
}
function delete($arr) {
	if (isset ( $arr ['scenario'] )) {
		$scenario = $arr ['scenario'];
		$filename = __DIR__ . '/scenarios/' . $scenario . '.php';
		$xml = __DIR__ . '/scenarios/xml/' . $scenario . '.xml';
		$result = unlink ( $filename );
		$result_xml = unlink ( $xml );
		if ($result && $result_xml) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function add($arr) {
	if (isset ( $arr->name, $arr->code, $arr->xml )) {
		$filename = __DIR__ . '/scenarios/' . $arr->name . '.php';
		$filename2 = __DIR__ . '/scenarios/xml/' . $arr->name . '.xml';
		$xml = $arr->xml;
		$data = "<?php\n";
		$data .= 'function __autoload($class_name){' . "\n";
		$data .= "\t" . 'require_once __DIR__.\'/../classes/\'.$class_name.\'.php\';' . "\n";
		$data .= "}\n";
		$data .= $arr->code;
		$result = file_put_contents ( $filename, $data );
		$result_xml = file_put_contents ( $filename2, $xml );
		if (! $result && ! $result_xml) {
			http_response_code ( 400 );
		} else {
			http_response_code ( 202 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function get($arr) {
	if (isset ( $arr ['scenario'] )) {
		include __DIR__ . '/scenarios/' . $arr ['scenario'] . '.php';
		http_response_code ( 202 );
		return array (
				'result' => 202 
		);
	} else {
		$files = scandir ( __DIR__ . '/scenarios' );
		$result = array ();
		foreach ( $files as $file ) {
			if (pathinfo ( $file, PATHINFO_EXTENSION ) == 'php') {
				$file = pathinfo ( $file, PATHINFO_FILENAME );
				$xml = file_get_contents ( __DIR__ . '/scenarios/xml/' . $file . '.xml' );
				$result [] = array (
						'file' => $file,
						'xml' => (! $xml ? '' : $xml) 
				);
			}
		}
		http_response_code ( 202 );
		return $result;
	}
}