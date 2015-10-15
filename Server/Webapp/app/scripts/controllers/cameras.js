'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:CamerasCtrl
 * @description # CamerasCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('CamerasCtrl', function($scope, $http, $sessionStorage, $filter, AtlantisUri) {
	get();
	$scope.getTitle = function(camera){
		if(camera.alias == null || camera.alias == ''){
			return camera.type;
		}else{
			return camera.alias;
		}
	};
	function get(){
		var nURL = AtlantisUri.Cameras() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.cameras = data;
			}
		})
	}
});
