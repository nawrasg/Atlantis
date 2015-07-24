'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:EntretienCtrl
 * @description # EntretienCtrl Controller of the atlantisWebAppApp
 */
var nApp = angular.module('atlantisWebAppApp');

nApp.controller('EntretienCtrl', function($scope, $sessionStorage, $http, $window, $mdDialog, $filter, AtlantisUri) {
	getEntretien();
	$scope.add = function(e){
		$mdDialog.show({
			templateUrl: 'views/entretien_add.html',
			targetEvent: e,
			controller: 'EntretienAddCtrl'
		}).then(function(item){
			$scope.data.push(item);
			$scope.data = $filter('orderBy')($scope.data, 'peremption');
			$scope.entretiens = $scope.data;
		});
	}
	$scope.filter = function(item){
		if(item == null || item == ''){
			$scope.entretiens = $scope.data;
		}else{
			var result = [];
			angular.forEach($scope.data, function(current){
				if($filter('lowercase')(current.nom).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.entretiens = result;
		}
	}
	$scope.modify = function(item, operation){
		switch(operation){
		case '+':
			var nURL = AtlantisUri.Entretien() + '?api=' + $sessionStorage.api;
			nURL += '&id=' + item.id + '&qte=' + (parseInt(item.quantite) + 1);
			$http.put(nURL).success(function(data, status){
				if(status == 202){
					item.quantite = parseInt(item.quantite) + 1;					
				}
			});
			break;
		case '-':
			var nURL = AtlantisUri.Entretien() + '?api=' + $sessionStorage.api;
			nURL += '&id=' + item.id + '&qte=' + (parseInt(item.quantite) - 1);
			$http.put(nURL).success(function(data, status){
				if(status == 202){					
					item.quantite = parseInt(item.quantite) - 1;
				}
			});
			break;
		case '.':
			var nURL = AtlantisUri.Entretien() + '?api=' + $sessionStorage.api;
			nURL += '&id=' + item.id;
			$http.delete(nURL).success(function(data, status){
				if(status == 202){
					var i = $scope.data.indexOf(item);
					$scope.data.splice(i, 1);
					$scope.entretiens = $scope.data;
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
	function getEntretien(){
		var nURL = AtlantisUri.Entretien() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.data = data;
				$scope.entretiens = data;				
			}
		});
	}
});
