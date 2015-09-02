'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SensorsSettingsCtrl
 * @description # SensorsSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('SensorsSettingsCtrl', function($scope, $mdDialog, $http, $sessionStorage, AtlantisUri) {
	$scope.addSensors = function(){
		var nURL = AtlantisUri.Sensors() + "?api=" + $sessionStorage.api;
		$http.post(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide({code: 202});
			}
		});
	};
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
});
