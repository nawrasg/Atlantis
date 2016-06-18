'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:LogCtrl
 * @description # LogCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('LogCtrl', function($scope, $http, $sessionStorage, $mdDialog, AtlantisUri, log) {
	$scope.levels = [{id:0, label:'Désactivé'},
	                  {id:1, label:'Diagnostic'},
	                  {id:2, label:'Information'},
	                  {id:3, label:'Avertissement'},
	                  {id:4, label:'Erreur'}];
	
	$http.get(AtlantisUri.Log() + '?api=' + $sessionStorage.api).success(function(data, status){
		if(status == 202){
			$scope.log = {id: log.level, log: data};
		}
	});		
	$scope.clear = function(){
		
	};
	$scope.close = function(){
		$mdDialog.hide();
	};
	$scope.changeLog = function(){
		console.log($scope.log.id);
	};
});
