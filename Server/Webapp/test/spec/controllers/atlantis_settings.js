'use strict';

describe('Controller: AtlantisSettingsCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var AtlantisSettingsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AtlantisSettingsCtrl = $controller('AtlantisSettingsCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
