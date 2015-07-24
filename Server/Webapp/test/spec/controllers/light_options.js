'use strict';

describe('Controller: LightOptionsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var LightOptionsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LightOptionsCtrl = $controller('LightOptionsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
