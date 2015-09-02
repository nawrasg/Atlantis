'use strict';

/**
 * @ngdoc service
 * @name atlantisWebAppApp.AtlantisUri
 * @description # AtlantisUri Factory in the atlantisWebAppApp.
 */
nApp.factory('AtlantisUri', function() {
	var mIP = '../';
	return {
		Courses : function(){return mIP + 'backend/at_courses.php';},
		Cuisine : function() {return mIP + 'backend/at_cuisine.php';},
		Devices : function(){return mIP + 'backend/at_ccdevices.php';},
		Ean : function(){return mIP + 'backend/at_ean.php';},
		Entretien : function(){return mIP + 'backend/at_entretien.php';},
		Geo : function(){return mIP + 'backend/at_geo.php';},
		Home : function(){return mIP + 'backend/at_home.php';},
		Lights : function(){return mIP + 'backend/at_lights.php';},
		Login : function(){return mIP + 'backend/at_auth.php';},
		Music : function(){return mIP + 'backend/at_music.php';},
		Pharmacie : function(){return mIP + 'backend/at_pharmacie.php';},
		Plantes : function(){return mIP + 'backend/at_plants.php';},
		Rooms : function(){return mIP + 'backend/at_rooms.php';},
		Sensors : function(){return mIP + 'backend/at_sensors.php';},
		Speech : function(){return mIP + 'backend/at_speech.php';},
		Settings : function(){return mIP + 'backend/at_settings.php';},
		Users : function(){return mIP + 'backend/at_users.php';},
	};
});
