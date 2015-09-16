'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SensorsCtrl
 * @description # SensorsCtrl Controller of the atlantisWebAppApp
 */
var nApp = angular.module('atlantisWebAppApp');

nApp.controller('SensorsCtrl', function($scope, $http, $sessionStorage,
		$filter, $mdDialog, $rootScope, AtlantisUri) {
	getLights($scope, $http);
	getSensors($scope, $http);
	var lightOptions = {
		from : 0,
		to : 10,
		step : 1
	};
	$scope.options = lightOptions;

	$scope.reachable = function(reachable) {
		if (reachable) {
			return 'images/ng_empty.png';
		} else {
			return 'images/ng_ball_red.png';
		}
	};
	$scope.changeBright = function(light) {
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&bri=' + light.uid + '&protocol=' + light.protocol + '&value='
				+ light.brightness;
		$http.put(nURL).success(function(data, status) {
			// TODO
		});
	};
	$scope.toggleLight = function(light) {
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&on=' + light.uid + "&protocol=" + light.protocol + "&value="
				+ light.on;
		$http.put(nURL).success(function(data, status, header, config) {
			// TODO
		});
	};
	$scope.getRoomLabel = function(light) {
		if (light.room != null && light.room != '') {
			var nRoom = $filter('filter')($rootScope.rooms, {
				id : light.room
			});
			if (nRoom != null && nRoom.length == 1) {
				return '(' + nRoom[0].room + ')';
			}
		}
	};
	$scope.editLight = function(light, e) {
		$mdDialog.show({
			templateUrl : 'views/light_settings.html',
			targetEvent : e,
			controller : 'LightSettingsCtrl',
			locals : {
				light : light
			},
		}).then(function(nLight) {
			if (nLight != null) {
				if (nLight.result == null) {
					light = nLight;
				} else {
					if (nLight.result === 'delete') {
						var i = $scope.lights.indexOf(light);
						$scope.lights.splice(i, 1);
					}
				}
			}
		});
	};
	$scope.editLights = function(e) {
		$mdDialog.show({
			templateUrl : 'views/lights_settings.html',
			targetEvent : e,
			controller : 'LightsSettingsCtrl'
		}).then(function(result) {
			if (result != null && result.code == 202) {
				getLights($scope, $http);
			}
		});
	};

	// Sensors
	$scope.sensorPic = function(type) {
		switch (type) {
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
		case 'Power':
		case 'Electric':
			return 'images/ng_lightning.png';
		case 'switchBinary':
			return 'images/ng_switch.png';
		default:
			return 'images/ng_device.png';
		}
	};
	$scope.sensorDesc = function(sensor) {
		switch (sensor.type) {
		case 'section':
			return $filter('uppercase')(
					(sensor.alias == null ? sensor.device : sensor.alias))
					+ ((sensor.room == null || sensor.room == '') ? '' : ' ('
							+ getRoom($scope, sensor.room) + ')');
		case 'Battery':
			return 'Autonomie : ' + sensor.value + ' %';
		case 'Tamper':
			return 'Capteur trafiqué le ' + $filter('date')(sensor.update * 1000, 'dd/MM/yyyy à HH:mm');
		case 'Door/Window':
			return (sensor.value == 'on') ? 'Etat : Ouvert' : 'Etat : Fermé';
		case 'Temperature':
			return 'Température : ' + sensor.value + ' ' + sensor.unit;
		case 'Luminiscence':
			return 'Luminosité : ' + sensor.value + ' ' + sensor.unit;
		case 'switchBinary':
			return (sensor.value == 'on') ? 'Allumé' : 'Eteint';
		case 'General purpose':
			return 'Dernier mouvement détecté le ' + $filter('date')(sensor.update * 1000, 'dd/MM/yyyy à HH:mm');
		case 'sensorBinary':
			return 'Dernier mouvement le ' + $filter('date')(sensor.update * 1000, 'dd/MM/yyyy à HH:mm');
		default:
			return sensor.value + ' ' + sensor.unit;
		}
	};
	$scope.toggleSwitch = function(sensor) {
		var nURL = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api;
		nURL += '&toggle=' + sensor.sensor + "&value=" + sensor.value;
		$http.put(nURL).success(function(data, status) {

		})
	};
	$scope.editSensors = function(e) {
		$mdDialog.show({
			templateUrl : 'views/sensors_settings.html',
			targetEvent : e,
			controller : 'SensorsSettingsCtrl'
		}).then(function(result) {
			if (result != null && result.code == 202) {
				getSensors($scope, $http);
			}
		});
	};
	$scope.editSensor = function(sensor, e){
		$mdDialog.show({
			templateUrl : 'views/sensor_settings.html',
			targetEvent : e,
			controller : 'SensorSettingsCtrl',
			locals : {
				sensor : sensor
			},
		}).then(function(nSensor) {
//			if (nLight != null) {
//				if (nLight.result == null) {
//					light = nLight;
//				} else {
//					if (nLight.result === 'delete') {
//						var i = $scope.lights.indexOf(light);
//						$scope.lights.splice(i, 1);
//					}
//				}
//			}
		});
	};
	function getLights($scope, $http) {
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status) {
			$scope.lights = data.lights;
		});
	}
	function getSensors($scope, $http) {
		var nURL = AtlantisUri.Sensors() + '?api=' + $sessionStorage.api
				+ '&action=get';
		$http.get(nURL).success(function(data, status) {
			$scope.capteurs = data.devices;
			$rootScope.rooms = data.rooms;
		});
	}
	function getRoom($scope, room) {
		var room = $filter('filter')($scope.rooms, {
			'id' : room
		});
		if (room.length == 1) {
			return room[0].room;
		} else {
			return false;
		}
	}
});
