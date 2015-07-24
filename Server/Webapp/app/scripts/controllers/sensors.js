'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SensorsCtrl
 * @description # SensorsCtrl Controller of the atlantisWebAppApp
 */
var nApp = angular.module('atlantisWebAppApp');

nApp.controller('SensorsCtrl', function($scope, $http, $sessionStorage, $filter, $sce, ModalService, $rootScope, AtlantisUri) {
	getLights($scope, $http);
	getSensors($scope, $http);
	var lightOptions = {from: 0, to: 10, step: 1};
	$scope.options = lightOptions;
	
	$scope.reachable = function(reachable){
		if(reachable){
			return 'images/ng_empty.png';
		}else{
			return 'images/ng_ball_red.png';
		}
	};
	$scope.changeBright = function(light){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&bri=' + light.uid + '&protocol=' + light.protocol + '&value=' + light.brightness;
		$http.put(nURL).success(function(data, status){
			//TODO
		});
	};
	$scope.toggleLight = function(light){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&on=' + light.uid + "&protocol=" + light.protocol + "&value=" + light.on;
		$http.put(nURL).success(function(data, status, header, config){
			//TODO
		});
	};
	$scope.modifyLight = function(light){
		ModalService.showModal({
			templateUrl: 'views/light_settings.html',
			controller: 'LightSettingsCtrl',
			inputs: {
				light: light
			}
		}).then(function(modal){
			modal.element.modal({ 
			    backdrop: 'static', 
			    keyboard: false 
			});
			 modal.close.then(function(result) {
				 
		      });
		});
	};

	// Sensors
	$scope.sensorPic = function(type){
		switch(type){
		case 'section':
			return 'images/ng_empty.png';
		case 'Battery':
			return 'images/ng_battery.png';
		case 'Tamper':
			return 'images/ng_alert.png';
		case 'Temperature':
			return 'images/ng_thermostat.png';
		case 'Luminiscence':
			return 'images/ng_sun.png';
		default:
			return 'images/ng_device.png';	
		}
	};
	$scope.sensorDesc = function(sensor){
		switch(sensor.type){
		case 'section':
			return $sce.trustAsHtml('<md-divider style="margin-bottom:10px"></md-divider><b>' + $filter('uppercase')(sensor.alias) + ((sensor.room == null || sensor.room == '') ? '' : ' (' + getRoom($scope, sensor.room) + ')') + '</b>');
		case 'Battery':
			return 'Autonomie : ' + sensor.value + ' %';
		case 'Tamper':
			return sensor.value;
		case 'Temperature':
			return 'Température : ' + sensor.value + ' ' + sensor.unit;
		case 'Luminiscence':
			return 'Luminosité : ' + sensor.value + ' ' + sensor.unit;
		default:
			return sensor.value + ' ' + sensor.unit;	
		}
	};
	function getLights($scope, $http){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			$scope.lights = data.lights;
		});
	}
	function getSensors($scope, $http){
		var nURL = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api + '&action=get';
		$http.get(nURL).success(function(data, status){
			$scope.sensors = data.devices;
			$rootScope.rooms = data.rooms;
		});
	}
	function getRoom($scope, room){
		var room = $filter('filter')($scope.rooms, {'id':room});
		if(room.length == 1){
			return room[0].room;
		}else{
			return '';
		}
	}
});
