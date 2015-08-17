<?php 

/**
 * Nawras GEORGI - July 2015
 * @version 0.1
 */

function generatePassword($length = 15) {
	$possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	$password = '';

	for($i = 0; $i < $length; $i++) {
		$rand = rand(0, strlen($possibleChars) - 1);
		$password .= substr($possibleChars, $rand, 1);
	}

	return $password;
}

if($argc > 4){
	//Updating connexion.php credentials
	$host = $argv[1];
	$db = $argv[2];
	$user = $argv[3];
	$password = $argv[4];
	
	$connexion = file_get_contents('backend/classes/connexion.php');
	$connexion = str_replace('ngHost', $host, $connexion);
	$connexion = str_replace('ngDB', $db, $connexion);
	$connexion = str_replace('ngUser', $user, $connexion);
	$connexion = str_replace('ngPassword', $password, $connexion);
	$result = file_put_contents('backend/classes/connexion.php', $connexion);
	if(!$result) die('Installation impossible !');
	
	//Creating user specific salt
	$sel = generatePassword();
	
	//Updating salt into at_users.php
	$at_users = file_get_contents('backend/at_users.php');
	$at_users = str_replace('ngSel', $sel, $at_users);
	$result = file_put_contents('backend/at_users.php', $at_users);
	if(!$result) die('Installation impossible !');
	
	//Updating salt into at_auth.php
	$at_auth = file_get_contents('backend/at_auth.php');
	$at_auth = str_replace('ngSel', $sel, $at_auth);
	$result = file_put_contents('backend/at_auth.php', $at_auth);
	if(!$result) die('Installation impossible !');
	
	//Creating admin password
	$userPassword = crypt('admin', $sel);
	$api = generatePassword();
	
	try {
		$bdd = new PDO('mysql:host='.$host, $user, $password);
		$bdd->query("CREATE DATABASE IF NOT EXISTS $db");
		$bdd->query("use $db");
		
		//Creating databses
		$sql = file_get_contents('Atlantis-master/Server/atlantis.sql');
		$createRequest = $bdd->exec($sql);
		
		//Inserting Admin user
		$request = $bdd->exec("INSERT INTO at_users VALUES('', 'Admin', '0', NULL, NULL, '$api', '$userPassword')");
		if($request != 1){
			die("Couldn't add admin user!\n");
		}
	} catch (Exception $e) {
		die($e->getMessage()."\n");
	}
	
	//Modify crontab
	$at_cron = file_get_contents('Atlantis-master/Server/crontab');
	$at_cron = str_replace('ngWebPath', getcwd(), $at_cron);
	$result = file_put_contents('Atlantis-master/Server/crontab', $at_cron);
	if(!$result) echo "Cannot install cron jobs!\n";
}

