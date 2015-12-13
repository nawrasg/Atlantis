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
		$string = urlencode ( $string );
		exec ( "curl 'http://translate.google.com/translate_tts?ie=UTF-8&q=$string&tl=fr&client=t' -H 'Referer: http://translate.google.com/' -H 'User-Agent: stagefright/1.2 (Linux;Android 5.0)' > msg.mp3" );
		exec ( 'mpg321 msg.mp3' );
	}
	public function beep() {
		$path = __DIR__ . "/../home/sound/notification.mp3";
		exec ( "mpg321 $path" );
	}
}
