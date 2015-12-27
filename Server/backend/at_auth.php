<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: Authorization' );

require_once __DIR__ . '/classes/connexion.php';

$sel = "ngSel";

$auth = getallheaders ()['Authorization'];
$auth_decoded = base64_decode ( $auth );
$arr = explode ( ":", $auth_decoded );

$username = $arr [0];
$pwd = crypt ( $arr [1], $sel);

$bdd = getBDD ();
$request = $bdd->query ( "SELECT * FROM at_users WHERE nom = '$username' AND password = '$pwd'" );
$result = $request->fetch ();

if (!$result) {
	http_response_code(403);
}else{
	http_response_code ( 202 );
	$user = array (
			'id' => $result ['id'],
			'nom' => $result ['nom'],
			'type' => $result ['type'],
			'mail' => $result ['mail'],
			'phone' => $result ['phone'],
			'cle' => $result ['cle']
	);
	echo json_encode($user);
}