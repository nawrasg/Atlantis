'use strict';

describe('Controller: CuisineAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var CuisineAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CuisineAddCtrl = $controller('CuisineAddCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
