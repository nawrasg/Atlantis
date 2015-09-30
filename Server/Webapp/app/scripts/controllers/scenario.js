'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ScenarioCtrl
 * @description # ScenarioCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ScenarioCtrl', function($scope, $window, $http, $mdToast, $sessionStorage, AtlantisUri) {
	var blocklyArea = document.getElementById('blocklyArea');
	var blocklyDiv = document.getElementById('blocklyDiv');
	var workspace = Blockly.inject(blocklyDiv, {
		toolbox : document.getElementById('toolbox')
	});
	var onresize = function(e) {
		// Compute the absolute coordinates and dimensions of blocklyArea.
		var element = blocklyArea;
		var x = 0;
		var y = 0;
		do {
			x += element.offsetLeft;
			y += element.offsetTop;
			element = element.offsetParent;
		} while (element);
		// Position blocklyDiv over blocklyArea.
		blocklyDiv.style.left = x + 'px';
		blocklyDiv.style.top = y + 'px';
		blocklyDiv.style.width = blocklyArea.offsetWidth + 'px';
		blocklyDiv.style.height = blocklyArea.offsetHeight + 'px';
	};
	$window.addEventListener('resize', onresize, false);
	onresize();
	get();
	$scope.save = function(){
		var code = Blockly.PHP.workspaceToCode(workspace);
		var xml = Blockly.Xml.workspaceToDom( Blockly.mainWorkspace );
		var xml_string = Blockly.Xml.domToText( xml );
		var data = {
			api : $sessionStorage.api,
			name : $scope.newScenario,
			code : code,
			xml : xml_string
		};
		$http.put(AtlantisUri.Scenarios(), data).success(function(data, status){
			if(status == 202){
				showToast($mdToast, 'Scénario enregistré avec succès !');
				get();
			}else{
				showToast($mdToast, 'Un problème est survenu !');
			}
		})
	};
	$scope.updateTitle = function(){
		$scope.newScenario = encodeURIComponent($scope.newScenario);
	};
	$scope.loadScenario = function(scenario){
		if(scenario != null){
			$scope.newScenario = scenario.file;
			if(scenario.xml !== ''){
				var xml = Blockly.Xml.textToDom(scenario.xml);
				Blockly.Xml.domToWorkspace( Blockly.mainWorkspace, xml );			
			}else{
				showToast($mdToast, 'Impossible de charger le script !');
			}			
		}
	};
	$scope.new = function(){
		$scope.newScenario = '';
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
	function clear(){
		var xml = Blockly.Xml.textToDom('<xml xmlns="http://www.w3.org/1999/xhtml"></xml>');
		Blockly.Xml.domToWorkspace( Blockly.mainWorkspace, xml );
	}
	function get(){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.scenarios = data;
			}
		});
	}
});
