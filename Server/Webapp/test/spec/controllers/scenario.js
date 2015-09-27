'use strict';

describe('Controller: ScenarioCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var ScenarioCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ScenarioCtrl = $controller('ScenarioCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
