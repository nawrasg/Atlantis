'use strict';

describe('Controller: ModeCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var ModeCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ModeCtrl = $controller('ModeCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ModeCtrl.awesomeThings.length).toBe(3);
  });
});
