<?php
header ( "Access-Control-Allow-Origin: *" );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Music.php';
require_once __DIR__ . '/classes/Settings.php';
require_once __DIR__ . '/getid3/getid3.php';

$page_level = 1;

if ($argc > 1) {
	switch ($argv [1]) {
		case 'search' :
			$settings = new Settings ();
			$musicPath = $settings->getSettings ( 'Files', 'music' );
			findMusic ( $musicPath );
			break;
		case 'sync' :
			folderToPlaylist ();
			break;
	}
}

if (isset ( $_REQUEST ['api'] ) && checkAPI ( $_REQUEST ['api'], $page_level )) {
	switch ($_SERVER ['REQUEST_METHOD']) {
		case 'POST' :
			if (isset ( $_REQUEST ['playlist'] )) {
				$playlist = createPlaylist ( $_REQUEST );
				echo json_encode ( $playlist );
			} else {
				$settings = new Settings ();
				$musicPath = $settings->getSettings ( 'Files', 'music' );
				findMusic ( $musicPath );
				http_response_code ( 202 );
			}
			break;
		case 'GET' :
			echo json_encode ( get ( $_REQUEST ) );
			break;
		case 'PUT' :
			update ( $_REQUEST );
			break;
		case 'DELETE' :
			delete ( $_REQUEST );
			break;
	}
}
function delete($arr) {
	if (isset ( $arr ['playlist'] )) {
		deletePlaylist ( $arr ['playlist'] );
	} else if (isset ( $arr ['welcome'] )) {
		deleteWelcomeMusic ( $arr ['api'] );
	} else {
		http_response_code ( 404 );
	}
}
function update($arr) {
	if (isset ( $arr ['action'] )) {
		$bdd = getBDD ();
		$music = new Music ();
		switch ($arr ['action']) {
			case 'on' :
				$music->on ();
				break;
			case 'off' :
				$music->off ();
				break;
			case 'start' :
				$music->start ();
				break;
			case 'pause' :
				$music->pause ();
				break;
			case 'stop' :
				$music->stop ();
				break;
			case 'next' :
				$music->next ();
				break;
			case 'previous' :
				$music->previous ();
				break;
			case 'repeat' :
				$music->repeat ();
				break;
			case 'shuffle' :
				$music->shuffle ();
				break;
			case 'play' :
				if (isset ( $arr ['link'] )) {
					$link = $arr ['link'];
					$music->play ( $link );
					echo 200;
				} else if (isset ( $arr ['id'] )) {
					$id = $arr ['id'];
					$req = $bdd->prepare ( 'SELECT file FROM at_music WHERE id = :id' );
					$req->execute ( array (
							'id' => $id 
					) );
					$data = $req->fetch ();
					$music->start ();
					$path = str_replace ( " ", "\ ", $data ['file'] );
					$music->play ( $path );
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'vol' :
				if (isset ( $arr ['level'], $arr ['source'] )) {
					if ($arr ['source'] == 0) {
						$source = FALSE;
					} else {
						$source = TRUE;
					}
					$vol = $arr ['level'];
					$vol *= 10;
					$music->volume ( $vol, $source );
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'state' :
				if ($music->isPlay ()) {
					$result = array (
							'state' => 1,
							'vol' => $music->getVol () 
					);
				} else {
					$result = array (
							'state' => 0,
							'vol' => $music->getVol () 
					);
				}
				echo json_encode ( $result );
				break;
			case 'playlistadd' :
				if (isset ( $arr ['playlist'], $arr ['song'] )) {
					$playlist = $arr ['playlist'];
					$song = $arr ['song'];
					$req = $bdd->prepare ( 'INSERT INTO at_songsplaylists VALUES("", :playlist, :song)' );
					$req->execute ( array (
							'playlist' => $playlist,
							'song' => $song 
					) );
					$req->closeCursor ();
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'playlistremove' :
				if (isset ( $arr ['playlist'], $arr ['song'] )) {
					$playlist = $arr ['playlist'];
					$song = $arr ['song'];
					$req = $bdd->prepare ( 'DELETE FROM at_songsplaylists WHERE playlist = :playlist AND song = :song' );
					$req->execute ( array (
							'playlist' => $playlist,
							'song' => $song 
					) );
					$req->closeCursor ();
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'playlistplay' :
				if (isset ( $arr ['playlist'] )) {
					$playlist = $arr ['playlist'];
					$music->clearPlaylist ();
					$req = $bdd->prepare ( 'SELECT at_music.file FROM at_music JOIN at_songsplaylists ON at_music.id = at_songsplaylists.song WHERE at_songsplaylists.playlist = :playlist' );
					$req->execute ( array (
							'playlist' => $playlist 
					) );
					while ( $data = $req->fetch () ) {
						$music->add ( $data ['file'] );
					}
					$req->closeCursor ();
					$music->start ();
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'playlistsongs' :
				if (isset ( $arr ['playlist'] )) {
					$playlist = $arr ['playlist'];
					$result = array ();
					$req = $bdd->prepare ( 'SELECT at_music.file, at_music.length, at_music.id FROM at_music JOIN at_songsplaylists ON at_music.id = at_songsplaylists.song WHERE at_songsplaylists.playlist = :playlist' );
					$req->execute ( array (
							'playlist' => $playlist 
					) );
					while ( $data = $req->fetch () ) {
						$title = basename ( $data ['file'] );
						$result [] = array (
								'id' => $data ['id'],
								'title' => $title,
								'length' => $data ['length'] 
						);
					}
					$req->closeCursor ();
					echo json_encode ( $result );
				} else {
					echo 404;
				}
				break;
		}
		http_response_code ( 202 );
	} else if (isset ( $arr ['welcome'] )) {
		setWelcomeMusic ( $arr ['api'], $arr ['welcome'] );
	} else {
		http_response_code ( 404 );
	}
}
function get($arr) {
	$music = new Music ();
	if (isset ( $arr ['songs'] )) {
		return getSongs ();
	} else {
		$result = array (
				'on' => ($music->isOn () ? 1 : 0),
				'play' => ($music->isPlay () ? 1 : 0),
				'welcome' => isWelcomeMusic ( $arr ['api'] ),
				'vol' => $music->getVol ( true ),
				'headphone' => $music->getVol ( TRUE ),
				'songs' => getSongs () 
		);
		http_response_code ( 202 );
		return $result;
	}
}
function deletePlaylist($id) {
	$bdd = getBDD ();
	$req = $bdd->exec ( "DELETE FROM at_playlists WHERE id = '$id'" );
	if ($req == 1) {
		http_response_code ( 202 );
	} else {
		http_response_code ( 400 );
	}
}
function createPlaylist($arr) {
	$bdd = getBDD ();
	$title = $arr ['playlist'];
	$req = $bdd->exec ( "INSERT INTO at_playlists VALUES('', '$title')" );
	if ($req == 1) {
		$id = $bdd->lastInsertId ();
		$playlist = array (
				'id' => $id,
				'title' => $title,
				'type' => 'playlist' 
		);
		http_response_code ( 202 );
		return $playlist;
	} else {
		http_response_code ( 400 );
	}
}
function getFolderName($path) {
	$data = explode ( '/', $path );
	$i = count ( $data ) - 2;
	return $data [$i];
}
function getPlaylistId($playlist) {
	$bdd = getBDD ();
	$request = $bdd->query ( "SELECT * FROM at_playlists WHERE title = '$playlist'" );
	$data = $request->fetch ();
	return $data ['id'];
}
function folderToPlaylist() {
	$bdd = getBDD ();
	$request = $bdd->query ( 'SELECT * FROM at_music' );
	while ( $data = $request->fetch () ) {
		$folder = getFolderName ( $data ['file'] );
		if ($folder != 'Music') {
			$bdd->exec ( "INSERT INTO at_playlists VALUES('', '$folder')" );
			$songId = $data ['id'];
			$playlistId = getPlaylistId ( $folder );
			$bdd->exec ( "INSERT INTO at_songsplaylists VALUES('', '$playlistId', '$songId')" );
		}
	}
	$request->closeCursor ();
	return 200;
}
function findMusic($path) {
	checkSavedMusic ();
	exec ( "find " . $path . " -name '*.mp3'", $output );
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'INSERT INTO at_music VALUES("", :file, :length, :album, :title)' );
	for($i = 0; $i < count ( $output ); $i ++) {
		$info = getTags ( $output [$i] );
		$req->execute ( array (
				'file' => $output [$i],
				'title' => $info ['title'],
				'album' => $info ['album'],
				'length' => $info ['length'] 
		) );
	}
	$req->closeCursor ();
	folderToPlaylist ();
}
function deleteMusicDB($id) {
	$bdd = getBDD ();
	$request = $bdd->exec ( "DELETE FROM at_music WHERE id = '$id'" );
	$request = $bdd->exec ( "DELETE FROM at_songsplaylists WHERE song = '$id'" );
	return 200;
}
function checkSavedMusic() {
	$bdd = getBDD ();
	$request = $bdd->query ( 'SELECT * FROM at_music' );
	while ( $data = $request->fetch () ) {
		if (! file_exists ( $data ['file'] )) {
			deleteMusicDB ( $data ['id'] );
		}
	}
	$request->closeCursor ();
	return 200;
}
function getTags($path) {
	$getID3 = new getID3 ();
	$info = $getID3->analyze ( $path );
	$arr = array (
			'title' => $info ['tags'] ['id3v2'] ['title'] [0],
			'album' => $info ['tags'] ['id3v2'] ['album'] [0],
			'length' => $info ['playtime_string'] 
	);
	return $arr;
}
function deleteWelcomeMusic($api) {
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'DELETE FROM at_welcome WHERE mac = :mac' );
	$req->execute ( array (
			'mac' => $api 
	) );
	$req->closeCursor ();
	http_response_code ( 202 );
}
function setWelcomeMusic($api, $music) {
	$bdd = getBDD ();
	$result = isWelcomeMusic ( $api );
	if ($result ['id'] == - 1) {
		$req = $bdd->prepare ( 'INSERT INTO at_welcome VALUES("", :mac, :music, now(), now())' );
	} else {
		$req = $bdd->prepare ( 'UPDATE at_welcome SET music = :music WHERE mac = :mac' );
	}
	$req->execute ( array (
			'mac' => $api,
			'music' => $music 
	) );
	$req->closeCursor ();
	http_response_code ( 202 );
}
function isWelcomeMusic($api) {
	$bdd = getBDD ();
	$req = $bdd->prepare ( 'SELECT id, music FROM at_welcome WHERE mac = :api' );
	$req->execute ( array (
			'api' => $api 
	) );
	$data = $req->fetch ();
	if (! $data) {
		return array (
				'id' => - 1,
				'music' => - 1 
		);
	} else {
		return array (
				'id' => $data ['id'],
				'music' => $data ['music'] 
		);
	}
}
function getSongs() {
	$arr = array ();
	$bdd = getBDD ();
	$req = $bdd->query ( 'SELECT * FROM at_playlists' );
	while ( $data = $req->fetch () ) {
		$arr [] = array (
				'type' => 'playlist',
				'title' => $data ['title'],
				'id' => $data ['id'] 
		);
	}
	$req->closeCursor ();
	$req2 = $bdd->query ( 'SELECT * FROM at_music' );
	while ( $data2 = $req2->fetch () ) {
		$title = basename ( $data2 ['file'] );
		$arr [] = array (
				'type' => 'song',
				'title' => $title,
				'path' => $data2 ['file'],
				'length' => $data2 ['length'],
				'album' => $data2 ['album'],
				'id' => $data2 ['id'] 
		);
	}
	$req2->closeCursor ();
	return $arr;
}