'use strict';

describe('Controller: CameraAddCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var CameraAddCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CameraAddCtrl = $controller('CameraAddCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(CameraAddCtrl.awesomeThings.length).toBe(3);
  });
});
