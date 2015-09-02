'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:LightsSettingsCtrl
 * @description # LightsSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('LightsSettingsCtrl', function($scope, $mdDialog, $http, $sessionStorage, AtlantisUri) {
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
	$scope.addLights = function(){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		$http.post(nURL).success(function(data, status){
			$mdDialog.hide({code: 202});
		})
	};
});
