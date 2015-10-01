<?php
require_once __DIR__ . '/../classes/connexion.php';
require_once __DIR__ . '/../classes/Zwave.php';
require_once __DIR__ . '/../classes/PushMessage.php';
require_once __DIR__ . '/../classes/Settings.php';

// set_time_limit ( 10 );
while ( ! getBDD () )
	;

$settings = new Settings ();
$push = new PushMessage ();
$zwave = new Zwave ();

$lastAlarm = $settings->getSettings ( 'Alarm', 'status' );
$arrMvt = loadSensors ();
$arrMvt2 = initTimestamp ( $arrMvt );

while ( true ) {
	$alarm = $settings->getSettings ( 'Alarm', 'status' );
	if ($alarm) {
		if (! $lastAlarm) {
			$arrMvt2 = initTimestamp ( $arrMvt );
			$lastAlarm = TRUE;
		}
		foreach ( $arrMvt as $i => $sensor ) {
			if ($sensor ['type'] == 'Door/Window') {
				switch ($sensor ['protocol']) {
					case 'zwave' :
						if ($zwave->getValue ( $sensor ['sensor'] ) == 'on' && $zwave->GetTimestamp ( $sensor ['sensor'] ) != $arrMvt2 [$i]) {
							$push->sendMessageAll ( "Atlantis - Alarme", "Porte ouverte !" );
							$arrMvt2 [$i] = $zwave->GetTimestamp ( $sensor ['sensor'] );
						}
						break;
				}
			}
		}
	} else {
		$lastAlarm = false;
	}
	sleep ( 2 );
}
function loadSensors() {
	$arrMvt = array ();
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT at_sensors.id, at_sensors.sensor, at_sensors.protocol, at_sensors.type, at_sensors_devices.room FROM at_sensors INNER JOIN at_sensors_devices ON `at_sensors`.`device` = `at_sensors_devices`.`device` WHERE `at_sensors`.`ignore` = 0 AND `at_sensors`.`type` = "Door/Window" OR `at_sensors`.`type` = "Temperature"' );
	while ( $data = $req->fetch () ) {
		array_push ( $arrMvt, $data );
	}
	$req->closeCursor ();
	return $arrMvt;
}
function initTimestamp($arr) {
	$zwave = new Zwave ();
	$arrMvt2 = array ();
	for($i = 0; $i < count ( $arr ); $i ++) {
		array_push ( $arrMvt2, $zwave->GetTimestamp ( $arr [$i] ['sensor'] ) );
	}
	return $arrMvt2;
}