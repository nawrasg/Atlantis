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
	$scope.saveAudio = function(){
		var source = $scope.atlantis.Audio.source;
		var welcome = $scope.atlantis.Audio.welcome;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api;
		nURL += '&section=Audio&source=' + source + '&welcome=' + welcome;
		$http.put(nURL).success(function(data, status) {
			if(status == 202){
				showToast($mdToast, 'Paramètres modifiés avec succès !');
			}else{
				showToast($mdToast, 'Impossible de modifier les paramètres (erreur ' + status + ') !');
			}
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
	$scope.saveMail = function(){
		var server = $scope.atlantis.SMTP.server;
		var port = $scope.atlantis.SMTP.port;
		var security = $scope.atlantis.SMTP.security;
		var auth = $scope.atlantis.SMTP.auth;
		var username = $scope.atlantis.SMTP.username;
		var password = $scope.atlantis.SMTP.password;
		var fromName = $scope.atlantis.SMTP.fromName;
		var fromMail = $scope.atlantis.SMTP.fromMail;
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api + '&section=SMTP';
		nURL += '&server=' + server + '&port=' + port + '&security=' + security + '&auth=' + auth;
		nURL += '&username=' + username + '&password=' + password + '&fromName=' + fromName + '&fromMail=' + fromMail;
		$http.put(nURL).success(function(data, status){
			console.log(status, data);
			if(status == 202){
				//TODO
			}
		});
	};
});
