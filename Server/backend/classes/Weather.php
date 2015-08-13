<?php

class Weather{
	var $data;
	var $city;
	
	function __construct($city = 'rennes'){
		$this->city = $city;
		$result = $this->fetchURL($city, 7);
		while($result->cod != '200'){
			sleep(1);
			$result = $this->fetchURL($city, 7);
		}
		$this->data = $result;
	}
    
	private function fetchURL($city, $day){
        $url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=".$city."&cnt=".$day."&mode=json&units=metric&lang=fr";
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $res = curl_exec($ch);
        curl_close($ch);	
        return json_decode($res);	
	}
	
    function getWeather($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        return json_encode($data);
    }
    
    function getTemperature($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        return $w->{'temp'}->{'day'};
        
    }
    
    function getAllTemperature($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        return json_encode($w->{'temp'});
    }
    
    function getHumidity($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        return $w->{'humidity'};
    }
    
    function getPressure($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        return $w->{'pressure'}; //hPa
    }
    
    function getDescription($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        $weather = $w->{'weather'};
        return $weather[0]->{'description'};
    }
    
    function getWeatherCode($day = 1){
        if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
    	$data = $result->{'list'};
    	$w = $data[$day - 1];
    	$weather = $w->{'weather'};
    	return $weather[0]->{'icon'};
    }
    
    function getWindSpeed($day = 1){
    	if($day <= 7){
    		$result = $this->data;
    	}else{
    		$result = $this->fetchURL($this->city, $day);    		
    	}
        $data = $result->{'list'};
        $w = $data[$day - 1];
        return $w->{'speed'}; //mps
    }
    
    function getCachedWeather(){
    	$file = "weather.json";
    	$json = file_get_contents($file, TRUE);
    	$data = json_decode($json);
    	return $data;
    }
}

