'use strict';

describe('Controller: DevicesAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var DevicesAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DevicesAddCtrl = $controller('DevicesAddCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
