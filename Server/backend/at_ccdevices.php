<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Sync.php';

$page_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			if (isset ( $_REQUEST ['ping'] )) {
				echo json_encode ( ping ( $_REQUEST ['ping'] ) );
			} else {
				echo json_encode ( get () );
			}
			break;
		case 'POST' :
			echo add ( $_REQUEST );
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
} else {
	http_response_code ( 403 );
}
function ping($ip) {
	$ping = exec ( '/atlantis/ping.sh ' . $ip );
	if ($ping != - 1) {
		$ping = 1;
	} else {
		$ping = - 1;
	}
	$result = array (
			'online' => $ping 
	);
	http_response_code ( 202 );
	return $result;
}
function get() {
	$arr = array (
			'devices' => getDevices (),
			'users' => getUsers () 
	);
	return $arr;
}
function getUsers() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT id, type, nom FROM at_users' );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$arr [] = array (
				'id' => $data ['id'],
				'nom' => $data ['nom'],
				'type' => $data ['type'] 
		);
	}
	$req->closeCursor ();
	return $arr;
}
function getDevices() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT at_devices.*, at_gcm.gcm FROM at_devices LEFT JOIN at_gcm ON at_devices.mac = at_gcm.mac ORDER BY nom' );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$ping = exec ( '/atlantis/ping.sh ' . $data ['ip'] );
		if ($ping != - 1) {
			$ping = 1;
		} else {
			$ping = - 1;
		}
		$arr [] = array (
				'id' => $data ['id'],
				'nom' => $data ['nom'],
				'ip' => $data ['ip'],
				'mac' => $data ['mac'],
				'type' => $data ['type'],
				'connexion' => $data ['connexion'],
				'username' => $data ['username'],
				'online' => $ping,
				'gcm' => $data ['gcm'] 
		);
	}
	$req->closeCursor ();
	return $arr;
}
function add($arr) {
	if (isset ( $arr ['title'], $arr ['ip'], $arr ['mac'], $arr ['type'], $arr ['connection'] )) {
		$bdd = getBDD ();
		if (isset ( $arr ['user'] )) {
			$user = $arr ['user'];
		} else {
			$user = NULL;
		}
		$title = $arr ['title'];
		$ip = $arr ['ip'];
		$mac = strtoupper ( $arr ['mac'] );
		$type = $arr ['type'];
		$connection = $arr ['connection'];
		$req = $bdd->exec ( "INSERT INTO at_devices(id, nom, ip, mac, type, connexion, username) VALUES('', '$title', '$ip', '$mac', '$type', '$connection', '$user')" );
		if ($req == 1) {
			$id = $bdd->lastInsertId ();
			$query = $bdd->query ( "SELECT * FROM at_devices WHERE id = '$id'" );
			$data = $query->fetch ();
			$ping = exec ( '/atlantis/ping.sh ' . $data ['ip'] );
			if ($ping != - 1) {
				$ping = 1;
			} else {
				$ping = - 1;
			}
			$newDevice = array (
					'id' => $data ['id'],
					'nom' => $data ['nom'],
					'ip' => $data ['ip'],
					'mac' => $data ['mac'],
					'type' => $data ['type'],
					'connexion' => $data ['connexion'],
					'username' => $data ['username'],
					'online' => $ping 
			);
			echo json_encode ( $newDevice );
			(new Sync())->update(Sync::DEVICES);
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function update($arr) {
	if (isset ( $arr ['id'], $arr ['title'], $arr ['ip'], $arr ['mac'], $arr ['type'], $arr ['connection'] )) {
		$bdd = getBDD ();
		if (isset ( $arr ['user'] )) {
			$user = $arr ['user'];
		} else {
			$user = NULL;
		}
		$id = $arr ['id'];
		$title = $arr ['title'];
		$ip = $arr ['ip'];
		$mac = strtoupper ( $arr ['mac'] );
		$type = $arr ['type'];
		$connection = $arr ['connection'];
		$req = $bdd->exec ( "UPDATE at_devices SET nom = '$title', ip = '$ip', mac = '$mac', type = '$type', connexion = '$connection', username = '$user' WHERE id = '$id'" );
		if ($req == 1) {
			(new Sync())->update(Sync::DEVICES);
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function delete($arr) {
	if (isset ( $arr ['id'] )) {
		$id = $arr ['id'];
		$bdd = getBDD ();
		$req = $bdd->query ( "SELECT * FROM at_devices WHERE id = '$id'" );
		$data = $req->fetch ();
		$mac = $data ['mac'];
		$bdd->exec ( "DELETE FROM at_gcm WHERE mac = '$mac'" );
		$req2 = $bdd->exec ( "DELETE FROM at_devices WHERE id = '$id'" );
		if ($req2 == 1) {
			(new Sync())->update(Sync::DEVICES);
			http_response_code ( 202 );
		} else {
			http_response_code ( 400 );
		}
	} else {
		http_response_code ( 404 );
	}
}