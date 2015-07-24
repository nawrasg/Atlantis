'use strict';

describe('Controller: EntretienAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var EntretienAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EntretienAddCtrl = $controller('EntretienAddCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
