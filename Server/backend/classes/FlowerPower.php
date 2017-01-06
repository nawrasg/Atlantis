<?php
class FlowerPower {
	function discover() {
		// $devices = $this->bleScan();
		$devices = array (
				'90:03:B7:CA:2D:14' 
		);
		$output = array ();
		foreach ( $devices as $mac ) {
			$name = $this->getName ( $mac );
			$color = $this->getColor ( $mac );
			$battery = $this->getBatteryLevel ( $mac );
			$output [] = array (
					'name' => $name,
					'$color' => $color,
					'mac' => $mac,
					'battery' => $battery 
			);
		}
		return $output;
	}
	private function bleScan() {
		exec ( "sudo timeout -s SIGINT 10s hcitool -i hci0 lescan", $output, $code );
		$devices = array ();
		foreach ( $output as $item ) {
			$data = explode ( " ", $item );
			if ($this->isMacAddress ( $data [0] )) {
				if (! in_array ( $data [0], $devices )) {
					if ($this->isParrot ( $data [0] )) {
						array_push ( $devices, $data [0] );
					}
				}
			}
		}
		return $devices;
	}
	function 
	private function getBatteryLevel($mac) {
		$cmd = exec ( "sudo gatttool -i hci0 -b $mac --char-read --uuid=2a19" );
		$value = $this->getValue ( $cmd );
		return hexdec ( $value );
	}
	private function getName($mac) {
		$cmd = exec ( "sudo gatttool -i hci0 -b $mac --char-read --uuid=39e1fe03-84a8-11e2-afba-0002a5d5c51b" );
		$data = $this->getValue ( $cmd );
		$name = $this->hexToStr ( $data );
		return $name;
	}
	private function getColor($mac) {
		$cmd = exec ( "sudo gatttool -i hci0 -b $mac --char-read --uuid=39e1fe04-84a8-11e2-afba-0002a5d5c51b" );
		$value = $this->getValue ( $cmd );
		$value = intval ( $value );
		switch ($value) {
			case 100 :
				return 'Brown';
			case 200 :
				return 'Esmerald';
			case 300 :
				return 'Lemon';
			case 400 :
				return 'Gray Brown';
			case 500 :
				return 'Gray Green';
			case 600 :
				return 'Classic Green';
			case 700 :
				return 'Gray Blue';
		}
	}
	function toggleLed($mac) {
		exec ( "sudo gatttool -i hci0 -b $mac --char-write --handle=0x003c --value=01" );
	}
	private function isMacAddress($mac) {
		return (preg_match ( '/([a-fA-F0-9]{2}[:|\-]?){6}/', $mac ) == 1);
	}
	private function isParrot($mac) {
		return (strncmp ( $mac, '90:03:B7', 8 ) == 0);
	}
	private function hexToStr($hex) {
		$string = '';
		for($i = 0; $i < strlen ( $hex ) - 1; $i += 2) {
			$string .= chr ( hexdec ( $hex [$i] . $hex [$i + 1] ) );
		}
		return $string;
	}
	private function getValue($value) {
		$value = explode ( "value: ", $value );
		return str_replace ( ' ', '', $value [1] );
	}
}