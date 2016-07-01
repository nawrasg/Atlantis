<?php
class Sync{
	const SCENARIOS = 'scenarios';
	const EAN = 'ean';
	const LIGHTS = 'lights';
	const ROOMS = 'rooms';
	const PLANTS = 'plants';
	
	private $data;
	private $file = 'sync.json';
	
	function __construct(){
		$json = file_get_contents($this->file, TRUE);
		$this->data = json_decode($json);
	}
	
	private function save(){
		file_put_contents($this->file, json_encode($this->data), FILE_USE_INCLUDE_PATH);
	}
	
	function update($section){
		$this->data->$section = time();
		$this->saveSettings();
	}
	
	function get($section){
		return $this->data->$section;
	}
}