<?php

header("Access-Control-Allow-Origin: *");

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Player.php';

$page_level = 1;
$player = new Player();

if (isset($_GET['action'], $_GET['api']) && checkAPI($_GET['api'], $page_level)) {
    switch($_REQUEST['action']){
        case 'speaker':
            if(isset($_REQUEST['text'])){
                $text = $_REQUEST['text'];
                $player->speak($text);
                echo 200;
            }else{
                echo 404;
            }
            break;
    }
} else if ($argc > 1) {
    
} else {
    echo 401;
}