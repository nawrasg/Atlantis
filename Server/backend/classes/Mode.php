<?php
require_once __DIR__.'/Settings.php';
class Mode{
	
	const DAY = 'day';
	const NIGHT = 'night';
	const AWAY = 'away';
	
	function setMode($mode){
		(new Settings())->setSettings('Mode', 'mode', $mode);
		$this->exec_scenario($mode);
	}
	
	function getMode(){
		return ((new Settings())->getSettings('Mode', 'mode'));
	}
	
	private function exec_scenario($mode) {
		switch($mode){
			case Mode::DAY:
				$scenario = __DIR__ . '/../scenarios/mode_day.php';
				break;
			case Mode::NIGHT:
				$scenario = __DIR__ . '/../scenarios/mode_night.php';
				break;
			case Mode::AWAY:
				$scenario = __DIR__ . '/../scenarios/mode_away.php';
				break;
				
		}
		if (file_exists ( $scenario )) {
			exec ( "nohup php $scenario >/dev/null 2>&1 &" );
		}
	}
}