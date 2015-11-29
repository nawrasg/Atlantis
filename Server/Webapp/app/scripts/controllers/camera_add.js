'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:CameraAddCtrl
 * @description # CameraAddCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('CameraAddCtrl', function($scope, $rootScope, $http, $sessionStorage, $mdDialog, $mdToast, AtlantisUri, camera) {
	if(camera != null){
		$scope.id = camera.id;
		$scope.type = camera.type;
		$scope.ip = camera.ip;
		$scope.image = camera.image;
		$scope.video = camera.video;
		$scope.username = camera.username;
		$scope.password = camera.password;
		$scope.alias = camera.alias;
		$scope.room = {id:camera.room};
	}
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
	$scope.rooms = $rootScope.rooms;
	$scope.save = function(){
		if(camera == null){
			if($scope.ip == null || $scope.ip == '' || $scope.type == null || $scope.type == '' || $scope.image == null || $scope.image == ''){
				$mdToast.show($mdToast, 'Merci de remplir l\'adresse IP, le type et l\'URL image de la caméra !');
			}else{
				var nURL = AtlantisUri.Cameras() + '?api=' + $sessionStorage.api;
				nURL += '&type=' + $scope.type + '&ip=' + $scope.ip + '&image=' + $scope.image;
				if($scope.video != null && $scope.video != ''){
					nURL += '&video=' + $scope.video;
				}
				if($scope.username != null && $scope.username != '' && $scope.password != null && $scope.password != ''){
					nURL += '&username=' + $scope.username + '&password=' + $scope.password;
				}
				if($scope.alias != null && $scope.room != null){
					nURL += '&alias=' + $scope.alias;
				}
				if($scope.room != null && $scope.room.id != null){
					nURL += '&room=' + $scope.room.id;
				}
				$http.post(nURL).success(function(data, status){
					if(status == 202){
						$mdDialog.hide();
					}
				});
			}
		}else{
			var nURL = AtlantisUri.Cameras() + '?api=' + $sessionStorage.api;
			nURL += '&id=' + camera.id + '&alias=' + $scope.camera.alias + '&room=' + $scope.camera.room + '&username=' + $scope.camera.username + '&password=' + $scope.camera.password;
			$http.put(nURL).success(function(data, status){
				if(status == 202){
					$mdDialog.hide();
				}
			});
		}
	};
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
	$scope.delete = function(){
		if(camera != null){
			var nURL = AtlantisUri.Cameras() + '?api=' + $sessionStorage.api + '&id=' + camera.id;
			$http.delete(nURL).success(function(data, status){
				if(status == 202){
					$mdDialog.hide({id:-1});
				}
			});
		}
	};
});
