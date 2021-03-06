<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';

$page_level = 1;
$admin_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode ( get ( $_REQUEST ) );
			http_response_code ( 202 );
			break;
		case 'POST' :
			add ( $_REQUEST );
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
		case 'DELETE' :
			if(checkAPI($_REQUEST['api'], $admin_level)){
				delete ( $_REQUEST );
			}
			break;
	}
}
function delete($arr) {
	if (checkAPI ( $arr ['api'], 0 )) {
		if (isset ( $arr ['id'] )) {
			$id = $arr ['id'];
			$bdd = getBDD ();
			$req = $bdd->exec ( "DELETE FROM at_cameras WHERE id = $id" );
			if (! $req) {
				http_response_code ( 400 );
			} else {
				http_response_code ( 202 );
			}
		} else {
			http_response_code ( 404 );
		}
	} else {
		http_response_code ( 403 );
	}
}
function add($arr) {
	if (isset ( $arr ['ip'], $arr ['type'], $arr ['image'] )) {
		$ip = $arr ['ip'];
		$type = $arr ['type'];
		$image = $arr ['image'];
		isset ( $arr ['video'] ) ? $video = $arr ['video'] : $video = NULL;
		isset ( $arr ['alias'] ) ? $alias = $arr ['alias'] : $alias = NULL;
		isset ( $arr ['room'] ) ? $room = $arr ['room'] : $room = NULL;
		if (isset ( $arr ['username'], $arr ['password'] )) {
			$username = $arr ['username'];
			$password = $arr ['password'];
		} else {
			$username = NULL;
			$password = NULL;
		}
		$bdd = getBDD ();
		$req = $bdd->exec ( "INSERT INTO at_cameras VALUES('', '$ip', '$type', '$image', '$video', '$username', '$password', '$alias', '$room')" );
		if (! $req) {
			http_response_code ( 400 );
		} else {
			http_response_code ( 202 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function update($arr) {
	if (isset ( $arr ['id'], $arr ['alias'], $arr ['room'] )) {
		$id = $arr ['id'];
		$alias = $arr ['alias'];
		$room = $arr ['room'];
		$bdd = getBDD ();
		$req = $bdd->exec ( "UPDATE at_cameras SET `alias` = '$alias', `room` = '$room' WHERE `id` = '$id'" );
		if (! $req) {
			http_response_code ( 400 );
		} else {
			http_response_code ( 202 );
		}
	}
	if (isset ( $arr ['id'], $arr ['username'], $arr ['password'] )) {
		$id = $arr ['id'];
		$username = $arr ['username'];
		$password = $arr ['password'];
		$bdd = getBDD ();
		$req = $bdd->exec ( "UPDATE at_cameras SET `username` = '$username', `password` = '$password' WHERE `id` = '$id'" );
		if (! $req) {
			http_response_code ( 400 );
		} else {
			http_response_code ( 202 );
		}
	}
}
function get($arr) {
	$bdd = getBDD ();
	if (isset ( $arr ['id'] )) {
		$id = $arr ['id'];
		$req = $bdd->query ( "SELECT * FROM at_cameras WHERE id = $id" );
	} else {
		$req = $bdd->query ( 'SELECT at_cameras.*, at_room.room AS room_label FROM at_cameras INNER JOIN at_room ON at_cameras.room = at_room.id' );
	}
	$output = array ();
	while ( $data = $req->fetch () ) {
		$output [] = array (
				'id' => $data ['id'],
				'ip' => $data ['ip'],
				'type' => $data ['type'],
				'image' => $data ['image'],
				'video' => $data ['video'],
				'username' => $data ['username'],
				'password' => $data ['password'],
				'alias' => $data ['alias'],
				'room' => $data ['room'],
				'room_label' => $data['room_label']
		);
	}
	return $output;
}