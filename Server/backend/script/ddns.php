<?php 

require_once __DIR__ . '/../classes/Settings.php';
require_once __DIR__ . '/../classes/Log.php';

$settings = new Settings();

if($settings->getSettings('DDNS', 'on')){
	$host = urlencode($settings->getSettings('DDNS', 'host'));
	$username = $settings->getSettings('DDNS', 'username');
	$password = $settings->getSettings('DDNS', 'password');
	$ip = urlencode(file_get_contents('http://nawrasg.fr/ip.php'));
	$url = "http://www.ovh.com/nic/update?system=dyndns&hostname=$host&myip=$ip";
	
	$ch = curl_init ( $url );
	curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, TRUE );
	curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
	curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
	curl_setopt($ch, CURLOPT_TIMEOUT, 30);
	$response = curl_exec ( $ch );
	curl_close($ch);
	(new Log())->log(Log::INFO, __FILE__, $response);
}