'use strict';

describe('Controller: GeoCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var GeoCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    GeoCtrl = $controller('GeoCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
