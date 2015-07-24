'use strict';

describe('Controller: UsersSettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var UsersSettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UsersSettingsCtrl = $controller('UsersSettingsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
