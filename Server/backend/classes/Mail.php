<?php
require_once __DIR__ . '/Settings.php';
require_once __DIR__ . '/PHPMailer/class.phpmailer.php';
require_once __DIR__ . '/PHPMailer/class.smtp.php';
class Mail {
	var $mail;
	function __construct() {
		$settings = new Settings();
		$this->mail = new PHPMailer ();
		$this->mail->isSMTP ();
		$fromName = $settings->getSettings('SMTP', 'fromName');
		$fromMail = $settings->getSettings('SMTP', 'fromMail');
		$this->mail->setFrom($fromMail, $fromName);
		$this->mail->Subject = 'Atlantis';
		$this->mail->Host = $settings->getSettings('SMTP', 'server');
		$this->mail->Port = $settings->getSettings('SMTP', 'port');
		$security = $settings->getSettings('SMTP', 'security');
		if($security != 'no'){
			$this->mail->SMTPSecure = $security;
		}
		if($settings->getSettings('SMTP', 'auth')){
			$this->mail->SMTPAuth = true;
			$this->mail->Username = $settings->getSettings('SMTP', 'username');
			$this->mail->Password = $settings->getSettings('SMTP', 'password');
		}
	}
	function addTo($address, $name = null) {
		if ($name == null) {
			$this->mail->addAddress ( $address );
		} else {
			$this->mail->addAddress ( $address, $name );
		}
	}
	function setSubject($subject){
		$this->mail->Subject = $subject;
	}
	function setBody($body){
		$this->mail->Body = $body;
	}
	function send($users = null) {
		return $this->mail->send ();
	}
}