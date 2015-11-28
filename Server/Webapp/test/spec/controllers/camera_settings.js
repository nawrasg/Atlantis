'use strict';

describe('Controller: CameraSettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var CameraSettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CameraSettingsCtrl = $controller('CameraSettingsCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(CameraSettingsCtrl.awesomeThings.length).toBe(3);
  });
});
