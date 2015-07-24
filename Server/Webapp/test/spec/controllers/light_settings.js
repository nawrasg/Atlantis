'use strict';

describe('Controller: LightSettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var LightSettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LightSettingsCtrl = $controller('LightSettingsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
