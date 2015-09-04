'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:GeoCtrl
 * @description # GeoCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('GeoCtrl', function($scope, $http, $sessionStorage, $mdToast, AtlantisUri) {
	$scope.admin = ($sessionStorage.user.type == 0);
	get();
	$scope.request = function(status){
		var nURL = AtlantisUri.Geo() + '?api=' + $sessionStorage.api;
		switch(status){
		case 'p':
			$http.post(nURL).success(function(data, status){
				if(status == 202){
					showToast($mdToast, 'Demande envoyée !');
				}
			});		
			break;
		case 's':
			break;
		}
	};
	$scope.getLocation = function(){
		get();
	};
	function get(){
		var nURL =  AtlantisUri.Geo() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.home = data.atlantis;
				$scope.devices = data.positions;
			}
		});
	}
});
