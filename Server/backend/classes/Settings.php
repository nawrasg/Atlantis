<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Settings
 *
 * @author Nawras
 */
class Settings {
    
    private $data;
    private $file = 'settings.json';
    
    public function __construct() {
        $json = file_get_contents($this->file, TRUE);
        $this->data = json_decode($json);
    }
    
    private function saveSettings(){
        file_put_contents($this->file, json_encode($this->data), FILE_USE_INCLUDE_PATH);
    }
    
    public function getSettings($section, $param){
        for($i = 0; $i < count($this->data); $i++){
            if($this->data[$i]->{'name'} == $section){
                return $this->data[$i]->{$param};
            }
        }
        return false;
    }
    
    public function getSectionSettings($section){
        for($i = 0; $i < count($this->data); $i++){
            if($this->data[$i]->{'name'} == $section){
                return $this->data[$i];
            }
        }
        return false;
    }
    
    public function setSettings($section, $param, $value){
        for($i = 0; $i < count($this->data); $i++){
            if($this->data[$i]->{'name'} == $section){
                $this->data[$i]->{$param} = $value;
            }
        }
        $this->saveSettings();
    }
    
    public function getAllSettings(){
        return json_encode($this->data);
    }
}
