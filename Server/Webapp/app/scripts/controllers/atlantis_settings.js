'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:AtlantisSettingsCtrl
 * @description # AtlantisSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('AtlantisSettingsCtrl', function($scope, $http,
		$sessionStorage, $mdToast, AtlantisUri) {
	
	var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
	$http.get(nURL).success(function(data, status) {
		if (status == 202) {
			$scope.atlantis = data;
		}
	});
	$scope.saveGeneral = function() {
		var url = $scope.atlantis[0].url;
		var dep = $scope.atlantis[0].dep;
		var city = $scope.atlantis[0].city;
		var lat = $scope.atlantis[0].lat;
		var long = $scope.atlantis[0].long;
		var radius = $scope.atlantis[0].radius;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Atlantis&url=' + url + '&dep=' + dep + '&city='
				+ city + '&lat=' + lat + '&long=' + long + '&radius=' + radius;
		$http.put(nURL).success(
				function(data, status) {
					showToast($mdToast, status, data);
				});
	};
	$scope.saveNotification = function() {
		var key = $scope.atlantis[1].key;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Notification&key=' + key;
		$http.put(nURL).success(function(data, status) {
			showToast($mdToast, status, data);
		});
	};
	$scope.saveSensors = function() {
		var hue_ip = $scope.atlantis[6].ip;
		var hue_user = $scope.atlantis[6].user;
		var zwave_ip = $scope.atlantis[3].IP;
		var zwave_port = $scope.atlantis[3].Port;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Zwave&ip=' + zwave_ip + '&port=' + zwave_port;
		$http.put(nURL).success(function(data, status) {
			showToast($mdToast, status, data);
		});
		nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Hue&ip=' + hue_ip + '&user=' + hue_user;
		$http.put(nURL).success(function(data, status) {
			showToast($mdToast, status, data);
		});
	};
	
	

});
