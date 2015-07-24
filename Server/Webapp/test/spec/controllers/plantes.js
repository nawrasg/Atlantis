'use strict';

describe('Controller: PlantesCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var PlantesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PlantesCtrl = $controller('PlantesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
