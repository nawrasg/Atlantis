'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:GeoCtrl
 * @description # GeoCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('GeoCtrl', function($scope, $http, $sessionStorage, $rootScope, AtlantisUri) {
	$scope.home = {lat:0, long:0};
	get();
	$scope.request = function(){
		var nURL = AtlantisUri.Geo() + '?api=' + $sessionStorage.api + '&action=request';
		$http.get(nURL);		
	};
	$scope.getLocation = function(){
		var nURL =  AtlantisUri.Geo() + '?api=' + $sessionStorage.api + '&action=get' ;
		$http.get(nURL).success(function(data, status){
			if(status == 200){
				$scope.devices.push( data);
			}
			console.log(data);
		});
	};
	function get(){
		var nURL =  AtlantisUri.Geo() + '?api=' + $sessionStorage.api + '&action=get' ;
		$http.get(nURL).success(function(data, status){
			if(status == 200){
				$scope.devices = data;
			}
		});
	}
});
