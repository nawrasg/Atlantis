'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:DiversSettingsCtrl
 * @description # DiversSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('DiversSettingsCtrl', function($scope, $http, $sessionStorage, AtlantisUri) {
	get();
	$scope.toggleCall = function(settings, status){
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api + '&section=CallNotifier&key=' + settings + '&value=' + status;
		$http.put(nURL).success(function(data, status){
			//TODO
		});
	};
	function get(){
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api + '&type=CallNotifier';
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.call = data;
			}
		});
	}
});
