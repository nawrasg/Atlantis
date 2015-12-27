<?php

class Music {

	public function pause() {
		exec ( 'sudo mocp -G' );
	}

	public function volume($level = 90, $headphone = FALSE) {
		if($headphone){
			exec ( 'sudo amixer cset numid=6 -- ' . $level . '%', $output, $code );
		}else{
			exec ( 'sudo amixer cset numid=1 -- ' . $level . '%', $output, $code );
		}
	}

	public function stop() {
		exec ( 'sudo mocp -s' );
		$this->clearPlaylist ();
	}

	public function next() {
		exec ( 'sudo mocp -f' );
	}

	public function previous() {
		exec ( 'sudo mocp -r' );
	}

	public function shuffle() {
		exec ( 'sudo mocp -t shuffle' );
	}

	public function repeat() {
		exec ( 'sudo mocp -t repeat' );
	}

	public function off() {
		exec ( 'sudo mocp -x' );
	}

	public function on() {
		exec ( 'sudo mocp -S' );
	}

	public function clearPlaylist() {
		exec ( 'sudo mocp -c' );
	}

	public function start() {
		exec ( 'sudo mocp -p' );
	}

	public function play($link) {
		exec ( 'sudo mocp -l ' . $link );
	}

	public function add($path) {
		$p = str_replace ( " ", "\ ", $path );
		exec ( 'sudo mocp -a ' . $p );
	}

	public function isOn() {
		exec ( 'sudo mocp -Q %state', $output, $val );
		if ($val == 2) {
			return FALSE;
		} else {
			return TRUE;
		}
	}

	public function isPlay() {
		if ($this->isOn ()) {
			exec ( 'sudo mocp -Q %state', $output );
			if ($output [0] == "STOP") {
				return FALSE;
			} else {
				return TRUE;
			}
		} else {
			return FALSE;
		}
	}

	public function getVol($headphone = FALSE) {
		if ($headphone) {
			exec ( 'sudo amixer cget numid=6', $output, $var );
		} else {
			exec ( 'sudo amixer cget numid=1', $output, $var );
		}
		$line = $output [2];
		$data = explode ( '=', $line );
		return $this->map ( $data [1], 0, 64, 0, 10 );
	}

	private function map($value, $fromLow, $fromHigh, $toLow, $toHigh) {
		$fromRange = $fromHigh - $fromLow;
		$toRange = $toHigh - $toLow;
		$scaleFactor = $toRange / $fromRange;
		$newValue = $value - $fromLow;
		$newValue *= $scaleFactor;
		return floor ( $newValue + $toLow );
	}
}
