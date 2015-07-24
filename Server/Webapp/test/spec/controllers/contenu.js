'use strict';

describe('Controller: ContenuCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var ContenuCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ContenuCtrl = $controller('ContenuCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
