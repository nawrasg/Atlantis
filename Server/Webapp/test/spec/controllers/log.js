'use strict';

describe('Controller: LogCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var LogCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LogCtrl = $controller('LogCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(LogCtrl.awesomeThings.length).toBe(3);
  });
});
