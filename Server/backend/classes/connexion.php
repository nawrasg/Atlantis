<?php


function getBDD() {
    try {
        $bdd = new PDO('mysql:host=ngHost; dbname=ngDB', 'ngUser', 'ngPassword');
        return $bdd;
    } catch (Exception $e) {
        return FALSE;
    }
    return FALSE;
}