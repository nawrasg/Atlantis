'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SensorSettingsCtrl
 * @description # SensorSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('SensorSettingsCtrl', function($scope, $rootScope, $mdDialog, $filter, $sessionStorage, $http, AtlantisUri, sensor) {
	$scope.sensor = sensor;
	$scope.rooms = $rootScope.rooms;
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
	$scope.delete = function(){
		
	};
	$scope.save = function(){
		var nURL = AtlantisUri.Sensors() + "?api=" + $sessionStorage.api;
		if(sensor.type === "section"){
			nURL += "&device=" + $scope.sensor.id + "&alias=" + $scope.sensor.alias + "&room=" + $scope.sensor.room;
		}else{
			nURL += "&sensor=" + $scope.sensor.id + "&history=" + $scope.sensor.history + "&ignore=" + $scope.sensor.ignore;
		}
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide($scope.sensor);
			}
		});
	};
	if(sensor.type != 'section'){
		$scope.lastupdate = $filter('date')(sensor.update * 1000, 'dd/MM/yyyy à hh:mm a');
	}
});
