'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:HistoryCtrl
 * @description # HistoryCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('HistoryCtrl', function($scope, $http, $sessionStorage,
		$filter, AtlantisUri) {
	var nChartOptions = {
		datasetFill : false
	};
	$scope.options = nChartOptions;
	get();
	$scope.loadPlant = function() {
		var nURL = AtlantisUri.History() + '?api=' + $sessionStorage.api;
		nURL += '&plant=' + $scope.plante.id + '&interval=' + $scope.interval;
		$http.get(nURL).success(function(data, status) {
			var nLabels = [];
			var nMoisture = [];
			var nTemp = [];
			angular.forEach(data, function(day) {
				nLabels.push($filter('date')(day.date, 'EEEE d'));
				nMoisture.push($filter('number')(day.moisture_m, 1));
				nTemp.push($filter('number')(day.soil_temperature_m, 1));
			});
			$scope.labels = nLabels;
			$scope.data = [ nMoisture, nTemp ];
			$scope.series = [ 'Humidité du sol', 'Température du sol' ];
		});
	};
	$scope.loadSensor = function() {
		var nURL = AtlantisUri.History() + '?api=' + $sessionStorage.api;
		nURL += '&sensor=' + $scope.capteur.id + '&interval=' + $scope.interval;
		$http.get(nURL).success(function(data, status) {
			var nLabels = [];
			var nValues = [];
			var nLastDate = '';
			angular.forEach(data, function(day) {
				nValues.push($filter('number')(day.value, 1));
				if(day.date == nLastDate){
					nLabels.push(day.time);
				}else{
					nLabels.push($filter('date')(day.date, 'EEEE d') + ' - ' + day.time);
					nLastDate = day.date;
				}
			});
			$scope.labels = nLabels;
			$scope.data = [ nValues ];
			$scope.series = [ $scope.capteur.type + ' ' + $scope.capteur.unit ];
		})
	};
	function get() {
		var nURL = AtlantisUri.History() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status) {
			if (status == 202) {
				$scope.plantes = data.plants;
				$scope.capteurs = data.sensors;
			}
		});
	}
});
