<?php
require_once __DIR__ . '/Settings.php';

class Player {

	function stop() {
		exec ( 'sudo pkill mpg321' );
	}

	function play($path) {
		$this->stop ();
		if (! $this->isSilentMode ()) {
			return;
		}
		exec ( 'sudo nohup mpg321 ' . $path . ' &' );
	}

	function speak($string) {
		if (! $this->isSilentMode ()) {
			//return;
		}
		exec ( 'mpg321 "http://translate.google.com/translate_tts?tl=fr&q=' . urlencode ( $string ) . '"', $arr, $res );
		echo json_encode($res);
	}

	private function isSilentMode() {
		$settings = new Settings ();
		$H1 = $settings->getSettings ( "Silence", "H1" );
		$H2 = $settings->getSettings ( "Silence", "H2" );
		$currentHour = date ( 'H' );
		if ($H1 == - 1 || $H2 == - 1) {
			return FALSE;
		}
		return TRUE;
	}
}
