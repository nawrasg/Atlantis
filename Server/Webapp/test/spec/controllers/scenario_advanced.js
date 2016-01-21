'use strict';

describe('Controller: ScenarioAdvancedCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var ScenarioAdvancedCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ScenarioAdvancedCtrl = $controller('ScenarioAdvancedCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ScenarioAdvancedCtrl.awesomeThings.length).toBe(3);
  });
});
