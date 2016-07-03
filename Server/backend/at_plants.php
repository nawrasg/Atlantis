<?php
header ( 'Access-Control-Allow-Origin: *' );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Sync.php';
require_once __DIR__ . '/classes/PushMessage.php';

$page_level = 2;
$admin_level = 0;

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'POST' :
			if ($_FILES) {
				upload ( $_REQUEST, $_FILES );
			} else {
				echo discover ();
			}
			break;
		case 'GET' :
			echo json_encode ( getValues () );
			http_response_code ( 202 );
			break;
		case 'PUT' :
			if (isset ( $_REQUEST ['id'] )) {
				if (isset ( $_REQUEST ['title'] )) {
					update ( $_REQUEST );
				}
			} else {
				echo getLiveValues ();
			}
			break;
		case 'DELETE' :
			if (checkAPI ( $_REQUEST ['api'], $admin_level )) {
				delete ( $_REQUEST );
			}
			break;
	}
} else if ($argc > 1) {
	switch ($argv [1]) {
		case 'update' :
			getLiveValues ();
			break;
		case 'battery' :
			checkBattery ();
			break;
	}
}
function delete($arr) {
	if (isset ( $arr ['id'] )) {
		$id = $arr ['id'];
		$bdd = getBDD ();
		$req = $bdd->exec ( "DELETE FROM at_plants WHERE id = $id" );
		if ($req > 0) {
			(new Sync())->update(Sync::PLANTS);
			http_response_code ( 202 );
		} else {
			http_response_code ( 403 );
		}
	} else {
		http_response_code ( 404 );
	}
}
function update($arr) {
	$bdd = getBDD ();
	$id = $arr ['id'];
	($arr ['title'] == '') ? $title = NULL : $title = $arr ['title'];
	isset ( $arr ['room'] ) ? $room = $arr ['room'] : $room = NULL;
	$request = $bdd->exec ( "UPDATE at_plants SET `title` = '$title', `room` = '$room' WHERE id = '$id'" );
	if ($request == 1) {
		(new Sync())->update(Sync::PLANTS);
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function upload($arr, $files) {
	$id = $arr ['id'];
	$path = $files ['file'] ['tmp_name'];
	$name = $files ['file'] ['name'];
	$ext = pathinfo ( $name, PATHINFO_EXTENSION );
	$result = move_uploaded_file ( $path, './home/plants/' . $id . '.' . $ext );
	echo json_encode ( $result );
	if ($result) {
		(new Sync())->update(Sync::PLANTS);
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function delete($arr) {
	if (isset ( $arr ['id'] )) {
	} else {
		http_response_code ( 404 );
	}
}
function discover() {
	exec ( 'sudo node /root/node_modules/flower-power/atlantis.js', $output );
	while ( count ( $output ) == 0 ) {
		exec ( 'sudo node /root/node_modules/flower-power/atlantis.js', $output );
	}
	if (count ( $output ) > 1) {
		for($i = 0; $i < count ( $output ); $i ++) {
			$line = explode ( '=', $output [$i] );
			if ($line [0] == 'id') {
				$line2 = explode ( '=', $output [$i + 2] );
				if ($line2 [0] == 'color') {
					insertPlant ( $line [1], $line2 [1] );
				}
			}
		}
		(new Sync())->update(Sync::PLANTS);
		return 200;
	}
	return 404;
}
function getValues() {
	$bdd = getBDD ();
	$request = $bdd->query ( 'SELECT at_plants.id, at_plants.sensor, at_plants.title, at_plants.picture, at_plants.color, at_plants.room, at_plants_history.battery, at_plants_history.light, at_plants_history.soil_conductivity, at_plants_history.soil_temperature, at_plants_history.air_temperature, at_plants_history.moisture, at_plants_history.date, at_plants_history.time FROM at_plants JOIN at_plants_history ON at_plants.id = at_plants_history.sensor ORDER BY at_plants_history.date DESC, at_plants_history.time DESC LIMIT 1' );
	$result = array ();
	while ( $data = $request->fetch () ) {
		$result [] = array (
				'id' => $data ['id'],
				'sensor' => $data ['sensor'],
				'title' => $data ['title'],
				'picture' => $data ['picture'],
				'color' => $data ['color'],
				'room' => $data ['room'],
				'battery' => $data ['battery'],
				'light' => $data ['light'],
				'conductivity' => $data ['soil_conductivity'],
				'stemperature' => $data ['soil_temperature'],
				'atemperature' => $data ['air_temperature'],
				'moisture' => $data ['moisture'],
				'date' => $data ['date'],
				'time' => $data ['time'] 
		);
	}
	$request->closeCursor ();
	return $result;
}
function insertPlant($id, $color) {
	$bdd = getBDD ();
	$request = $bdd->prepare ( 'INSERT INTO at_plants(`id`, `sensor`, `color`) VALUES("", :id, :color)' );
	$request->execute ( array (
			'id' => $id,
			'color' => $color 
	) );
	$request->closeCursor ();
	return 200;
}
function getLiveValues() {
	exec ( 'sudo node /root/node_modules/flower-power/atlantis.js', $output );
	while ( count ( $output ) == 0 ) {
		exec ( 'sudo node /root/node_modules/flower-power/atlantis.js', $output );
	}
	for($i = 0; $i < count ( $output ); $i ++) {
		$line = explode ( '=', $output [$i] );
		if ($line [0] == 'id') {
			$batteryArr = explode ( '=', $output [$i + 1] );
			$lightArr = explode ( '=', $output [$i + 3] );
			$conductivityArr = explode ( '=', $output [$i + 4] );
			$soilTemperatureArr = explode ( '=', $output [$i + 5] );
			$airTemperatureArr = explode ( '=', $output [$i + 6] );
			$moistureArr = explode ( '=', $output [$i + 7] );
			insertValues ( $line [1], $batteryArr [1], $lightArr [1], $conductivityArr [1], $soilTemperatureArr [1], $airTemperatureArr [1], $moistureArr [1] );
		}
	}
	return 200;
}
function insertValues($id, $battery, $light, $conductivity, $sTemperature, $aTemperature, $moisture) {
	$sensorId = getPlantId ( $id );
	$bdd = getBDD ();
	$request = $bdd->prepare ( 'INSERT INTO at_plants_history VALUES ("", :id, :battery, :light, :conductivity, :stemperature, :atemperature, :moisture, now(), now())' );
	$request->execute ( array (
			'id' => $sensorId,
			'battery' => $battery,
			'light' => $light,
			'conductivity' => $conductivity,
			'stemperature' => $sTemperature,
			'atemperature' => $aTemperature,
			'moisture' => $moisture 
	) );
	$request->closeCursor ();
	return 200;
}
function getPlantId($sensor) {
	$bdd = getBDD ();
	$request = $bdd->prepare ( 'SELECT id FROM at_plants WHERE sensor = :sensor' );
	$request->execute ( array (
			'sensor' => $sensor 
	) );
	$data = $request->fetch ();
	$request->closeCursor ();
	return $data ['id'];
}
function checkBattery() {
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT t1.* FROM at_plants_history t1 LEFT JOIN at_plants_history t2 ON (t1.sensor = t2.sensor AND t1.date < t2.date) WHERE t2.date IS NULL AND t1.battery < 15' );
	if ($req) {
		$push = new PushMessage ();
		$push->sendMessage ( 'Atlantis', 'Certains capteurs necessitent un changement de piles !' );
	}
}
