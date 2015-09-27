'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:MainCtrl
 * @description # MainCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('MainCtrl', function($scope, $http, $sessionStorage, AtlantisUri) {
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
	}
});
