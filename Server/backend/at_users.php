<?php

header ( "Access-Control-Allow-Origin: *" );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ .'/classes/connexion.php';
require_once __DIR__ .'/classes/checkAPI.php';

$page_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'GET' :
			echo json_encode(get());
			http_response_code(202);
			break;
		case 'POST' :
			if(isset($_REQUEST['name'], $_REQUEST['type'], $_REQUEST['pwd'])){
				add($_REQUEST);
				
			}else{
				http_response_code(404);
			}	
			break;
		case 'PUT' :
			update($_REQUEST);
			break;
		case 'DELETE' :
			delete($_REQUEST);
			break;
	}
}else{
	http_response_code(401);
}

function get() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT id, type, cle, nom, mail, phone FROM at_users' );
	$arr = array ();
	while ( $data = $req->fetch () ) {
		$arr [] = array (
				'id' => $data ['id'],
				'nom' => $data ['nom'],
				'cle' => $data ['cle'],
				'type' => $data ['type'],
				'mail' => $data['mail'],
				'phone' => $data['phone']
		);
	}
	$req->closeCursor();
	return $arr;
}

function add($arr){
	$name = $arr['name'];
	$type = $arr['type'];
	$pwd = 	cryptPassword($arr['pwd']);
	$api = generatePassword();
	$bdd = getBDD();
	$req = $bdd->exec("INSERT INTO at_users VALUES('', '$name', '$type', NULL, NULL, '$api', '$pwd')");
	if($req == 1){
		$newId = $bdd->lastInsertId();
		$req2 = $bdd->query ( "SELECT id, type, cle, nom, mail, phone FROM at_users WHERE id = '$newId'" );
		echo json_encode($req2->fetch());
		http_response_code(202);
	}else{
		http_response_code(400);
	}
}

function update($arr){
	if(isset($arr['id'], $arr['type'], $arr['mail'], $arr['phone'])){
		$id = $arr['id'];
		$type = $arr['type'];
		$mail = ($arr['mail'] == 'null' ? NULL : $arr['mail']);
		$phone = ($arr['phone'] == 'null' ? NULL : $arr['phone']);
		$bdd = getBDD();
		if(isset($arr['pwd'])){
			$pwd = cryptPassword($arr['pwd']);
			$request = $bdd->exec("UPDATE at_users SET type = '$type', password = '$pwd', mail = '$mail', phone = '$phone' WHERE id = '$id'");
		}else{			
			$request = $bdd->exec("UPDATE at_users SET type = '$type', mail = '$mail', phone = '$phone' WHERE id = '$id'");
		}
		if($request == 1){
			http_response_code(202);
		}else{
			http_response_code(400);
		}
	}else if(isset($arr['cle'])){
		$id = $arr['cle'];
		$bdd = getBDD();
		$api = generatePassword();
		$request = $bdd->exec("UPDATE at_users SET cle = '$api' WHERE id = '$id'");
		if($request == 1){
			echo $api;
			http_response_code(202);
		}else{
			http_response_code(400);
		}
	}
}

function delete($arr){
	if(isset($arr['id'])){
		$id = $arr['id'];
		$bdd = getBDD();
		$request = $bdd->exec("DELETE FROM at_users WHERE id = '$id'");
		if($request == 1){
			http_response_code(202);
		}
	}else{
		http_response_code(404);
	}
}

function cryptPassword($pwd){
	$sel = "ngSel";
	return crypt ( $pwd, $sel);
}

function generatePassword($length = 15) {
	$possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	$password = '';

	for($i = 0; $i < $length; $i++) {
		$rand = rand(0, strlen($possibleChars) - 1);
		$password .= substr($possibleChars, $rand, 1);
	}

	return $password;
}