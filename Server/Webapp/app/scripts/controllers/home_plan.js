'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:HomePlanCtrl
 * @description # HomePlanCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('HomePlanCtrl', function($scope, $sessionStorage, $mdDialog, $mdToast, Upload, $timeout, AtlantisUri) {
	$scope.$watch('files', function () {
		if($scope.files && $scope.files.length){
			var nURL = AtlantisUri.Rooms();
			Upload.upload({
				url: nURL,
				fields: {
					'api': $sessionStorage.api
				},
				file: $scope.files
			}).progress(function (evt) {
				var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
			}).success(function (data, status, headers, config) {
				if(status == 202){
					showToast($mdToast, 'Plan envoyé avec succès !');
					$mdDialog.hide();
				}else{
					showToast($mdToast, status, data);
				}
			});			
		}
    });
	$scope.cancel = function(){
		$mdDialog.cancel();
	}
});
