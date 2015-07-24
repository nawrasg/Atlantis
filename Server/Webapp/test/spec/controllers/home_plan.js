'use strict';

describe('Controller: HomePlanCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var HomePlanCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    HomePlanCtrl = $controller('HomePlanCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
