<?php 

require_once __DIR__ . '/../classes/Settings.php';
require_once __DIR__ . '/../classes/Log.php';

$settings = new Settings();

if($settings->getSettings('DDNS', 'on')){
	$host = urlencode($settings->getSettings('DDNS', 'host'));
	$username = $settings->getSettings('DDNS', 'username');
	$password = $settings->getSettings('DDNS', 'password');
	$ip = urlencode(file_get_contents('http://nawrasg.fr/ip.php'));
	$last_ip = $settings->getSettings('DDNS', 'last_ip');
	
	if($ip == $last_ip){
		(new Log())->log(Log::DEBUG, __FILE__, "No IP Change $ip");
	}else{
		$url = "http://www.ovh.com/nic/update?system=dyndns&hostname=$host&myip=$ip";
		$ch = curl_init ( $url );
		curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, TRUE );
		curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
		curl_setopt($ch, CURLOPT_USERPWD, "$username:$password");
		curl_setopt($ch, CURLOPT_TIMEOUT, 30);
		$response = curl_exec ( $ch );
		curl_close($ch);
		(new Log())->log(Log::INFO, __FILE__, $response);
		$settings->setSettings('DDNS', 'last_ip', $ip);
	}
}