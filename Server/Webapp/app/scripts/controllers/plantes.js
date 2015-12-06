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
				getPlants();
			}
		});
	};
	$scope.filter = function(item){
		if(item == null || item == ''){
			getPlants();
		}else{
			var result = [];
			angular.forEach($scope.data, function(current){
				if($filter('lowercase')(current.title).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.plants = result;
		}
	};
	$scope.add = function(){
		var nURL = AtlantisUri.Plantes() + '?api=' + $sessionStorage.api;
		$http.post(nURL).success(function(data, status){
			getPlants();
		});
	};
	$scope.getPlantImage = function(plant){
		return AtlantisUri.Images() + '?api=' + $sessionStorage.api + '&type=plant&id=' + plant.id;
	};
	function getPlants(){
		var nURL = AtlantisUri.Plantes() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.data = data;
				$scope.plants = data;
			}
		});
	}
});
