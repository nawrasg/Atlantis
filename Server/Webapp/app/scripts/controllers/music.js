'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:MusicCtrl
 * @description # MusicCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('MusicCtrl', function($scope, $http, $sessionStorage, $filter, AtlantisUri) {
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
		var nURL = '';
		switch(cmd){
		case 'stop':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=stop';
			break;
		case 'pause':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=pause';
			break;
		case 'repeat':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=repeat';
			break;
		case 'previous':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=previous';
			break;
		case 'next':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=next';
			break;
		case 'shuffle':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=shuffle';
			break;
		case 'refresh':
			nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api + '&action=search';
			break;
		}
		$http.get(nURL).success(function(data, status){
			
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
