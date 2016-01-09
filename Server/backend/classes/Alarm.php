<?php
require_once __DIR__ . '/Settings.php';
class Alarm {
	public function isOn() {
		return (new Settings ())->getSettings ( 'Alarm', 'status' );
	}
	public function on() {
		(new Settings ())->setSettings ( 'Alarm', 'status', true );
		$this->exec_scenario ();
	}
	public function off() {
		(new Settings ())->setSettings ( 'Alarm', 'status', false );
		$this->exec_scenario ();
	}
	public function toggle() {
		if ($this->isOn ()) {
			$this->off ();
		} else {
			$this->on ();
		}
	}
	public function execAlarm(){
		$this->exec_scenario();
	}
	private function exec_scenario() {
		$scenario = __DIR__ . '/../scenarios/alarm.php';
		if (file_exists ( $scenario )) {
			exec ( "nohup php $scenario >/dev/null 2>&1 &" );
		}
	}
}