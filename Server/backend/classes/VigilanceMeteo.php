<?php

/**
 * Based on: http://vigilance.meteofrance.com/data/NXFR34_LFPW_.xml
 *
 * @author Nawras GEORGI
 */
class VigilanceMeteo {

    var $url = "http://vigilance.meteofrance.com/data/NXFR34_LFPW_.xml";
    var $data;

    public function __construct() {
        $file = file_get_contents($this->url);
        $xml = simplexml_load_string($file);
        $json = json_encode($xml);
        $this->data = json_decode($json);
    }

    public function getDate() {
        return $this->data->entetevigilance->{'@attributes'}->dateinsert;
    }

    public function getColor($departement) {
        $data = $this->data->datavigilance;
        for ($i = 0; $i < count($data); $i++) {
            $dep = $data[$i]->{'@attributes'}->dep;
            if($dep == $departement){
                $color = $data[$i]->{'@attributes'}->couleur;
                switch($color){
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
        }
        return FALSE;
    }

}
