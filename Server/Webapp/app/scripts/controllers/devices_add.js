'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:DevicesAddCtrl
 * @description # DevicesAddCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('DevicesAddCtrl', function($scope, $http, $sessionStorage, $mdDialog, $mdToast, AtlantisUri, users, device) {
	$scope.types = [{
		id:'windows',
		label:'Microsoft Windows'
	},{
		id:'smartphone',
		label: 'Smartphone'
	},{
		id:'linux',
		label: 'Linux'
	},{
		id:'imprimante',
		label: 'Imprimante'
	},{
		id:'autre',
		label: 'Autre'
	}];
	$scope.connections = [{
		id:'wifi',
		label:'Wifi'
	},{
		id:'ethernet',
		label: 'Ethernet'
	}];
	$scope.users = users;
	if(device != null){
		$scope.btnSubmit = 'Modifier';
		$scope.name = device.nom;
		$scope.ip = device.ip;
		$scope.mac = device.mac;
		$scope.type = {id:device.type};
		$scope.connection = {id:device.connexion};
		$scope.user = {id:device.username};
	}else{
		$scope.btnSubmit = 'Ajouter';
	}
	$scope.submit = function(){
		if($scope.name == null || $scope.ip == null || $scope.mac == null || $scope.type == null || $scope.connection == null){
			showToast($mdToast, "Merci de remplir les détails de l'appareil !");
			return;
		}
		var nURL = AtlantisUri.Devices() + '?api=' + $sessionStorage.api;
		nURL += '&title=' + $scope.name + '&ip=' + $scope.ip + '&mac=' + $scope.mac + '&type=' + $scope.type.id + '&connection=' + $scope.connection.id;
		if($scope.user != null && $scope.user.id != ''){
			nURL += '&user=' + $scope.user.id;
		}
		if(device == null){
			$http.post(nURL).success(function(data, status){
				if(status == 202){
					$mdDialog.hide(data);
				}
			})
		}else{
			nURL += '&id=' + device.id;
			$http.put(nURL).success(function(data, status){
				if(status == 202){
					var nItem = {nom:$scope.name, ip:$scope.ip, mac:angular.uppercase($scope.mac), type:$scope.type.id, connexion:$scope.connection.id, username:$scope.user.id};
					$mdDialog.hide(nItem);
				}
			});
		}
	};
	$scope.ring = function(device){
		var nURL = AtlantisUri.Notify() + '?api=' + $sessionStorage.api + '&cmd=ring&id=' + $scope.mac;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				showToast($mdToast, 'Notification envoyée !');
			}
		});
	};
	$scope.cancel = function(){
		$mdDialog.cancel();
	};
});
