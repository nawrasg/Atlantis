'use strict';

describe('Controller: PharmacieCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var PharmacieCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PharmacieCtrl = $controller('PharmacieCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
