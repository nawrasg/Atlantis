'use strict';

describe('Controller: CamerasCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var CamerasCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CamerasCtrl = $controller('CamerasCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
