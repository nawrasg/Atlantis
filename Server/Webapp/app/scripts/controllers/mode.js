'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ModeCtrl
 * @description # ModeCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ModeCtrl', function($scope, $http, $sessionStorage, AtlantisUri) {
	$scope.setMode = function(mode){
		var nURL = AtlantisUri.Home() + '?api=' + $sessionStorage.api + '&mode=' + mode;
		$http.put(nURL).success(function(data, status){
			//TODO
		});
	}
});
