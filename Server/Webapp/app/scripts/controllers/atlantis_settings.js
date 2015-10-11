'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:AtlantisSettingsCtrl
 * @description # AtlantisSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('AtlantisSettingsCtrl', function($scope, $http, $sessionStorage, $mdToast, AtlantisUri) {
	
	var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
	$http.get(nURL).success(function(data, status) {
		if (status == 202) {
			$scope.atlantis = data;
		}
	});
	$scope.saveGeneral = function() {
		var url = $scope.atlantis.Atlantis.url;
		var dep = $scope.atlantis.Atlantis.dep;
		var city = $scope.atlantis.Atlantis.city;
		var lat = $scope.atlantis.Atlantis.lat;
		var long = $scope.atlantis.Atlantis.long;
		var radius = $scope.atlantis.Atlantis.radius;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Atlantis&url=' + url + '&dep=' + dep + '&city=' + city + '&lat=' + lat + '&long=' + long + '&radius=' + radius;
		$http.put(nURL).success(
				function(data, status) {
					showToast($mdToast, status, data);
				});
	};
	$scope.saveNotification = function() {
		var key = $scope.atlantis.Notification.key;
		var appid = $scope.atlantis.Weather.appid;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Notification&key=' + key + '&appid=' + appid;
		$http.put(nURL).success(function(data, status) {
			showToast($mdToast, status, data);
		});
	};
	$scope.saveSensors = function() {
		var hue_ip = $scope.atlantis.Hue.ip;
		var hue_user = $scope.atlantis.Hue.user;
		var zwave_ip = $scope.atlantis.Zwave.IP;
		var zwave_port = $scope.atlantis.Zwave.Port;
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
	$scope.saveFiles = function(){
		var music = $scope.atlantis.Files.music;
		var nURL = AtlantisUri.Settings() + "?api=" + $sessionStorage.api;
		nURL += "&section=Files&music=" + music;
		$http.put(nURL).success(function(data, status){
			showToast($mdToast, status, data);
		});
	};
	

});
