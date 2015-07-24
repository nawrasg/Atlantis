'use strict';

describe('Service: AtlantisUri', function () {

  // load the service's module
  beforeEach(module('atlantisWebAppApp'));

  // instantiate service
  var AtlantisUri;
  beforeEach(inject(function (_AtlantisUri_) {
    AtlantisUri = _AtlantisUri_;
  }));

  it('should do something', function () {
    expect(!!AtlantisUri).toBe(true);
  });

});
