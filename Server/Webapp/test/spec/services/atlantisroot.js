'use strict';

describe('Service: AtlantisRoot', function () {

  // load the service's module
  beforeEach(module('atlantisWebAppApp'));

  // instantiate service
  var AtlantisRoot;
  beforeEach(inject(function (_AtlantisRoot_) {
    AtlantisRoot = _AtlantisRoot_;
  }));

  it('should do something', function () {
    expect(!!AtlantisRoot).toBe(true);
  });

});
