'use strict';

describe('Controller: SecuritySettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var SecuritySettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SecuritySettingsCtrl = $controller('SecuritySettingsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
