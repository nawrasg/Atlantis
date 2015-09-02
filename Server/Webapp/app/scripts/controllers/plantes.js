'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:PlantesCtrl
 * @description # PlantesCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('PlantesCtrl', function($scope, $http, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
	getPlants();
	$scope.getRoomLabel = function(plant){
		if(plant.room != null){
			var nRoom = $filter('filter')($sessionStorage.rooms, {id : plant.room});
			if (nRoom != null && nRoom.length == 1) {
				return '(' + nRoom[0].room + ')';
			}
		}
	};
	$scope.editPlant = function(plant, e){
		$mdDialog.show({
			templateUrl : 'views/plant_settings.html',
			targetEvent : e,
			controller : 'PlantSettingsCtrl',
			locals : {
				plant : plant
			},
		}).then(function(nPlant) {
			if(nPlant != null){
//				plant = nPlant;
				getPlants();
			}
		});
	};
	function getPlants(){
		var nURL = AtlantisUri.Plantes() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.plants = data;
			}
		});
	}
});
