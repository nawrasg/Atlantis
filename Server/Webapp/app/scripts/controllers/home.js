'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:HomeCtrl
 * @description # HomeCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('HomeCtrl', function($scope, $rootScope, $http, $sessionStorage, $filter, $mdDialog, $mdToast, $window, AtlantisUri) {
	get();
	getCourses();
	$scope.toggleAlarm = function(){
		var nURL = AtlantisUri.Home() + '?api=' + $sessionStorage.api + '&alarm=' + $scope.alarm;
		$http.put(nURL).success(function(data, status){
			if(status == 202){
				showToast($mdToast, 'Alarme activée !');
			}
		});
	};
	$scope.addCourse = function(item){
		var nURL = AtlantisUri.Courses() + '?api=' + $sessionStorage.api;
		if($scope.item != null){
			var data = $scope.item.split(',');
			nURL += '&name=' + data[0];
			if(data.length == 2){
				nURL += '&quantity=' + parseInt(data[1]);
			}
			$http.post(nURL).success(function(data, status){
				if(status == 202){
					getCourses();
					$scope.item = null;				
				}
			});			
		}
	};
	$scope.notifyCourses = function(){
		var nURL = AtlantisUri.Notify() + '?api=' + $sessionStorage.api;
		nURL += '&msg=Liste de courses modifiée !';
		$http.post(nURL).success(function(data, status){
			showToast($mdToast, 'Notification envoyée !');
		});
	};
	$scope.modifyCourse = function(item, method){
		var nURL = AtlantisUri.Courses() + '?api=' + $sessionStorage.api +'&id=' + item.id;
		switch(method){
		case '+':
			nURL += '&quantity=' + (parseInt(item.quantity) + 1);
			$http.put(nURL).success(function(data, status){
				console.log(status);
				console.log(data);
				if(status == 202){
					item.quantity = parseInt(item.quantity) + 1;
				}
			});
			break;
		case '-':
			nURL += '&quantity=' + (parseInt(item.quantity) - 1);
			$http.put(nURL).success(function(data, status){
				if(status == 202){
					item.quantity = parseInt(item.quantity) - 1;
				}
			});
			break;
		case '.':
			$http.delete(nURL).success(function(data, status){
				if(status == 202){
					var i = $scope.courses.indexOf(item);
					$scope.courses.splice(i, 1);
				}
			});
			break;
		}
	};
	$scope.editHome = function(e){
		$mdDialog.show({
			templateUrl: 'views/room.html',
			controller: 'RoomCtrl'
		});
	};
	$scope.planHome = function(e){
		$mdDialog.show({
			templateUrl: 'views/home_plan.html',
			controller: 'HomePlanCtrl'
		}).then(function(){
			$window.location.reload();
		});
	};
	function getCourses(){
		var nURL = AtlantisUri.Courses() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.courses = data;
			}
		});
	}
	function get(){
		var nURL = AtlantisUri.Home() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			$scope.alarm = data.alarm;
			$sessionStorage.rooms = data.rooms;
			$scope.weather = [];
			$scope.day1 = getWeatherIcon(data.weather[0].code);
			$scope.day2 = getWeatherIcon(data.weather[1].code);
			$scope.meteo1 = $filter('firstUpper')(data.weather[0].description) + ' ' + data.weather[0].temperature + '°';
			$scope.meteo2 = $filter('firstUpper')(data.weather[1].description) + ' ' + data.weather[1].temperature + '°';
		});
	}
	function getWeatherIcon(code){
		var nCode = $filter('limitTo')(code, code.length - 1);
		switch(nCode){
		case '01':
			return 'images/weather/ng_weather_sun.png';
		case '02':
		case '03':
		case '04':
		case '11':
			return 'images/weather/ng_weather_cloud.png';
		case '09':
		case '10':
			return 'images/weather/ng_weather_heavy.png';
		case '13':
			return 'images/weather/ng_weather_snow.png';
		case '50':
			return 'images/weather/ng_weather_fog.png';
		}
	}
});
