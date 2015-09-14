<?php
header ( "Access-Control-Allow-Origin: *" );
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
			add ( $_REQUEST );
			break;
		case 'PUT' :
			if (isset ( $_REQUEST ['ignore'] )) {
				ignore ( $_REQUEST ['ignore'] );
			}
			if (isset ( $_REQUEST ['open'] )) {
				open ( $_REQUEST ['open'] );
			}
			if (isset ( $_REQUEST ['quantite'], $_REQUEST ['id'] )) {
				if (isset ( $_REQUEST ['close'] )) {
					changeQuantity ( $_REQUEST ['id'], $_REQUEST ['quantite'], TRUE );
				} else {
					changeQuantity ( $_REQUEST ['id'], $_REQUEST ['quantite'] );
				}
			}
			break;
		case 'DELETE' :
			if (isset ( $_REQUEST ['id'] )) {
				delete ( $_REQUEST ['id'] );
			}
			break;
	}
}
function changeQuantity($id, $quantity, $close = FALSE) {
	$bdd = getBDD ();
	if ($close) {
		$request = $bdd->exec ( "UPDATE at_cuisine SET quantite = '$quantity', date2 = 0000-00-00, status = 0 WHERE id = '$id'" );
	} else {
		$request = $bdd->exec ( "UPDATE at_cuisine SET quantite = '$quantity' WHERE id = '$id'" );
	}
	if ($request == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function ignore($id) {
	$bdd = getBDD ();
	$request = $bdd->exec ( "UPDATE at_cuisine SET avoid = 1 WHERE id = '$id'" );
	if ($request == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function open($id) {
	$bdd = getBDD ();
	$request = $bdd->exec ( "UPDATE at_cuisine SET status = 1, date2 = now() WHERE id = '$id'" );
	if ($request == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function get() {
	$bdd = getBDD ();
	$request = $bdd->query ( 'SELECT * FROM at_cuisine LEFT JOIN at_ean ON at_cuisine.element = at_ean.ean ORDER BY date2 DESC, peremption' );
	// $request = $bdd->query ( 'SELECT *, SUM(quantite) as quantity FROM (SELECT * FROM at_cuisine LEFT JOIN at_ean ON at_cuisine.element = at_ean.ean ORDER BY date2 DESC, peremption) as tble GROUP BY element' );
	$today = strtotime ( now );
	$result = array ();
	while ( $data = $request->fetch () ) {
		$date = strtotime ( $data ['peremption'] ) - $today;
		$date = intval ( $date / 86400 );
		$data2 = strtotime ( $data ['date2'] ) - $today;
		$date2 = intval ( $date2 / 86400 );
		if ($data ['nom'] == NULL) {
			$label = $data ['element'];
			$ean = NULL;
		} else {
			$label = $data ['nom'];
			$ean = $data ['ean'];
		}
		$result [] = array (
				'id' => $data ['id'],
				'label' => $label,
				'ean' => $ean,
				'quantite' => $data ['quantite'],
				'peremption' => $date,
				'endroit' => $data ['endroit'],
				'status' => $data ['status'],
				'ouvert' => $date2,
				'ignore' => $data ['avoid'] 
		);
	}
	$request->closeCursor ();
	http_response_code ( 202 );
	return $result;
}
function delete($id) {
	$bdd = getBDD ();
	$request = $bdd->exec ( "DELETE FROM at_cuisine WHERE id = '$id'" );
	if ($request == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function add($values) {
	if (isset ( $values ['element'], $values ['peremption'] )) {
		$bdd = getBDD ();
		if (isset ( $values ['quantite'] ) && $values ['quantite'] > 0) {
			$quantite = $values ['quantite'];
		} else {
			$quantite = 1;
		}
		if (isset ( $values ['endroit'] )) {
			$place = $values ['endroit'];
		} else {
			$place = 'placard';
		}
		$element = $values ['element'];
		if (isset ( $values ['ean'] )) {
			$ean = $values ['element'];
			$label = $values ['ean'];
			$bdd->exec ( "INSERT INTO at_ean VALUES('$ean', '$label')" );
		}
		$peremption = $values ['peremption'];
		$request = $bdd->exec ( "INSERT INTO at_cuisine (`element`, `peremption`, `quantite`, `endroit`) VALUES('$element', '$peremption', '$quantite', '$place')" );
		if ($request == 1) {
			$id = $bdd->lastInsertId ();
			$newRequest = $bdd->query ( "SELECT * FROM at_cuisine LEFT JOIN at_ean ON at_cuisine.element = at_ean.ean WHERE at_cuisine.id = '$id' ORDER BY date2 DESC, peremption" );
			$data = $newRequest->fetch ();
			$today = strtotime ( now );
			$date = strtotime ( $data ['peremption'] ) - $today;
			$date = intval ( $date / 86400 );
			$data2 = strtotime ( $data ['date2'] ) - $today;
			$date2 = intval ( $date2 / 86400 );
			if ($data ['nom'] == NULL) {
				$label = $data ['element'];
				$ean = NULL;
			} else {
				$label = $data ['nom'];
				$ean = $data ['ean'];
			}
			$newItem = array (
					'id' => $data ['id'],
					'label' => $label,
					'ean' => $ean,
					'quantite' => $data ['quantite'],
					'peremption' => $date,
					'endroit' => $data ['endroit'],
					'status' => $data ['status'],
					'ouvert' => $date2,
					'ignore' => $data ['avoid'] 
			);
			echo json_encode ( $newItem );
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}