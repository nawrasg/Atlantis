<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get () );
			break;
		case 'POST' :
			echo json_encode ( add ( $_REQUEST ) );
			break;
		case 'PUT' :
			echo update ( $_REQUEST );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
} else {
	http_response_code ( 403 );
}
function add($arr) {
	if (isset ( $arr ['nom'], $arr ['peremption'] )) {
		$bdd = getBDD ();
		if (! isset ( $arr ['qte'] ) || $arr ['qte'] == "") {
			$qte = 1;
		} else {
			$qte = $arr ['qte'];
		}
		$nom = ucfirst ( strtolower ( $arr ['nom'] ) );
		$peremption = $arr ['peremption'];
		$req = $bdd->exec ( "INSERT INTO at_entretien VALUES('', '$nom', '$peremption', '$qte')" );
		if ($req == 1) {
			$id = $bdd->lastInsertId ();
			$auj = strtotime ( now );
			$request = $bdd->query ( "SELECT * FROM at_entretien WHERE id = '$id'" );
			$data = $request->fetch ();
			$dt = strtotime ( $data ['peremption'] ) - $auj;
			$dt = intval ( $dt / 86400 );
			$produit = array (
					'id' => $data ['id'],
					'nom' => $data ['nom'],
					'quantite' => $data ['quantite'],
					'peremption' => $dt 
			);
			http_response_code ( 202 );
			return $produit;
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function get() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_entretien ORDER BY peremption' );
	$auj = strtotime ( now );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$dt = strtotime ( $data ['peremption'] ) - $auj;
		$dt = intval ( $dt / 86400 );
		$arr [] = array (
				'id' => $data ['id'],
				'nom' => $data ['nom'],
				'quantite' => $data ['quantite'],
				'peremption' => $dt 
		);
	}
	$req->closeCursor ();
	http_response_code ( 202 );
	return $arr;
}
function update($arr) {
	if (isset ( $arr ['id'] )) {
		$bdd = getBDD ();
		if (isset ( $arr ['qte'] ) && $arr ['qte'] > 0) {
			$id = $arr ['id'];
			$qte = $arr ['qte'];
			$req = $bdd->exec ( "UPDATE at_entretien SET quantite = '$qte' WHERE id = '$id'" );
		} else {
			$req = $bdd->prepare ( 'DELETE FROM at_entretien WHERE id = :id' );
			$req->execute ( array (
					'id' => $arr ['id'] 
			) );
			$req->closeCursor ();
		}
		http_response_code ( 202 );
	} else {
		http_response_code ( 404 );
	}
}
function delete($arr) {
	if (isset ( $arr ['id'] )) {
		$id = $arr ['id'];
		$bdd = getBDD ();
		$request = $bdd->exec ( "DELETE FROM at_entretien WHERE id = '$id'" );
		if ($request == 1) {
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}