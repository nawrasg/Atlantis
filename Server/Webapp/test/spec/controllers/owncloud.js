'use strict';

describe('Controller: OwncloudCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var OwncloudCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    OwncloudCtrl = $controller('OwncloudCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(OwncloudCtrl.awesomeThings.length).toBe(3);
  });
});
