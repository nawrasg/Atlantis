'use strict';

describe('Controller: PharmacieAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var PharmacieAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PharmacieAddCtrl = $controller('PharmacieAddCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
