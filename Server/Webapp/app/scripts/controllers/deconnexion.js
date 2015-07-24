'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:DeconnexionCtrl
 * @description # DeconnexionCtrl Controller of the atlantisWebAppApp
 */
nApp.controller('DeconnexionCtrl', function($scope, $sessionStorage, $rootScope, $location) {
	delete $sessionStorage.api;
	$rootScope.navigation = true;
	$location.path('/login');
});
