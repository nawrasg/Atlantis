'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ModeCtrl
 * @description # ModeCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ModeCtrl', function($scope, $http, $sessionStorage, $mdToast, AtlantisUri, AtlantisRoot) {
	$scope.atlantis = AtlantisRoot;
	$scope.setMode = function(mode){
		var nURL = AtlantisUri.Home() + '?api=' + $sessionStorage.api + '&mode=' + mode;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				$scope.atlantis.mode = mode;
			}else{
				showToast($mdToast, 'Impossible de changer de mode (erreur ' + status + ') !')
			}
			
		});
	}
});
