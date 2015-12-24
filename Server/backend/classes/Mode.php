<?php
require_once __DIR__.'/Settings.php';
class Mode{
	
	const DAY = 'day';
	const NIGHT = 'night';
	const AWAY = 'away';
	
	function setMode($mode){
		(new Settings())->setSettings('Mode', 'mode', $mode);
	}
	
	function getMode(){
		return ((new Settings())->getSettings('Mode', 'mode'));
	}
}