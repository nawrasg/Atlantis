<?php
require_once __DIR__ . '/../classes/connexion.php';
require_once __DIR__ . '/../classes/Zwave.php';
require_once __DIR__ . '/../classes/PushMessage.php';
require_once __DIR__ . '/../classes/Settings.php';
require_once __DIR__ . '/../classes/Mode.php';

$pid = getmypid ();
(new Settings ())->setSettings ( 'Daemon', 'pid', $pid );

while ( ! getBDD () )
	;

$push = new PushMessage ();
$mode = new Mode ();
$alarm = new Alarm ();

$arrMvt = loadSensors ();
$arrMvt2 = initTimestamp ( $arrMvt );
$scenarios = loadScenarios ( $arrMvt );

while ( true ) {
	$settings = new Settings ();
	$zwave = new Zwave ();
	$alarm = $settings->getSettings ( 'Alarm', 'status' );
	foreach ( $arrMvt as $i => $sensor ) {
		if ($sensor ['type'] == 'Door/Window') {
			switch ($sensor ['protocol']) {
				case 'zwave' :
					if ($zwave->getValue ( $sensor ['sensor'] ) == 'on' && $zwave->GetTimestamp ( $sensor ['sensor'] ) != $arrMvt2 [$i]) {
						$arrMvt2 [$i] = $zwave->GetTimestamp ( $sensor ['sensor'] );
						execScenario ( $i, $scenarios );
						if ($mode->getMode () == Mode::NIGHT || $mode->getMode () == Mode::AWAY) {
							$alarm->execAlarm();
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
						if ($mode->getMode () == Mode::AWAY) {
							$alarm->execAlarm();
						}
					}
					break;
			}
		} else if ($sensor ['type'] == 'Tamper') {
			switch ($sensor ['protocol']) {
				case 'zwave' :
					if ($zwave->GetTimestamp ( $sensor ['sensor'] ) != $arrMvt2 [$i]) {
						$arrMvt2 [$i] = $zwave->GetTimestamp ( $sensor ['sensor'] );
						$txt = "Le capteur " . $sensor ['alias'] . " dans " . $sensor ['room_label'] . " a ete trafique !";
						$push->sendMessageAll ( "Atlantis - Alarme", $txt );
					}
			}
		}
		checkModeHours ();
	}
	sleep ( 0.5 );
	if ($settings->getSettings ( 'Daemon', 'stop' )) {
		$settings->setSettings ( 'Daemon', 'stop', false );
		$settings->setSettings ( 'Daemon', 'pid', - 1 );
		break;
	}
	if ($settings->getSettings ( 'Daemon', 'scenarios' )) {
		$settings->setSettings ( 'Daemon', 'scenarios', false );
		$scenarios = loadScenarios ( $arrMvt );
	}
}
exit ();
function loadSensors() {
	$arrMvt = array ();
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT at_sensors.id, at_sensors.sensor, at_sensors.protocol, at_sensors.type, at_sensors_devices.room, at_sensors_devices.alias, at_room.room AS room_label FROM at_sensors INNER JOIN at_sensors_devices ON `at_sensors`.`device` = `at_sensors_devices`.`device` INNER JOIN at_room ON at_sensors_devices.room = at_room.id WHERE `at_sensors`.`ignore` = 0 AND `at_sensors`.`type` = "Door/Window" OR `at_sensors`.`type` = "Temperature" OR `at_sensors`.`type` = "Motion" OR `at_sensors`.`type` = "Tamper"' );
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
		exec ( "nohup php $filename >/dev/null 2>&1 &" );
	}
}
function checkModeHours() {
	$settings = new Settings ();
	if ($settings->getSettings ( 'Mode', 'nightAuto' )) {
		$mode = new Mode ();
		$from = $settings->getSettings ( 'Mode', 'nightFrom' );
		$to = $settings->getSettings ( 'Mode', 'nightTo' );
		if (time () > strtotime ( $from ) && $mode->getMode () == Mode::DAY) {
			$mode->setMode ( Mode::NIGHT );
		} else if (time () < strtotime ( $from ) && time () > strtotime ( $to ) && $mode->getMode () == Mode::NIGHT) {
			$mode->setMode ( Mode::DAY );
		}
	}
}