<?php
require_once __DIR__ . '/../classes/connexion.php';
require_once __DIR__ . '/../classes/Zwave.php';
require_once __DIR__ . '/../classes/PushMessage.php';
require_once __DIR__ . '/../classes/Settings.php';

$pid = getmypid ();
(new Settings ())->setSettings ( 'Daemon', 'pid', $pid );

while ( ! getBDD () )
	;

$push = new PushMessage ();
$zwave = new Zwave ();

$arrMvt = loadSensors ();
$arrMvt2 = initTimestamp ( $arrMvt );
$scenarios = loadScenarios ( $arrMvt );

while ( true ) {
	$alarm = (new Settings ())->getSettings ( 'Alarm', 'status' );
	foreach ( $arrMvt as $i => $sensor ) {
		if ($sensor ['type'] == 'Door/Window') {
			switch ($sensor ['protocol']) {
				case 'zwave' :
					if ($zwave->getValue ( $sensor ['sensor'] ) == 'on' && $zwave->GetTimestamp ( $sensor ['sensor'] ) != $arrMvt2 [$i]) {
						$arrMvt2 [$i] = $zwave->GetTimestamp ( $sensor ['sensor'] );
						execScenario ( $i, $scenarios );
						if ($alarm) {
							$push->sendMessageAll ( "Atlantis - Alarme", "Porte ouverte !" );
						}
					}
					break;
			}
		} else if ($sensor ['type'] == 'Motion') {
			switch ($sensor ['protocol']) {
				case 'zwave' :
					if ($zwave->GetTimestamp ( $sensor ['sensor'] ) != $arrMvt2 [$i]) {
						$arrMvt2 [$i] = $zwave->GetTimestamp ( $sensor ['sensor'] );
						execScenario ( $i, $scenarios );
						if ($alarm) {
							$push->sendMessageAll ( "Atlantis - Alarme", "Mouvement !" );
						}
					}
					break;
			}
		}
	}
	sleep ( 2 );
}
function loadSensors() {
	$arrMvt = array ();
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT at_sensors.id, at_sensors.sensor, at_sensors.protocol, at_sensors.type, at_sensors_devices.room FROM at_sensors INNER JOIN at_sensors_devices ON `at_sensors`.`device` = `at_sensors_devices`.`device` WHERE `at_sensors`.`ignore` = 0 AND `at_sensors`.`type` = "Door/Window" OR `at_sensors`.`type` = "Temperature" OR `at_sensors`.`type` = "Motion"' );
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
function loadScenarios($arr) {
	$bdd = getBDD ();
	$output = array ();
	foreach ( $arr as $sensor ) {
		$result = array ();
		if ($sensor ['type'] == 'Door/Window' || $sensor ['type'] == 'Motion') {
			$sensor = $sensor ['id'];
			$req = $bdd->query ( "SELECT * FROM at_sensors_scenarios WHERE sensor = $sensor" );
			while ( $data = $req->fetch () ) {
				array_push ( $result, $data );
			}
		}
		array_push ( $output, $result );
	}
	return $output;
}
function execScenario($i, $scenarios) {
	foreach ( $scenarios [$i] as $scenario ) {
		$name = $scenario ['scenario'];
		$filename = __DIR__ . "/../scenarios/$name.php";
		exec ( "nohup php $filename &" );
	}
}