'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:UsersSettingsCtrl
 * @description # UsersSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('UsersSettingsCtrl', function($scope, $http, $sessionStorage, $filter, $mdDialog, AtlantisUri) {
//	var nUserAddDialog = '<md-dialog style=padding:20px><md-content><md-input-container><label>Nom</label><input ng-model=user.name class=dialog-close></md-input-container><md-input-container><label>Mot de passe</label><input ng-model=user.pwd class=dialog-close type=password></md-input-container><md-input-container><md-select ng-model=user.type.id placeholder=Type><md-option ng-value=type.id ng-repeat="type in types">{{type.label}}</md-option></md-select></md-input-container><md-input-container><label>Adresse mail</label><input ng-model=user.mail class=dialog-close></md-input-container><md-input-container><label>Téléphone</label><input ng-model=user.phone class=dialog-close></md-input-container></md-content><div class=md-actions><md-button ng-click=cancel()>Annuler</md-button><md-button class=md-primary ng-click=submit()>Ajouter</md-button></div></md-dialog>';
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
	var nURL = AtlantisUri.Users() + '?api=' + $sessionStorage.api;
	$http.get(nURL).success(function(data, status){
		if(status == 202){
			$scope.users = data;
		}
	});
});
