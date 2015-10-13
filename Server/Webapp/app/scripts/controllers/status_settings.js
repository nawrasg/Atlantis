'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:StatusSettingsCtrl
 * @description # StatusSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('StatusSettingsCtrl', function($scope, $http, $sessionStorage, $mdToast, AtlantisUri) {
	get();
	$scope.startDaemon = function(){
		var nURL = AtlantisUri.System() + '?api=' + $sessionStorage.api + '&daemon=start';
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				showToast($mdToast, 'Démarrage en cours...');
			}else{
				showToast($mdToast, 'Un problème technique est survenu (' + status + ')');
			}
		})
	};
	$scope.stopDaemon = function(){
		var nURL = AtlantisUri.System() + '?api=' + $sessionStorage.api + '&daemon=stop';
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				showToast($mdToast, 'Arrêt en cours...');
			}else{
				showToast($mdToast, 'Un problème technique est survenu (' + status + ')');
			}
		})
	};
	function get(){
		var nURL = AtlantisUri.System() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.free_hdd = Math.round(data.free_hdd * 100);
				if(data.daemon == true){
					$scope.daemon = 'En cours...';
				}else{
					$scope.daemon = 'A l\'arrêt.';
				}
			}
		});
	}
});
