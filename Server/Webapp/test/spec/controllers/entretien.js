'use strict';

describe('Controller: EntretienCtrl', function () {

  // load the controller's module
  beforeEach(module('atlantisWebAppApp'));

  var EntretienCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EntretienCtrl = $controller('EntretienCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
