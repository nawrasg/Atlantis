'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:CuisineAddCtrl
 * @description # CuisineAddCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('CuisineAddCtrl', function($scope, $http, $filter, $localStorage, $sessionStorage, $mdDialog, $mdToast, AtlantisUri) {
	$scope.places = [
	                 {
	                	 id:'placard',
	                	 label:'Placard'
	                 },{
	                	 id:'congelateur',
	                	 label:'Congélateur'
	                 },{
	                	 id:'frigidaire',
	                	 label:'Frigidaire'
	                 }
	                 ];
	if($localStorage.ean == null || $localStorage.ean == '') {
		getEAN();
	}else{
		$scope.items = $localStorage.ean;
	}
	$scope.filterList = function(item){
		var items = $localStorage.ean;
		var results = [];
		angular.forEach(items, function(current){
			if($filter('lowercase')(current.ean).indexOf($filter('lowercase')(item)) > -1 || $filter('lowercase')(current.nom).indexOf($filter('lowercase')(item)) > -1){
				results.push(current);
			}
		})
		return results;
	}
	$scope.itemChange = function(){
		$scope.codebarre = null;
	}
	$scope.itemSelect = function(){
		if($scope.selectedItem.ean != null){
			$scope.codebarre = $scope.selectedItem.ean;
		}
	}
	$scope.submit = function(){
		if($scope.searchText == null || $scope.searchText == '' || $scope.date == null || $scope.date == ''){
			showToast($mdToast, 'Merci de remplir le nom et la date de péremption du produit !');
			return;
		}
		var nURL = AtlantisUri.Cuisine() + '?api=' + $sessionStorage.api;
		if($scope.selectedItem.ean == null){
			nURL += '&element=' + $scope.searchText;
			if($scope.codebarre != null && $scope.codebarre != ''){
				nURL += '&ean=' + $scope.codebarre; 
			}
		}else{
			nURL += '&element=' + $scope.selectedItem.ean;
		}
		nURL += '&peremption=' + $filter('date')($scope.date, 'yyyy-MM-dd') + '&endroit=' + $scope.place.id + '&quantite=' + $scope.quantity;
		$http.post(nURL).success(function(data, status){
			if(status == 202){
				$mdDialog.hide(data);
			}
		})
	}
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
	function getEAN(){
		var nURL = AtlantisUri.Ean() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			$localStorage.ean = data;
			$scope.items = $localStorage.ean;
		});
	}
});
