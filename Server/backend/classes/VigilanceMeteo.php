<?php
require_once __DIR__ . '/Settings.php';

/**
 * Based on: http://vigilance.meteofrance.com/data/NXFR34_LFPW_.xml
 *
 * @author Nawras GEORGI
 */
class VigilanceMeteo {

    var $url = "http://vigilance.meteofrance.com/data/NXFR34_LFPW_.xml";
    var $data;
    var $dep;
    var $meteo;

    public function __construct() {
        $xml = simplexml_load_file($this->url);
        $this->data = $xml;
        $this->dep = (new Settings())->getSettings('Atlantis', 'dep');
        $size = count($xml);
        foreach($xml as $dep){
        	if($dep['dep'] == $this->dep){
				$this->meteo = $dep;
        	}
        }
    }

    function getVigilance(){
    	$value = $this->meteo['couleur'];
    	return $this->getColor($value);
    }
    
    function getDepartement(){
    	return $this->meteo['dep'];
    }
    
    function getRisk(){
    	if(isset($this->meteo->crue)){
    		$crue = $this->getColor($this->meteo->crue['valeur']);
    	}else{
    		$crue = 'na';
    	}
    	if(isset($this->meteo->risque)){
    		$risque = $this->getRisque($this->meteo->risque['valeur']);
    	}else{
    		$risque = 'na';
    	}
    	return array (
    			'vigilance' => $this->getVigilance(),
				'crue' => $crue,
				'risque' => $risque 
		);
    }
    
    private function getColor($value){
    	switch($value){
    		case '1':
    			return 'vert';
    		case '2':
    			return 'jaune';
    		case '3':
    			return 'orange';
    		case '4':
    			return 'rouge';
    	}
    }
    
    private function getRisque($value){
    	switch($value){
    		case '1':
    			return 'vent';
    		case '2':
    			return 'pluie-inondation';
    		case '3':
    			return 'orages';
    		case '4':
    			return 'inondations';
    		case '5':
    			return 'neige-verglas';
    		case '6':
    			return 'canicule';
    		case '7':
    			return 'grand froid';
    	}
    }

}
