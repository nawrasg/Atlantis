'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:HistoryCtrl
 * @description # HistoryCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('HistoryCtrl', function($scope, $http, $sessionStorage,	$filter, AtlantisUri) {
	get();
	$scope.loadPlant = function(){
		var nURL = AtlantisUri.History() + '?api=' + $sessionStorage.api;
		nURL += '&plant=' + $scope.plante.id;
		$http.get(nURL).success(function(data, status){
			var nLabels = [];
			var nMoisture = [];
			var nTemp = [];
			angular.forEach(data, function(day){
				nLabels.push($filter('date')(day.date, 'EEEE d'));
				nMoisture.push($filter('number')(day.moisture_m, 1));
				nTemp.push($filter('number')(day.soil_temperature_m, 1));
			});
			$scope.labels = nLabels;
			$scope.data = [nMoisture, nTemp];
			$scope.series = ['Humidité du sol', 'Température du sol'];
		});
	};
	function get() {
		var nURL = AtlantisUri.History() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.plantes = data.plants;
			}
		});
	}
});
