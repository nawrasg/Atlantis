'use strict';

describe('Controller: DeconnexionCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var DeconnexionCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DeconnexionCtrl = $controller('DeconnexionCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
