'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:OwncloudCtrl
 * @description # OwncloudCtrl Controller of the atlantisWebAppApp
 */
nApp.controller('OwncloudCtrl', function($scope, $http, $sessionStorage, $mdDialog, AtlantisUri, owncloud) {
	console.log(owncloud);
	$scope.owncloud = owncloud;
	$scope.close = function(){
		$mdDialog.hide();
	};
	$scope.save = function(){
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api + '&section=owncloud';
		nURL += '&path=' + $scope.owncloud.path + '&username=' + encodeURIComponent($scope.owncloud.username) + '&password=' + encodeURIComponent($scope.owncloud.password);
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide();
			}else{
				showToast($mdToast, status, data);
			}
		});
	};
});
