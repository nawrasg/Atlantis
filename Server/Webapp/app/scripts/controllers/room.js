'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:RoomCtrl
 * @description # RoomCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('RoomCtrl', function($scope, $rootScope, $http, $filter, $sessionStorage, $mdDialog, AtlantisUri) {
	$scope.rooms = $sessionStorage.rooms;
	$scope.addRoom = function(room){
		var nURL = AtlantisUri.Rooms() + '?api=' + $sessionStorage.api + '&label=' + room;
		$http.post(nURL).success(function(data, status){
			if(status == 202){
				$scope.rooms.push(data);
				$scope.rooms = $filter('orderBy')($scope.rooms, 'room');
				$scope.item = null;
			}
		});
	};
	$scope.delRoom = function(room){
		var nURL = AtlantisUri.Rooms() + '?api=' + $sessionStorage.api + '&id=' + room.id;
		$http.delete(nURL).success(function(data, status){
			if(status == 202){
				var i = $scope.rooms.indexOf(room);
				$scope.rooms.splice(i, 1);
			}
		});
	};
	$scope.cancel = function(){
		$sessionStorage.rooms = $scope.rooms;
		$mdDialog.cancel();
	};
});
