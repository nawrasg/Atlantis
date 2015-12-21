'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:UsersSettingsCtrl
 * @description # UsersSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('UsersSettingsCtrl', function($scope, $http, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
	$scope.filter = function(item){
		if(item == null || item == ''){
			loadUsers();
		}else{
			var result = [];
			angular.forEach($scope.users, function(current){
				if($filter('lowercase')(current.nom).indexOf($filter('lowercase')(item)) > -1){
					result.push(current);
				}
			})
			$scope.users = result;
		}
	};
	$scope.addUser = function(e){
		$mdDialog.show({
			templateUrl: 'views/user_add.html',
			targetEvent: e,
			controller: 'UserAddCtrl',
			locals:{
				user:null
			}
		}).then(function(newUser){
			if(newUser != null){
				$scope.users.push(newUser);
			}
		}, function(){
			//
		});			
	};
	$scope.changeAPI = function(user){
		var nURL = AtlantisUri.Users() + '?api=' + $sessionStorage.api + '&cle=' + user.id;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				user.cle = data;
			}
		});
	};
	$scope.editUser = function(user, e){
		$mdDialog.show({
			templateUrl: 'views/user_add.html',
			targetEvent: e,
			controller: 'UserAddCtrl',
			locals: {
				user: user
			},
		}).then(function(updatedUser){
			user = updatedUser;
		}, function(){
			// TODO
		});
	};
	$scope.delUser = function(user){
		var nURL = AtlantisUri.Users() + '?api=' + $sessionStorage.api + '&id=' + user.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				var i = $scope.users.indexOf(user);
				$scope.users.splice(i, 1);				
			}
		});
	};
	loadUsers();
	function loadUsers(){
		var nURL = AtlantisUri.Users() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.users = data;
				console.log(data);
			}
		});
	}
});
