'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:SettingsCtrl
 * @description # SettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('SettingsCtrl', function($scope, $window, $sessionStorage, AtlantisUri) {
	initAccountType();
	$scope.exportSettings = function(){
		var nURL = AtlantisUri.Settings() + '?api=' + $sessionStorage.api + '&download=1';
		$window.open(nURL, '_blank');
	};
	function initAccountType(){
		var types = [];
		types.push({id:0, label:'Administrateur'});
		types.push({id:1, label:'Utilisateur'});
		types.push({id:2, label:'Visiteur'});
		$scope.types = types;
	}
});
