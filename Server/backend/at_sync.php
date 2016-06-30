<?php
header ( "Access-Control-Allow-Origin: *" );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Sync.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			if(isset($_GET['lastmodified'])){
				http_response_code(202);
				echo json_encode(get($_GET['lastmodified']));
			}else{
				http_response_code(400);
			}						
			break;
	}
} else {
	http_response_code ( 403 );
}

function get($lastmodified) {
	$sync = new Sync();
	$output = array();
	if($lastmodified < $sync->get(Sync::SCENARIOS)){
		$output[Sync::SCENARIOS] = getScenarios();
	}
	if($lastmodified < $sync->get(Sync::EAN)){
		$output[Sync::EAN] = getEan();
	}
	return $output;
}

function getEan() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_ean ORDER BY nom' );
	$result = $req->fetchAll(PDO::FETCH_ASSOC);
	$req->closeCursor ();
	return $result;
}

function getCourses($lastmodified){
	$bdd = getBDD ();
	$req = $bdd->query ( "SELECT * FROM at_courses WHERE lastmodified > '$lastmodified' ORDER BY name" );
	$result = $req->fetchAll(PDO::FETCH_ASSOC);
	$req->closeCursor ();
	return $result;
}

function getScenarios(){
	
	$files = scandir ( __DIR__ . '/scenarios' );
	$result = array ();
	foreach ( $files as $file ) {
		if (pathinfo ( $file, PATHINFO_EXTENSION ) == 'php') {
			$file = pathinfo ( $file, PATHINFO_FILENAME );
			$xml = file_get_contents ( __DIR__ . '/scenarios/xml/' . $file . '.xml' );
			$php = file_get_contents ( __DIR__ . '/scenarios/' . $file . '.php' );
			$result [] = array (
					'file' => $file
			);
		}
	}
	return $result;
}