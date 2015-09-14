<?php
header ( "Access-Control-Allow-Origin: *" );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			if (isset ( $_REQUEST ['ean'] )) {
				echo get ( $_REQUEST );
			} else {
				echo json_encode ( getAll () );
			}
			break;
		case 'POST' :
			echo add ( $_REQUEST );
			break;
	}
} else {
	http_response_code ( 403 );
}
function add($arr) {
	if (isset ( $arr ['ean'], $arr ['nom'] ) && $arr ['ean'] != '') {
		$bdd = getBDD ();
		$nom = ucwords ( strtolower ( $arr ['nom'] ) );
		$req = $bdd->prepare ( 'INSERT INTO at_ean VALUES(:ean, :nom)' );
		$req->execute ( array (
				'ean' => $arr ['ean'],
				'nom' => $nom 
		) );
		$req->closeCursor ();
		http_response_code ( 202 );
	} else {
		http_response_code ( 404 );
	}
}
function get($arr) {
	if (isset ( $arr ['ean'] )) {
		$bdd = getBDD ();
		$req = $bdd->prepare ( 'SELECT * FROM at_ean WHERE ean = :ean' );
		$req->execute ( array (
				'ean' => $_GET ['ean'] 
		) );
		$resultat = $req->fetch ();
		if ($resultat) {
			http_response_code ( 202 );
			return $resultat ['nom'];
		} else {
			http_response_code ( 404 );
		}
	}
}
function getAll() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_ean ORDER BY nom' );
	$result = array ();
	while ( $data = $req->fetch () ) {
		$result [] = array (
				'nom' => $data ['nom'],
				'ean' => $data ['ean'] 
		);
	}
	$req->closeCursor ();
	http_response_code ( 202 );
	return $result;
}