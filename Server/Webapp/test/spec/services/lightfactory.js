'use strict';

describe('Service: lightFactory', function () {

  // load the service's module
  beforeEach(module('atlantisWebAppApp'));

  // instantiate service
  var lightFactory;
  beforeEach(inject(function (_lightFactory_) {
    lightFactory = _lightFactory_;
  }));

  it('should do something', function () {
    expect(!!lightFactory).toBe(true);
  });

});
