'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:PharmacieCtrl
 * @description # PharmacieCtrl Controller of the atlantisWebAppApp
 */
var nApp = angular.module('atlantisWebAppApp');

nApp.controller('PharmacieCtrl', function($scope, $sessionStorage, $http, $window, $mdDialog, $filter, AtlantisUri) {
	getPharmacie();
	$scope.add = function(e){
		$mdDialog.show({
			templateUrl: 'views/pharmacie_add.html',
			targetEvent: e,
			controller: 'PharmacieAddCtrl'
		}).then(function(item){
			$scope.data.push(item);
			$scope.data = $filter('orderBy')($scope.data, 'peremption');
			$scope.pharmacies = $scope.data;
		});
	}
	$scope.filter = function(item){
		if(item == null || item == ''){
			$scope.pharmacies = $scope.data;
		}else{
			var result = [];
			angular.forEach($scope.data, function(current){
				if($filter('lowercase')(current.nom).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.pharmacies = result;
		}
	}
	$scope.modify = function(item, operation){
		switch(operation){
		case '+':
			var nURLP = AtlantisUri.Pharmacie() + '?api=' + $sessionStorage.api;
			nURLP += '&id=' + item.id + '&qte=' + (parseInt(item.quantite) + 1);
			$http.put(nURLP).success(function(data, status){
				if(status == 202){
					item.quantite = parseInt(item.quantite) + 1;					
				}
			});
			break;
		case '-':
			var nURLM = AtlantisUri.Pharmacie() + '?api=' + $sessionStorage.api;
			nURLM += '&id=' + item.id + '&qte=' + (parseInt(item.quantite) - 1);
			$http.put(nURLM).success(function(data, status){
				if(status == 202){
					item.quantite = parseInt(item.quantite) - 1;					
				}
			});
			break;
		case '.':
			var nURL = AtlantisUri.Pharmacie() + '?api=' + $sessionStorage.api;
			nURL += '&id=' + item.id;
			$http.delete(nURL).success(function(data, status){
				if(status == 202){
					var i = $scope.data.indexOf(item);
					$scope.data.splice(i, 1);
					$scope.pharmacies = $scope.data;
				}
			})
			break;
		}
	};
	$scope.peremption = function(day){
		if(day < 0){
			return 'Périmé depuis ' + getDateUnit(day, $window);
		}else{
			return 'Valable encore ' + getDateUnit(day, $window);						
		}
	};
	$scope.status = function(day){
		if(day < 0){
			return 'images/ng_ball_red.png';
		}else{
			return 'images/ng_empty.png';
		}
	};
	function getPharmacie(){
		var nURL = AtlantisUri.Pharmacie() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.pharmacies = data;
				$scope.data = data;				
			}
		});
	}
});
