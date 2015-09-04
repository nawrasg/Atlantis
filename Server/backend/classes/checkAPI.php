<?php
require_once __DIR__ . '/connexion.php';

/**
 * Niveau des comptes :
 * 0 : Administrateur (sans restrictions)
 * 1 : Utilisateur quotidien
 * 2 : Visiteur (uniquement lumieres)
 * 3 : Urgences
 * -1 : Bloque
 * 
 * @param type $val        	
 * @param type $level        	
 * @return boolean
 */
function checkAPI($val, $level) {
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'SELECT nom, type FROM at_users WHERE cle = :cle' );
	$req->execute ( array (
			'cle' => $val 
	) );
	$resultat = $req->fetch ();
	if ($resultat) {
		$req->closeCursor ();
		$user_level = $resultat ['type'];
	} else {
		$req = $bdd->prepare ( 'SELECT nom FROM at_devices WHERE mac = :mac' );
		$req->execute ( array (
				'mac' => strtoupper ( $val ) 
		) );
		$resultat = $req->fetch ();
		$req->closeCursor ();
		if (! $resultat) {
			return false;
		} else {
			$req2 = $bdd->prepare ( 'SELECT at_users.type FROM at_users INNER JOIN at_devices ON at_devices.username = at_users.id WHERE at_devices.mac = :mac' );
			$req2->execute ( array (
					'mac' => strtoupper ( $val ) 
			) );
			$resultat2 = $req2->fetch ();
			$req2->closeCursor ();
			if ($resultat2) {
				$user_level = $resultat2 ['type'];
			} else {
				return false;
			}
		}
	}
	if ($user_level == - 1) {
		return false;
	}
	if ($user_level <= $level) {
		return true;
	} else {
		return false;
	}
}