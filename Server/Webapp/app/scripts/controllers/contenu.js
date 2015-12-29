'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ContenuCtrl
 * @description # ContenuCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ContenuCtrl', function($scope, $http, $window, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
	getCuisine();
	$scope.add = function(e){
		$mdDialog.show({
			templateUrl: 'views/cuisine_add.html',
			targetEvent: e,
			controller: 'CuisineAddCtrl'
		}).then(function(item){
			$scope.data.push(item);
			$scope.data = $filter('orderBy')($scope.data, 'peremption');
			loadCuisine();
		}, function(){
			//
		});	
	};
	$scope.filter = function(item){
		if(item == null || item == ''){
			loadCuisine();
		}else{
			var result = [];
			angular.forEach($scope.data, function(current){
				if($filter('lowercase')(current.label).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.frigos = $filter('filter')(result, {endroit:'frigidaire'});
			$scope.congelos = $filter('filter')(result, {endroit:'congelateur'});
			$scope.placards = $filter('filter')(result, {endroit:'placard'});
		}
	};
	$scope.open = function(item){
		var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api + '&open=' + item.id;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				item.status = 1;
				getCuisine();
			}
		})
	};
	$scope.ignore = function(item){
		var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api + '&ignore=' + item.id;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				item.ignore = 1;
			}
		})
	}
	$scope.modify = function(item, operation){
		switch(operation){
		case '+':
			{
				var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api;
				nURL += "&id=" + item.id + "&quantite=" + (parseInt(item.quantite) + 1);
				$http.put(nURL).success(function(data, status){
					if(status == 202){
						item.quantite = parseInt(item.quantite) + 1;
					}
				});			
			}
			break;
		case '-':
			{
				var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api;
				nURL += "&id=" + item.id + "&quantite=" + (parseInt(item.quantite) - 1) + '&close=true';
				$http.put(nURL).success(function(data, status){
					if(status == 202){
						item.quantite = parseInt(item.quantite) - 1;	
						item.status = 0;	
						getCuisine();
					}
				});			
			}
			break;
		case '.':
			{
				var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api + '&id=' + item.id;
				$http.delete(nURL).success(function(data, status){
					var i = $scope.data.indexOf(item);
					$scope.data.splice(i, 1);
					loadCuisine();
				});
			}
			break;
		}
		
	};
	$scope.status = function(day, open, place){
		if(place == 'congelateur'){
			return 'images/ng_ball_blue.png';
		}else{
			if(day < 0){
				return 'images/ng_ball_red.png';
			}else{
				if(open == 1){
					return 'images/ng_ball_orange.png';
				}else{
					if(place == 'frigidaire'){
						return 'images/ng_ball_green.png';						
					}else{
						return 'images/ng_empty.png';
					}
				}
			}
		}
	};
	$scope.peremption = function(produit){
		if(produit.place == 'congelateur'){
			return 'Congelé depuis ' + getDateUnit(produit.peremption, $window);
		}else{
			if(produit.peremption < 0){
				return 'Périmé depuis ' + getDateUnit(produit.peremption, $window);
			}else{
				if(produit.ouvert < 0){
					return 'Ouvert depuis ' + getDateUnit(produit.ouvert, $window);
				}else{
					return 'A consommer dans ' + getDateUnit(produit.peremption, $window);						
				}
			}
		}
	};
	function getCuisine(){
		var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			$scope.data = data;
			$scope.frigos = $filter('filter')(data, {endroit:'frigidaire'});
			$scope.congelos = $filter('filter')(data, {endroit:'congelateur'});
			$scope.placards = $filter('filter')(data, {endroit:'placard'});
		});
	}
	function loadCuisine(){
		$scope.frigos = $filter('filter')($scope.data, {endroit:'frigidaire'});
		$scope.congelos = $filter('filter')($scope.data, {endroit:'congelateur'});
		$scope.placards = $filter('filter')($scope.data, {endroit:'placard'});
	}
});


