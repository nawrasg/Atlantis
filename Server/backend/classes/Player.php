<?php
require_once __DIR__ . '/Settings.php';
class Player {
	function stop() {
		exec ( 'sudo pkill mpg321' );
	}
	function play($path) {
		$this->stop ();
		exec ( 'sudo nohup mpg321 ' . $path . ' &' );
	}
	function speak($string) {
		exec ( 'mpg321 "http://translate.google.com/translate_tts?tl=fr&q=' . urlencode ( $string ) . '"', $arr, $res );
		echo json_encode ( $res );
	}
	public function beep() {
		$path = __DIR__ . "/../home/sound/notification.mp3";
		exec ( "mpg321 $path" );
	}
}
