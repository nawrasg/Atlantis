'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:PlantSettingsCtrl
 * @description # PlantSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('PlantSettingsCtrl', function($scope, $sessionStorage, $http, $mdDialog, $mdToast, $timeout, AtlantisUri, Upload, plant) {
	$scope.plant = plant;
	$scope.rooms = $sessionStorage.rooms;
	if($sessionStorage.user.type == 0){
		$scope.admin = true;
	}
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
	$scope.delete = function(){
		
	};
	$scope.save = function(){
		var nURL = AtlantisUri.Plantes() + "?api=" + $sessionStorage.api;
		nURL += "&id=" + $scope.plant.id + "&title=" + $scope.plant.title + "&room=" + $scope.plant.room;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide($scope.plant);
			}
		});
	};
	$scope.$watch('files', function () {
		if($scope.files && $scope.files.length){
			var nURL = AtlantisUri.Plantes();
			Upload.upload({
				url: nURL,
				fields: {
					'api': $sessionStorage.api,
					'id': plant.id
				},
				file: $scope.files
			}).progress(function (evt) {
				var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
				console.log(progressPercentage);
			}).success(function (data, status, headers, config) {
				console.log(status, data);
				if(status == 202){
					showToast($mdToast, 'Image envoyée avec succès !');
				}else{
					showToast($mdToast, status, data);
				}
			});			
		}
    });
});
