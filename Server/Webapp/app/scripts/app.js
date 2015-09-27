'use strict';

/**
 * @ngdoc overview
 * @name atlantisWebAppApp
 * @description # atlantisWebAppApp
 * 
 * Main module of the application.
 */
var nApp = angular.module('atlantisWebAppApp', [ 'ngRoute', 'ngMap',
		'chart.js', 'ab-base64', 'ngStorage', 'ngMaterial', 'ngSanitize',
		'ngFileUpload' ]);

var toastPosition = {
	bottom : false,
	top : true,
	left : false,
	right : true
};

nApp.config(function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'views/home.html',
		controller : 'HomeCtrl'
	}).when('/contenu', {
		templateUrl : 'views/contenu.html',
		controller : 'ContenuCtrl'
	}).when('/pharmacie', {
		templateUrl : 'views/pharmacie.html',
		controller : 'PharmacieCtrl'
	}).when('/entretien', {
		templateUrl : 'views/entretien.html',
		controller : 'EntretienCtrl'
	}).when('/devices', {
		templateUrl : 'views/devices.html',
		controller : 'DevicesCtrl'
	}).when('/sensors', {
		templateUrl : 'views/sensors.html',
		controller : 'SensorsCtrl'
	}).when('/geo', {
		templateUrl : 'views/geo.html',
		controller : 'GeoCtrl'
	}).when('/login', {
		templateUrl : 'views/login.html',
		controller : 'LoginCtrl'
	}).when('/music', {
		templateUrl : 'views/music.html',
		controller : 'MusicCtrl'
	}).when('/deconnexion', {
		templateUrl : 'views/deconnexion.html',
		controller : 'DeconnexionCtrl'
	}).when('/settings', {
		templateUrl : 'views/settings.html',
		controller : 'SettingsCtrl'
	}).when('/plantes', {
		templateUrl : 'views/plantes.html',
		controller : 'PlantesCtrl'
	}).when('/history', {
		templateUrl : 'views/history.html',
		controller : 'HistoryCtrl'
	}).when('/scenario', {
		templateUrl : 'views/scenario.html',
		controller : 'ScenarioCtrl'
	}).otherwise({
		redirectTo : '/home'
	});
});

nApp.run(function($rootScope, $location, $sessionStorage) {
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		if ($sessionStorage.api == null) {
			$rootScope.navigation = true;
			$location.path('/login');
		}
	});
});

nApp.filter('firstUpper', function() {
	return function(input, scope) {
		return input ? input.substring(0, 1).toUpperCase()
				+ input.substring(1).toLowerCase() : "";
	}
});

function getDateUnit(day, $window) {
	day = $window.Math.abs(day);
	if (day > 365) {
		return $window.Math.round(day / 365) + ' ans';
	}
	if (day > 30) {
		return $window.Math.round(day / 30) + ' mois';
	}
	return day + ' jours';
}

function getToastPosition() {
	return Object.keys(toastPosition).filter(function(pos) {
		return toastPosition[pos];
	}).join(' ');
}

function showToast($mdToast, status, data) {
	if (status == 202) {
		$mdToast.show($mdToast.simple().content(
				'Modifications effectuées avec succès !').position(
				getToastPosition()).hideDelay(1500));
	} else {
		$mdToast.show($mdToast.simple().content(
				'Erreur : ' + status + ' - ' + data).position(
				getToastPosition()).hideDelay(5000));
	}
}

function showToast($mdToast, message) {
	$mdToast.show($mdToast.simple().content(message).position(
			getToastPosition()).hideDelay(3000));
}