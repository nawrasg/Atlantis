'use strict';

describe('Controller: UserAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var UserAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UserAddCtrl = $controller('UserAddCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
