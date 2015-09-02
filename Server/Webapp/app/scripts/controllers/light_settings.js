'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:LightSettingsCtrl
 * @description # LightSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('LightSettingsCtrl', function($scope, $rootScope, $mdDialog, $http, $sessionStorage, AtlantisUri, light) {
	$scope.cancel = function(){
		$mdDialog.hide();
	}
	$scope.save = function(){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&set=' + $scope.light.id + '&room=' + $scope.light.room + '&name=' + $scope.light.name + '&uid=' + $scope.light.uid;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide($scope.light);
			}
		})
	}
	$scope.changeColor = function(color){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&color=' + $scope.light.uid + '&value=' + color;
		$http.put(nURL);
	}
	$scope.delete = function(){
		var nURL = AtlantisUri.Lights() + '?api=' + $sessionStorage.api;
		nURL += '&id=' + $scope.light.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide({result:'delete'});				
			}else{
				console.log(status, data);
			}
		})
	};
	$scope.rooms = $rootScope.rooms;
	$scope.light = light;
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
});
