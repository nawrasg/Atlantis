'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:LoginCtrl
 * @description # LoginCtrl Controller of the atlantisWebAppApp
 */
nApp.controller('LoginCtrl', function($scope, $http, base64, $sessionStorage, $rootScope, $location, AtlantisUri) {
	$scope.auth = function(){
		var nUsername = $scope.user.username;
		var nPwd = $scope.user.password;
		var nURL = AtlantisUri.Login();
		$http.defaults.headers.common.Authorization = base64.encode(nUsername + ':' + nPwd);
		$http.get(nURL).success(function(data, status, header, config){
			if(status == 202){
				$scope.user  = null;
				$rootScope.navigation = false;
				$sessionStorage.api = data;
				delete $http.defaults.headers.common.Authorization;
				$location.path('/home');
			}else{
				console.log(status, data);
			}
		});
	};
});
