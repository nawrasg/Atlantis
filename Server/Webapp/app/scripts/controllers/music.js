'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:MusicCtrl
 * @description # MusicCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('MusicCtrl', function($scope, $http, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
	get();
	
	$scope.toggleMusic = function(){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=';
		if($scope.on){
			nURL += 'on';
		}else{
			nURL += 'off';
		}
		$http.get(nURL);
	};
	$scope.action = function(cmd){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		switch(cmd){
		case 'stop':
			nURL += '&action=stop';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'pause':
			nURL += '&action=pause';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'repeat':
			nURL += '&action=repeat';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'previous':
			nURL += '&action=previous';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'next':
			nURL += '&action=next';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'shuffle':
			nURL += '&action=shuffle';
			$http.get(nURL).success(function(data, status){
				
			});
			break;
		case 'refresh':
			$http.post(nURL).success(function(data, status){
				console.log(status, data);
			});
			break;
		}
	};
	$scope.editPlaylist = function(playlist, e){
		$mdDialog.show({
			templateUrl : 'views/playlist_settings.html',
			targetEvent : e,
			controller : 'PlaylistSettingsCtrl',
			locals : {
				playlist : playlist
			},
		}).then(function(nPlaylist) {
			get();
		});
	};
	$scope.play = function(song){
		if(song.type == 'song'){
			var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=play&id=' + song.id;
			$http.get(nURL).success(function(data, status){
				
			});
		}else{
			var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=playlistplay&playlist=' + song.id;
			$http.get(nURL).success(function(data, status){
				
			});
		}
	};
	$scope.speak = function(){
		var nURL = AtlantisUri.Speech() + '?api=' + $sessionStorage.api;
		nURL += '&action=speaker&text=' + encodeURI($scope.message);
		$http.get(nURL).success(function(data, status){
			console.log(status + ' : ' + data);
		});
	};
	$scope.changeVol = function(volume){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		nURL += '&action=vol&source=1&level=' + volume;
		$http.get(nURL).success(function(data, status){
			console.log(data);
		});
	};
	$scope.songPic = function(song){
		switch(song.type){
		case 'playlist':
			return 'images/music/ng_note_double.png';
		case 'song':
			if(song.id == $scope.welcomeSong){
				return 'images/music/ng_note_white.png';
			}else{
				return 'images/music/ng_note.png';
			}
		}
	};
	$scope.editSong = function(song, e){
		$mdDialog.show({
			templateUrl : 'views/song_settings.html',
			targetEvent : e,
			controller : 'SongSettingsCtrl',
			locals : {
				song : song,
				playlists : $scope.playlists
			},
		}).then(function(){
			get();
		});
	};
	function get(){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		nURL += '&action=init';
		$http.get(nURL).success(function(data, status){
			if(status == 200){
				$scope.on = (data.on == 1);
				$scope.welcome = (data.welcome.id != -1);
				$scope.playlists = $filter('filter')(data.songs, {type:'playlist'});
				$scope.songs = $filter('filter')(data.songs, {type:'song'});
				$scope.volume = data.vol;
				$scope.welcomeSong = data.welcome.id;
			}
		});
	}
});
