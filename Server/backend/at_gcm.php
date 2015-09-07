<?php

header("Access-Control-Allow-Origin: *");
require_once './connexion.php';
require_once './checkAPI.php';
require_once './at_history_cmd.php';

$page_level = 2;

if(isset($_GET['api']) && checkAPI($_GET['api'], $page_level)){
    if(isset($_GET['action'])){
        $bdd = getBDD();
        $req = $bdd->prepare('SELECT * FROM at_gcm WHERE mac = :mac');
        $req->execute(array('mac' => $_GET['api']));
        $resultat = $req->fetch();
        if($resultat){
            echo 409;
        }else{
            $req2 = $bdd->prepare('INSERT INTO at_gcm VALUES(:mac, :id)');
            $req2->execute(array('mac' => $_GET['api'], 'id' => $_GET['action']));
            echo 200;
        }
    }else{
        echo 404;
    }
}else{
    echo 401;
}
