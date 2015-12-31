'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:UserAddCtrl
 * @description # UserAddCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('UserAddCtrl', function($scope, $http, $sessionStorage, $mdDialog, $mdToast, AtlantisUri, user) {
	$scope.types = [{
		id:0,
		label:'Administrateur'
	},{
		id:1,
		label: 'Utilisateur'
	},{
		id:2,
		label: 'Visiteur'
	}];
	if(user != null){
		$scope.user = {name: user.nom, phone: user.phone, mail: user.mail, type: {id:user.type}};
		$scope.btnSubmit = "Modifier";
	}else{
		$scope.btnSubmit = "Ajouter";
	}
	
	$scope.submit = function(){
		add();
	};
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
	
	function add(){
		var nURL = AtlantisUri.Users() + '?api=' + $sessionStorage.api;
		if(user == null){
			nURL += '&name=' + $scope.user.name + '&type=' + $scope.user.type.id + '&pwd=' + $scope.user.pwd;
			$http.post(nURL).success(function(data, status){
				if(status == 202){
					$mdDialog.hide(data);
				}else{
					showToast($mdToast, status, data);
				}
			});					
		}else{
			nURL += '&id=' + user.id + '&type=' + $scope.user.type.id + '&mail=' + $scope.user.mail + '&phone=' + $scope.user.phone;
			if($scope.user.pwd != null && $scope.user.pwd != ''){
				nURL += '&pwd=' + $scope.user.pwd;
			}
			$http.put(nURL).success(function(data, status){
				showToast($mdToast, status, data);
				if(status == 202){
					user.mail = $scope.user.mail;
					user.phone = $scope.user.phone;
					user.type = $scope.user.type.id;
					$mdDialog.hide(user);
				}
			});
		}
	}
});
