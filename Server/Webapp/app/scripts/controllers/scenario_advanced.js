'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ScenarioAdvancedCtrl
 * @description # ScenarioAdvancedCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ScenarioAdvancedCtrl', function($scope, $http, $filter, $sessionStorage, $mdToast, AtlantisUri) {
	var reservedScenarios = [{id:'alarm', description:'Ce scénario est exécuté lorsque l\'alarme est déclenchée.'},
	                         {id:'mode_day', description:'Ce scénario est exécuté lorsque le mode jour est activé.'},
	                         {id:'mode_night', description:'Ce scénario est exécuté lorsque le mode nuit est activé.'},
	                         {id:'mode_away', description:'Ce scénario est exécuté lorsque le mode absent est activé.'}];
	get();
	$scope.aceLoaded = function(editor){
		editor.setTheme("ace/theme/twilight");
        editor.getSession().setMode("ace/mode/php");
        $scope.editor = editor;
        clear();
	};
	$scope.aceChanged = function(e){
		
	};
	$scope.loadScenario = function(scenario){
		if(scenario != null){
			$scope.newScenario = scenario.file;
			$scope.editor.setValue(scenario.php);						
		}
	};
	$scope.new = function(){
		$scope.newScenario = '';
		$scope.scenario = null;
		clear();
	};
	$scope.delete = function(){
		var scenario = $scope.scenario;
		if(scenario != null && scenario.file != ''){
			var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api + '&scenario=' + scenario.file;
			$http.delete(nURL).success(function(data, status){
				if(status == 202){
					$scope.newScenario = null;
					$scope.scenario = null;
					var i = $scope.scenarios.indexOf(scenario);
					$scope.scenarios.splice(i, 1);
					clear();
					showToast($mdToast, 'Scénario effacé avec succès !');
				}else{
					showToast($mdToast, 'Un problème est survenu !');
				}
			});			
		}
	};
	$scope.updateTitle = function(){
		var filtered = $filter('filter')(reservedScenarios, {id:$scope.newScenario});
		if(filtered.length == 1 && filtered[0].id == $scope.newScenario){
			$scope.scenario_description = filtered[0].description; 
		}else{
			$scope.scenario_description = null;
		}
	};
	function clear(){
		$scope.editor.setValue('<?php\n');
	}
	function get(){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.scenarios = $filter('filter')(data, {xml:' '}, true);
			}
		});
	}
});
