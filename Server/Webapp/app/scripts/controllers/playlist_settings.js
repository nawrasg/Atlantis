'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:PlaylistSettingsCtrl
 * @description # PlaylistSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('PlaylistSettingsCtrl', function($scope, $http, $sessionStorage, $mdDialog, AtlantisUri, playlist) {
	$scope.playlist = playlist;
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
	getSongs();
	$scope.close = function(){
		$mdDialog.cancel();
	};
	$scope.delete = function(){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		nURL += '&playlist=' + playlist.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide();
			}
		});
	};
	$scope.del = function(song){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		nURL += '&action=playlistremove&playlist=' + playlist.id + '&song=' + song.id;
		$http.get(nURL).success(function(data, status){
			if(status == 200){
				var i = $scope.songs.indexOf(song);
				$scope.songs.splice(i, 1);
			}
		});
	};
	function getSongs(){
		var nURL = AtlantisUri.Music() + '?api=' + $sessionStorage.api;
		nURL += '&action=playlistsongs&playlist=' + playlist.id;
		$http.get(nURL).success(function(data, status){
			$scope.songs = data;
		});
	}
});
