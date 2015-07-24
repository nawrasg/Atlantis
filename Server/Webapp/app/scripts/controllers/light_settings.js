'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:LightSettingsCtrl
 * @description # LightSettingsCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('LightSettingsCtrl', function($scope, $filter, $rootScope, $element, close, light) {
	$scope.light = light;
	var room = $filter('filter')($rootScope.rooms, {id:light.room});
	$scope.room = room[0];
	console.log(room);
	$scope.close = function() {
		close({
			light : $scope.light
		}, 200); // close, but give 500ms for bootstrap to animate
	};

	// This cancel function must use the bootstrap, 'modal' function because
	// the doesn't have the 'data-dismiss' attribute.
	$scope.cancel = function() {

		// Manually hide the modal.
		$element.modal('hide');

		// Now call close, returning control to the caller.
		close({
			light : $scope.light
		}, 200); // close, but give 500ms for bootstrap to animate
	};
});
