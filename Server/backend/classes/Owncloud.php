<?php

require_once __DIR__. '/Settings.php';

class Owncloud{
	var $oc;
	var $admin_username;
	var $admin_password;
	
	function __construct(){
		$settings = new Settings();
		$this->oc = $settings->getSettings('ownCloud', 'path');
		$this->admin_username = $settings->getSettings('ownCloud', 'username');
		$this->admin_password = $settings->getSettings('ownCloud', 'password');
	}
	
	function addUser($username, $password){
		$url = $this->oc . "/ocs/v1.php/cloud/users";
		$data = ['userid' => $username, 'password' => $password];
		
		$ch = curl_init ( $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, TRUE );
		curl_setopt ( $ch, CURLOPT_CUSTOMREQUEST, "POST" );
		curl_setopt ( $ch, CURLOPT_POSTFIELDS, $data );
		curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
		curl_setopt($ch, CURLOPT_USERPWD, "$this->admin_username:$this->admin_password");
		curl_setopt($ch, CURLOPT_TIMEOUT, 30);
		$response = curl_exec ( $ch );
		curl_close($ch);
		
		$ocs = new SimpleXMLElement($response);
		$code = $ocs->meta[0]->statuscode;
		if($code == 100){
			return true;
		}else{
			return false;
		}
	}
	
	function removeUser($username){
		$url = $this->oc . "/ocs/v1.php/cloud/users/$username";
		
		$ch = curl_init ( $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, TRUE );
		curl_setopt ( $ch, CURLOPT_CUSTOMREQUEST, "DELETE" );
		curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
		curl_setopt($ch, CURLOPT_USERPWD, "$this->admin_username:$this->admin_password");
		curl_setopt($ch, CURLOPT_TIMEOUT, 30);
		$response = curl_exec ( $ch );
		curl_close($ch);
		
		$ocs = new SimpleXMLElement($response);
		$code = $ocs->meta[0]->statuscode;
		if($code == 100){
			return true;
		}else{
			return false;
		}
	}
}