'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ScenarioCtrl
 * @description # ScenarioCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ScenarioCtrl', function($scope, $window, $http, $filter, $mdToast, $sessionStorage, AtlantisUri) {
	var reservedScenarios = [{id:'alarm', description:'Ce scénario est exécuté lorsque l\'alarme est déclenchée.'},
	                         {id:'mode_day', description:'Ce scénario est exécuté lorsque le mode jour est activé.'},
	                         {id:'mode_night', description:'Ce scénario est exécuté lorsque le mode nuit est activé.'},
	                         {id:'mode_away', description:'Ce scénario est exécuté lorsque le mode absent est activé.'}];
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
	getToolbox();
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
		var filtered = $filter('filter')(reservedScenarios, {id:$scope.newScenario});
		if(filtered.length == 1 && filtered[0].id == $scope.newScenario){
			$scope.scenario_description = filtered[0].description; 
		}else{
			$scope.scenario_description = null;
		}
	};
	$scope.loadScenario = function(scenario){
		if(scenario != null){
			$scope.newScenario = scenario.file;
			if(scenario.xml !== ''){
				clear();
				var xml = Blockly.Xml.textToDom(scenario.xml);
				Blockly.Xml.domToWorkspace( Blockly.mainWorkspace, xml );			
			}else{
				showToast($mdToast, 'Impossible de charger le script !');
			}			
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
	$scope.loadToolbox = function(section){
		var tool = '<xml id="toolbox" style="display: none">';
		switch(section){
		case 'atlantis':
			tool += '<block type="at_light"></block><block type="at_light_status"></block><block type="at_switch"></block><block type="at_music"></block><block type="at_gcm"></block><block type="at_mail"></block><block type="at_alarm_status"></block><block type="at_alarm"></block><block type="at_time"></block>';
			break;
		case 'logic':
			tool += '<block type="controls_if"></block><block type="logic_compare"></block><block type="logic_operation"></block><block type="logic_negate"></block><block type="logic_boolean"></block><block type="logic_null"></block><block type="logic_ternary"></block>';
			break;
		case 'control':
			tool += '<block type="controls_if"></block><block type="controls_repeat_ext"></block><block type="logic_compare"></block><block type="math_number"></block><block type="math_arithmetic"></block><block type="text"></block><block type="text_print"></block><block type="at_sleep"></block>';
			break;
		}
		tool += '</xml>';
		workspace.updateToolbox(tool);
	};
	function clear(){
		Blockly.mainWorkspace.clear();
	}
	function get(){
		var nURL = AtlantisUri.Scenarios() + '?api=' + $sessionStorage.api;
		$http.get(nURL).success(function(data, status){
			if(status == 202){
				$scope.scenarios = data;
			}
		});
	}
	function getToolbox(){
		$http.get(AtlantisUri.Lights() + '?api=' + $sessionStorage.api).success(function(data, status){
			if(status == 202){
				$window.lights = data.lights;
				$window.rooms = data.rooms;
			}
		});
		$http.get(AtlantisUri.Sensors() + '?api=' + $sessionStorage.api + '&type=switchBinary').success(function(data, status){
			if(status == 202){
				$window.switches = data;
			}
		});
		$scope.tools = true;			
	}
});
