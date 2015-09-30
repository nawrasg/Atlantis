'use strict';

/**
 * @ngdoc service
 * @name atlantisWebAppApp.AtlantisUri
 * @description # AtlantisUri Factory in the atlantisWebAppApp.
 */
nApp.factory('AtlantisUri', function() {
	var mIP = '';
	
	return {
		Courses : function(){return mIP + 'backend/at_courses.php';},
		Cuisine : function() {return mIP + 'backend/at_cuisine.php';},
		Devices : function(){return mIP + 'backend/at_ccdevices.php';},
		Ean : function(){return mIP + 'backend/at_ean.php';},
		Entretien : function(){return mIP + 'backend/at_entretien.php';},
		GCM : function(){return mIP + 'backend/at_gcm.php';},
		Geo : function(){return mIP + 'backend/at_geo.php';},
		History : function(){return mIP + 'backend/at_history.php';},
		Home : function(){return mIP + 'backend/at_home.php';},
		Lights : function(){return mIP + 'backend/at_lights.php';},
		Login : function(){return mIP + 'backend/at_auth.php';},
		Music : function(){return mIP + 'backend/at_music.php';},
		Pharmacie : function(){return mIP + 'backend/at_pharmacie.php';},
		Plantes : function(){return mIP + 'backend/at_plants.php';},
		Notify : function(){return mIP + 'backend/at_notify.php';},
		Rooms : function(){return mIP + 'backend/at_rooms.php';},
		Scenarios : function(){return mIP + 'backend/at_scenario.php';},
		Sensors : function(){return mIP + 'backend/at_sensors.php';},
		Speech : function(){return mIP + 'backend/at_speech.php';},
		Settings : function(){return mIP + 'backend/at_settings.php';},
		Users : function(){return mIP + 'backend/at_users.php';},
	};
});
