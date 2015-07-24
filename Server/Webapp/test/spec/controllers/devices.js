'use strict';

describe('Controller: DevicesCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var DevicesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DevicesCtrl = $controller('DevicesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
