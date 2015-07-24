'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:DevicesCtrl
 * @description # DevicesCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('DevicesCtrl', function($scope, $http, $filter, $localStorage, $sessionStorage, $mdDialog, AtlantisUri) {
	get();
	$scope.add = function(e){
		$mdDialog.show({
			templateUrl: 'views/devices_add.html',
			targetEvent: e,
			controller: 'DevicesAddCtrl',
			locals:{
				users:$scope.users,
				device:null
			}
		}).then(function(item){
			$scope.data.push(item);
			$scope.data = $filter('orderBy')($scope.data, 'nom');
			$scope.devices = $scope.data;
		}, function(){
			//
		});	
	};
	$scope.ping = function(item){
		var nURL = AtlantisUri.Devices() + '?api=' + $sessionStorage.api + '&ping=' + item.ip;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				item.online = data.online;
			}
		})
	};
	$scope.del = function(item){
		var nURL = AtlantisUri.Devices() + '?api=' + $sessionStorage.api;
		nURL += '&id=' + item.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				var i = $scope.data.indexOf(item);
				$scope.data.splice(i, 1);
				$scope.devices = $scope.data;
			}
		})
	};
	$scope.edit = function(item, e){
		$mdDialog.show({
			templateUrl: 'views/devices_add.html',
			targetEvent: e,
			controller: 'DevicesAddCtrl',
			locals:{
				users:$scope.users,
				device:item
			}
		}).then(function(newItem){
			item.nom = newItem.nom;
			item.ip = newItem.ip;
			item.mac = newItem.mac;
			item.type = newItem.type;
			item.connexion = newItem.connexion;
			item.username = newItem.username;
		}, function(){
			//
		});
	};
	$scope.filter = function(item){
		if(item == null || item == ''){
			$scope.devices = $scope.data;
		}else{
			var result = [];
			angular.forEach($scope.data, function(current){
				if($filter('lowercase')(current.nom).indexOf($filter('lowercase')(item)) > -1 || $filter('lowercase')(current.ip).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.devices = result;
		}
	}
	$scope.connection = function(connection){
		switch(connection){
		case 'wifi':
			return 'images/ng_wifi.png';
		case 'ethernet':
			return 'images/ng_ethernet.png';
		}
	};
	$scope.status = function(online){
		switch(online){
		case 1:
			return 'images/ng_ball_green.png';
		case -1:
			return 'images/ng_ball_red.png';
		}
	};
	$scope.type = function(type){
		switch(type){
		case 'smartphone':
			return 'url(images/ng_smartphone.png)';
		case 'linux':
			return 'url(images/ng_server.png)';
		case 'windows':
			return 'url(images/ng_windows.png)';
		case 'imprimante':
			return 'url(images/ng_printer.png)';
		default:
			return 'url(images/ng_device.png)';
		}
	};
	function get(){
		$mdDialog.show({
			templateUrl: 'views/wait.html'
		});	
		var nURL = AtlantisUri.Devices() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			$scope.devices = data.devices;
			$scope.data = data.devices;
			$scope.users = data.users;
			$mdDialog.hide();
//			$localStorage.devices = data.devices;
//			$localStorage.users = data.users;
		});			
//		if($localStorage.devices == null || $localStorage.devices == '') {
//		}else{
//			$scope.devices = $localStorage.devices;
//			$scope.data = $localStorage.devices;
//			$scope.users = $localStorage.users;
//		}
	}
});
