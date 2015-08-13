<?php
header ( "Access-Control-Allow-Origin: *" );
header ( 'Access-Control-Allow-Headers: origin, x-requested-with, content-type, accept' );
header ( 'Access-Control-Allow-Methods: GET, POST, PUT, DELETE' );

require_once __DIR__ . '/classes/connexion.php';
require_once __DIR__ . '/classes/checkAPI.php';
require_once __DIR__ . '/classes/Music.php';
require_once __DIR__ . '/getid3/getid3.php';

$page_level = 1;

if (isset ( $_GET ['api'] ) && checkAPI ( $_GET ['api'], $page_level )) {
	$music = new Music ();
	$bdd = getBDD ();
	if (isset ( $_GET ['action'] )) {
		switch ($_GET ['action']) {
			case 'welcome' :
				if (isset ( $_REQUEST ['music'] )) {
					echo setWelcomeMusic ( $_REQUEST ['api'], $_REQUEST ['music'] );
				} else {
					echo deleteWelcomeMusic ( $_REQUEST ['api'] );
				}
				break;
			case 'on' :
				$music->on ();
				echo 200;
				break;
			case 'off' :
				$music->off ();
				echo 200;
				break;
			case 'start' :
				$music->start ();
				echo 200;
				break;
			case 'pause' :
				$music->pause ();
				echo 200;
				break;
			case 'stop' :
				$music->stop ();
				echo 200;
				break;
			case 'next' :
				$music->next ();
				echo 200;
				break;
			case 'previous' :
				$music->previous ();
				echo 200;
				break;
			case 'repeat' :
				$music->repeat ();
				echo 200;
				break;
			case 'shuffle' :
				$music->shuffle ();
				echo 200;
				break;
			case 'play' :
				if (isset ( $_GET ['link'] )) {
					$link = $_GET ['link'];
					$music->play ( $link );
					echo 200;
				} else if (isset ( $_GET ['id'] )) {
					$id = $_GET ['id'];
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
				if (isset ( $_GET ['level'], $_REQUEST['source'] )) {
					if($_REQUEST['source'] == 0){
						$source = FALSE;
					}else{
						$source = TRUE;
					}
					$vol = $_GET ['level'];
					$vol *= 10;
					$music->volume ( $vol, $source );
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'state' :
				if ($music->isPlay ()) {
					$arr = array (
							'state' => 1,
							'vol' => $music->getVol () 
					);
				} else {
					$arr = array (
							'state' => 0,
							'vol' => $music->getVol () 
					);
				}
				echo json_encode ( $arr );
				break;
			case 'init' :
				$arr = array (
						'on' => ($music->isOn () ? 1 : 0),
						'play' => ($music->isPlay () ? 1 : 0),
						'welcome' => isWelcomeMusic ( $_REQUEST ['api'] ),
						'vol' => $music->getVol (true),
						'headphone' => $music->getVol(TRUE),
						'songs' => getSongs () 
				);
				echo json_encode ( $arr );
				break;
			case 'songs' :
				$arr = getSongs ();
				echo json_encode ( $arr );
				break;
			case 'playlistcreate' :
				if (isset ( $_GET ['playlist'] )) {
					$title = $_GET ['playlist'];
					$req = $bdd->prepare ( 'INSERT INTO at_playlists VALUES("", :title)' );
					$req->execute ( array (
							'title' => $title 
					) );
					$req->closeCursor ();
					echo 200;
				} else {
					echo 404;
				}
				break;
			case 'playlistadd' :
				if (isset ( $_GET ['playlist'], $_GET ['song'] )) {
					$playlist = $_GET ['playlist'];
					$song = $_GET ['song'];
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
				if (isset ( $_GET ['playlist'], $_GET ['song'] )) {
					$playlist = $_GET ['playlist'];
					$song = $_GET ['song'];
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
				if (isset ( $_GET ['playlist'] )) {
					$playlist = $_GET ['playlist'];
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
				if (isset ( $_GET ['playlist'] )) {
					$playlist = $_GET ['playlist'];
					$arr = array ();
					$req = $bdd->prepare ( 'SELECT at_music.file, at_music.length, at_music.id FROM at_music JOIN at_songsplaylists ON at_music.id = at_songsplaylists.song WHERE at_songsplaylists.playlist = :playlist' );
					$req->execute ( array (
							'playlist' => $playlist 
					) );
					while ( $data = $req->fetch () ) {
						$title = basename ( $data ['file'] );
						$arr [] = array (
								'id' => $data ['id'],
								'title' => $title,
								'length' => $data ['length'] 
						);
					}
					$req->closeCursor ();
					echo json_encode ( $arr );
				} else {
					echo 404;
				}
				break;
			case 'search' :
				findMusic ( '/Files/Music' );
				break;
		}
	} else {
		echo 404;
	}
} else if ($argc > 1) {
	switch ($argv [1]) {
		case 'search' :
			findMusic ( '/media/HDD/Music' );
			break;
		case 'sync' :
			folderToPlaylist ();
			break;
	}
} else {
	echo 401;
}

function getFolderName($path) {
	$data = explode ( '/', $path );
	$i = count ( $data ) - 2;
	return $data [$i];
}

function getPlaylistId($playlist){
	$bdd = getBDD();
	$request = $bdd->query("SELECT * FROM at_playlists WHERE title = '$playlist'");
	$data = $request->fetch();
	return $data['id'];
}

function folderToPlaylist() {
	$bdd = getBDD ();
	$request = $bdd->query ( 'SELECT * FROM at_music' );
	while ( $data = $request->fetch () ) {
		$folder = getFolderName ( $data ['file'] );
		if ($folder != 'Music') {
			$bdd->exec ( "INSERT INTO at_playlists VALUES('', '$folder')" );
			$songId = $data['id'];
			$playlistId = getPlaylistId($folder);
			$bdd->exec("INSERT INTO at_songsplaylists VALUES('', '$playlistId', '$songId')");
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
	return 200;
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
	return 200;
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