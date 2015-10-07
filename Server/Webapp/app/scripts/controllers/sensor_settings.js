'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SensorSettingsCtrl
 * @description # SensorSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('SensorSettingsCtrl', function($scope, $rootScope, $mdDialog, $filter, $sessionStorage, $http, $mdToast, AtlantisUri, sensor) {
	$scope.sensor = sensor;
	if(isScenario()){
		getScenarios();	
	}
	$scope.rooms = $rootScope.rooms;
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
	$scope.delete = function(){
		
	};
	$scope.addScenario = function(scenario){
		if(scenario != null){
			var nURL = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api;
			nURL += '&sensor=' + sensor.id + '&scenario=' + scenario.file;
			$http.post(nURL).success(function(data, status){
				if(status == 202){
					showToast($mdToast, 'Scénario enregistré avec succès !');
					getScenarios();
				}else{
					showToast($mdToast, 'Un problème technique est survenu !');
				}
			});
		}
	};
	$scope.removeScenario = function(sc){
		var nURL = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api + '&scenario=' + sc.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				var i = $scope.scs.indexOf(sc);
				$scope.scs.splice(i, 1);
			}else{
				showToast($mdToast, 'Un problème technique est survenu !');
			}
		});
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
	function getScenarios(){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.scenarios = data;				
			}
		});
		var nURL2 = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api + '&scenario=' + sensor.id;
		$http.get(nURL2).success(function(data, status){
			if(status == 202){
				$scope.scs = data;
			}
		});
	}
	function isScenario(){
		switch(sensor.type){
		case 'Door/Window':
			return true;
		default:
			return false;
		}
	}
});
