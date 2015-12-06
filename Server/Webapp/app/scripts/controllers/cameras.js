'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:CamerasCtrl
 * @description # CamerasCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('CamerasCtrl', function($scope, $rootScope, $http, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
	get();
	$scope.getTitle = function(camera){
		var nResult = '';
		if(camera.alias == null || camera.alias == ''){
			nResult = camera.type;
		}else{
			nResult = camera.alias;
		}
		if(camera.room != null && camera.room > -1){
			var nRoom = $filter('filter')($rootScope.rooms, {id : camera.room});
			if (nRoom != null && nRoom.length == 1) {
				return nResult + ' (' + nRoom[0].room + ')';
			}
		}
		return nResult;
	};
	$scope.editCamera = function(camera, e){
		$mdDialog.show({
			templateUrl : 'views/camera_add.html',
			targetEvent : e,
			controller : 'CameraAddCtrl',
			locals : {
				camera : camera
			},
		}).then(function(nCamera) {
			if(nCamera != null){
				get();
			}
		});
	};
	$scope.refresh = function(camera){
		//TODO
	};
	$scope.refreshVideo = function(camera){
		//TODO
	};
	$scope.add = function(e){
		$mdDialog.show({
			templateUrl : 'views/camera_add.html',
			targetEvent : e,
			controller : 'CameraAddCtrl',
			locals : {
				camera : null
			},
		}).then(function(nCamera) {
			if(nCamera != null){
				get();
			}
		});
	};
	$scope.getImageUrl = function(camera){
		if(camera.id >= 0){
			return AtlantisUri.Images() + '?type=camera&id=' + camera.id + '&api=' + $sessionStorage.api;
		}
	}
	function get(){
		var nURL = AtlantisUri.Cameras() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.cameras = data;
			}
		})
	}
});
