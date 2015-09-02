'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SongSettingsCtrl
 * @description # SongSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('SongSettingsCtrl', function($scope, $http, $sessionStorage, $mdDialog, AtlantisUri, song, playlists) {
	$scope.playlists = playlists;
	$scope.createPlaylist = function(){
		var nURL = AtlantisUri.Music() + "?api=" + $sessionStorage.api + "&playlist=" + $scope.newPlaylist; 
		$http.post(nURL).success(function(data, status){
			if(status == 202){
				$scope.playlists.push(data);
				$scope.newPlaylist = null;
			}
		});
	};
	$scope.addSong = function(){
		if($scope.song.playlist != null){
			var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
			nURL += "&playlist=" + $scope.song.playlist + '&song=' + song.id + '&action=playlistadd';
			$http.get(nURL).success(function(data, status){
				console.log(data, status);
			});
		}
	};
	$scope.cancel = function(){
		$mdDialog.cancel();
		//TODO : Refresh Music List
	};
});
