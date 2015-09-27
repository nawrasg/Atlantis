'use strict';

/**
 * @ngdoc function
 * @name atlantisWebAppApp.controller:ScenarioCtrl
 * @description # ScenarioCtrl Controller of the atlantisWebAppApp
 */

nApp.controller('ScenarioCtrl', function($scope, $window, $http, $sessionStorage, AtlantisUri) {
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
	$scope.save = function(){
		var code = Blockly.PHP.workspaceToCode(workspace);
		var data = {
			api : $sessionStorage.api,
			name : $scope.scenario,
			code : code
		};
		$http.put(AtlantisUri.Scenarios(), data).success(function(data, status){
			console.log(status, data);
		})
	};
});
