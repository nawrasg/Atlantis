'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:MainCtrl
 * @description # MainCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('MainCtrl', function($scope, $rootScope, $http, $sessionStorage, AtlantisUri) {
	get();
	$scope.launch = function(scenario){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api + '&scenario=' + scenario;
		$http.get(nURL).success(function(data, status){
			console.log(status, data);
		});
	};
	function get(){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.scenarios = data;
			}
		});
		var nURL2 = AtlantisUri.Rooms() + '?api=' + $sessionStorage.api;
		$http.get(nURL2).success(function(data, status){
			if(status == 202){
				$rootScope.rooms = data;
			}
		});
	}
});
