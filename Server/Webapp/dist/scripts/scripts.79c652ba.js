"use strict";function getDateUnit(a,b){return a=b.Math.abs(a),a>365?b.Math.round(a/365)+" ans":a>30?b.Math.round(a/30)+" mois":a+" jours"}function getToastPosition(){return Object.keys(toastPosition).filter(function(a){return toastPosition[a]}).join(" ")}function showToast(a,b,c){202==b?a.show(a.simple().content("Modifications effectuées avec succès !").position(getToastPosition()).hideDelay(1500)):a.show(a.simple().content("Erreur : "+b+" - "+c).position(getToastPosition()).hideDelay(5e3))}function showToast(a,b){a.show(a.simple().content(b).position(getToastPosition()).hideDelay(3e3))}var nApp=angular.module("atlantisWebAppApp",["ngRoute","ngMap","chart.js","ab-base64","ngStorage","ngMaterial","ngSanitize","ngFileUpload"]),toastPosition={bottom:!1,top:!0,left:!1,right:!0};nApp.config(["$routeProvider",function(a){a.when("/home",{templateUrl:"views/home.html",controller:"HomeCtrl"}).when("/contenu",{templateUrl:"views/contenu.html",controller:"ContenuCtrl"}).when("/pharmacie",{templateUrl:"views/pharmacie.html",controller:"PharmacieCtrl"}).when("/entretien",{templateUrl:"views/entretien.html",controller:"EntretienCtrl"}).when("/devices",{templateUrl:"views/devices.html",controller:"DevicesCtrl"}).when("/sensors",{templateUrl:"views/sensors.html",controller:"SensorsCtrl"}).when("/geo",{templateUrl:"views/geo.html",controller:"GeoCtrl"}).when("/login",{templateUrl:"views/login.html",controller:"LoginCtrl"}).when("/music",{templateUrl:"views/music.html",controller:"MusicCtrl"}).when("/deconnexion",{templateUrl:"views/deconnexion.html",controller:"DeconnexionCtrl"}).when("/settings",{templateUrl:"views/settings.html",controller:"SettingsCtrl"}).when("/plantes",{templateUrl:"views/plantes.html",controller:"PlantesCtrl"}).when("/history",{templateUrl:"views/history.html",controller:"HistoryCtrl"}).otherwise({redirectTo:"/home"})}]),nApp.run(["$rootScope","$location","$sessionStorage",function(a,b,c){a.$on("$routeChangeStart",function(d,e,f){null==c.api&&(a.navigation=!0,b.path("/login"))})}]),nApp.filter("firstUpper",function(){return function(a,b){return a?a.substring(0,1).toUpperCase()+a.substring(1).toLowerCase():""}}),angular.module("atlantisWebAppApp").controller("MainCtrl",["$scope",function(a){a.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}]),nApp.controller("ContenuCtrl",["$scope","$http","$window","$sessionStorage","$filter","$mdDialog","AtlantisUri",function(a,b,c,d,e,f,g){function h(){var c=g.Cuisine()+"?api="+d.api;b.get(c).success(function(b,c){a.data=b,a.frigos=e("filter")(b,{endroit:"frigidaire"}),a.congelos=e("filter")(b,{endroit:"congelateur"}),a.placards=e("filter")(b,{endroit:"placard"})})}function i(){a.frigos=e("filter")(a.data,{endroit:"frigidaire"}),a.congelos=e("filter")(a.data,{endroit:"congelateur"}),a.placards=e("filter")(a.data,{endroit:"placard"})}h(),a.add=function(b){f.show({templateUrl:"views/cuisine_add.html",targetEvent:b,controller:"CuisineAddCtrl"}).then(function(b){a.data.push(b),a.data=e("orderBy")(a.data,"peremption"),i()},function(){})},a.filter=function(b){if(null==b||""==b)i();else{var c=[];angular.forEach(a.data,function(a){e("lowercase")(a.label).indexOf(e("lowercase")(b))>-1&&c.push(a)}),a.frigos=e("filter")(c,{endroit:"frigidaire"}),a.congelos=e("filter")(c,{endroit:"congelateur"}),a.placards=e("filter")(c,{endroit:"placard"})}},a.open=function(a){var c=g.Cuisine()+"?api="+d.api+"&open="+a.id;b.put(c).success(function(b,c){202==c&&(a.status=1)})},a.ignore=function(a){var c=g.Cuisine()+"?api="+d.api+"&ignore="+a.id;b.put(c).success(function(b,c){202==c&&(a.ignore=1)})},a.modify=function(c,e){switch(e){case"+":var f=g.Cuisine()+"?api="+d.api;f+="&id="+c.id+"&quantite="+(parseInt(c.quantite)+1),b.put(f).success(function(a,b){202==b&&(c.quantite=parseInt(c.quantite)+1)});break;case"-":var f=g.Cuisine()+"?api="+d.api;f+="&id="+c.id+"&quantite="+(parseInt(c.quantite)-1)+"&close=true",b.put(f).success(function(a,b){202==b&&(c.quantite=parseInt(c.quantite)-1,c.status=0)});break;case".":var f=g.Cuisine()+"?api="+d.api+"&id="+c.id;b["delete"](f).success(function(b,d){var e=a.data.indexOf(c);a.data.splice(e,1),i()})}},a.status=function(a,b,c){return"congelateur"==c?"images/ng_ball_blue.png":0>a?"images/ng_ball_red.png":1==b?"images/ng_ball_orange.png":"frigidaire"==c?"images/ng_ball_green.png":"images/ng_empty.png"},a.peremption=function(a){return"congelateur"==a.place?"Congelé depuis "+getDateUnit(a.peremption,c):a.peremption<0?"Périmé depuis "+getDateUnit(a.peremption,c):a.ouvert<0?"Ouvert depuis "+getDateUnit(a.ouvert,c):"A consommer dans "+getDateUnit(a.peremption,c)}}]);var nApp=angular.module("atlantisWebAppApp");nApp.controller("PharmacieCtrl",["$scope","$sessionStorage","$http","$window","$mdDialog","$filter","AtlantisUri",function(a,b,c,d,e,f,g){function h(){var d=g.Pharmacie()+"?api="+b.api;c.get(d).success(function(b,c){202==c&&(a.pharmacies=b,a.data=b)})}h(),a.add=function(b){e.show({templateUrl:"views/pharmacie_add.html",targetEvent:b,controller:"PharmacieAddCtrl"}).then(function(b){a.data.push(b),a.data=f("orderBy")(a.data,"peremption"),a.pharmacies=a.data})},a.filter=function(b){if(null==b||""==b)a.pharmacies=a.data;else{var c=[];angular.forEach(a.data,function(a){f("lowercase")(a.nom).indexOf(f("lowercase")(b))>-1&&c.push(a)}),a.pharmacies=c}},a.modify=function(d,e){switch(e){case"+":var f=g.Pharmacie()+"?api="+b.api;f+="&id="+d.id+"&qte="+(parseInt(d.quantite)+1),c.put(f).success(function(a,b){202==b&&(d.quantite=parseInt(d.quantite)+1)});break;case"-":var h=g.Pharmacie()+"?api="+b.api;h+="&id="+d.id+"&qte="+(parseInt(d.quantite)-1),c.put(h).success(function(a,b){202==b&&(d.quantite=parseInt(d.quantite)-1)});break;case".":var i=g.Pharmacie()+"?api="+b.api;i+="&id="+d.id,c["delete"](i).success(function(b,c){if(202==c){var e=a.data.indexOf(d);a.data.splice(e,1),a.pharmacies=a.data}})}},a.peremption=function(a){return 0>a?"Périmé depuis "+getDateUnit(a,d):"Valable encore "+getDateUnit(a,d)},a.status=function(a){return 0>a?"images/ng_ball_red.png":"images/ng_empty.png"}}]);var nApp=angular.module("atlantisWebAppApp");nApp.controller("EntretienCtrl",["$scope","$sessionStorage","$http","$window","$mdDialog","$filter","AtlantisUri",function(a,b,c,d,e,f,g){function h(){var d=g.Entretien()+"?api="+b.api;c.get(d).success(function(b,c){202==c&&(a.data=b,a.entretiens=b)})}h(),a.add=function(b){e.show({templateUrl:"views/entretien_add.html",targetEvent:b,controller:"EntretienAddCtrl"}).then(function(b){a.data.push(b),a.data=f("orderBy")(a.data,"peremption"),a.entretiens=a.data})},a.filter=function(b){if(null==b||""==b)a.entretiens=a.data;else{var c=[];angular.forEach(a.data,function(a){f("lowercase")(a.nom).indexOf(f("lowercase")(b))>-1&&c.push(a)}),a.entretiens=c}},a.modify=function(d,e){switch(e){case"+":var f=g.Entretien()+"?api="+b.api;f+="&id="+d.id+"&qte="+(parseInt(d.quantite)+1),c.put(f).success(function(a,b){202==b&&(d.quantite=parseInt(d.quantite)+1)});break;case"-":var f=g.Entretien()+"?api="+b.api;f+="&id="+d.id+"&qte="+(parseInt(d.quantite)-1),c.put(f).success(function(a,b){202==b&&(d.quantite=parseInt(d.quantite)-1)});break;case".":var f=g.Entretien()+"?api="+b.api;f+="&id="+d.id,c["delete"](f).success(function(b,c){if(202==c){var e=a.data.indexOf(d);a.data.splice(e,1),a.entretiens=a.data}})}},a.peremption=function(a){return 0>a?"Périmé depuis "+getDateUnit(a,d):"Valable encore "+getDateUnit(a,d)},a.status=function(a){return 0>a?"images/ng_ball_red.png":"images/ng_empty.png"}}]),nApp.controller("DevicesCtrl",["$scope","$http","$filter","$sessionStorage","$mdDialog","AtlantisUri",function(a,b,c,d,e,f){function g(){if(null==d.devices){e.show({templateUrl:"views/wait.html"});var c=f.Devices()+"?api="+d.api;b.get(c).success(function(b,c){a.devices=b.devices,a.data=b.devices,a.users=b.users,e.hide(),d.devices=b.devices,d.users=b.users})}else a.devices=d.devices,a.data=d.devices,a.users=d.users}g(),a.add=function(b){e.show({templateUrl:"views/devices_add.html",targetEvent:b,controller:"DevicesAddCtrl",locals:{users:a.users,device:null}}).then(function(b){a.data.push(b),a.data=c("orderBy")(a.data,"nom"),a.devices=a.data},function(){})},a.ping=function(a){var c=f.Devices()+"?api="+d.api+"&ping="+a.ip;b.get(c).success(function(b,c){202==c&&(a.online=b.online)})},a.del=function(c){var e=f.Devices()+"?api="+d.api;e+="&id="+c.id,b["delete"](e).success(function(b,d){if(202==d){var e=a.data.indexOf(c);a.data.splice(e,1),a.devices=a.data}})},a.edit=function(b,c){e.show({templateUrl:"views/devices_add.html",targetEvent:c,controller:"DevicesAddCtrl",locals:{users:a.users,device:b}}).then(function(a){b.nom=a.nom,b.ip=a.ip,b.mac=a.mac,b.type=a.type,b.connexion=a.connexion,b.username=a.username},function(){})},a.filter=function(b){if(null==b||""==b)a.devices=a.data;else{var d=[];angular.forEach(a.data,function(a){(c("lowercase")(a.nom).indexOf(c("lowercase")(b))>-1||c("lowercase")(a.ip).indexOf(c("lowercase")(b))>-1)&&d.push(a)}),a.devices=d}},a.connection=function(a){switch(a){case"wifi":return"images/ng_wifi.png";case"ethernet":return"images/ng_ethernet.png"}},a.status=function(a){switch(a){case 1:return"images/ng_ball_green.png";case-1:return"images/ng_ball_red.png"}},a.type=function(a){switch(a){case"smartphone":return"url(images/ng_smartphone.png)";case"linux":return"url(images/ng_server.png)";case"windows":return"url(images/ng_windows.png)";case"imprimante":return"url(images/ng_printer.png)";default:return"url(images/ng_device.png)"}}}]);var nApp=angular.module("atlantisWebAppApp");nApp.controller("SensorsCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","$rootScope","AtlantisUri",function(a,b,c,d,e,f,g){function h(a,b){var d=g.Lights()+"?api="+c.api;b.get(d).success(function(b,c){a.lights=b.lights})}function i(a,b){var d=g.Sensors()+"?api="+c.api+"&action=get";b.get(d).success(function(b,c){a.capteurs=b.devices,f.rooms=b.rooms})}function j(a,b){var b=d("filter")(a.rooms,{id:b});return 1==b.length?b[0].room:!1}h(a,b),i(a,b);var k={from:0,to:10,step:1};a.options=k,a.reachable=function(a){return a?"images/ng_empty.png":"images/ng_ball_red.png"},a.changeBright=function(a){var d=g.Lights()+"?api="+c.api;d+="&bri="+a.uid+"&protocol="+a.protocol+"&value="+a.brightness,b.put(d).success(function(a,b){})},a.toggleLight=function(a){var d=g.Lights()+"?api="+c.api;d+="&on="+a.uid+"&protocol="+a.protocol+"&value="+a.on,b.put(d).success(function(a,b,c,d){})},a.getRoomLabel=function(a){if(null!=a.room&&""!=a.room){var b=d("filter")(f.rooms,{id:a.room});if(null!=b&&1==b.length)return"("+b[0].room+")"}},a.editLight=function(b,c){e.show({templateUrl:"views/light_settings.html",targetEvent:c,controller:"LightSettingsCtrl",locals:{light:b}}).then(function(c){if(null!=c)if(null==c.result)b=c;else if("delete"===c.result){var d=a.lights.indexOf(b);a.lights.splice(d,1)}})},a.editLights=function(c){e.show({templateUrl:"views/lights_settings.html",targetEvent:c,controller:"LightsSettingsCtrl"}).then(function(c){null!=c&&202==c.code&&h(a,b)})},a.sensorPic=function(a){switch(a){case"section":return"images/ng_empty.png";case"Battery":return"images/ng_battery.png";case"Tamper":return"images/ng_alert.png";case"Temperature":return"images/ng_thermostat.png";case"Luminiscence":return"images/ng_sun.png";case"Power":case"Electric":return"images/ng_lightning.png";case"switchBinary":return"images/ng_switch.png";default:return"images/ng_device.png"}},a.sensorDesc=function(b){switch(b.type){case"section":return d("uppercase")(null==b.alias?b.device:b.alias)+(null==b.room||""==b.room?"":" ("+j(a,b.room)+")");case"Battery":return"Autonomie : "+b.value+" %";case"Tamper":return"Capteur trafiqué le "+d("date")(1e3*b.update,"dd/MM/yyyy à HH:mm");case"Door/Window":return"on"==b.value?"Etat : Ouvert":"Etat : Fermé";case"Temperature":return"Température : "+b.value+" "+b.unit;case"Luminiscence":return"Luminosité : "+b.value+" "+b.unit;case"switchBinary":return"on"==b.value?"Allumé":"Eteint";case"General purpose":return"Dernier mouvement détecté le "+d("date")(1e3*b.update,"dd/MM/yyyy à HH:mm");case"sensorBinary":return"Dernier mouvement le "+d("date")(1e3*b.update,"dd/MM/yyyy à HH:mm");default:return b.value+" "+b.unit}},a.toggleSwitch=function(a){var d=g.Sensors()+"?api="+c.api;d+="&toggle="+a.sensor+"&value="+a.value,b.put(d).success(function(a,b){})},a.editSensors=function(c){e.show({templateUrl:"views/sensors_settings.html",targetEvent:c,controller:"SensorsSettingsCtrl"}).then(function(c){null!=c&&202==c.code&&i(a,b)})},a.editSensor=function(a,b){e.show({templateUrl:"views/sensor_settings.html",targetEvent:b,controller:"SensorSettingsCtrl",locals:{sensor:a}}).then(function(a){})}}]),nApp.controller("GeoCtrl",["$scope","$http","$sessionStorage","$mdToast","AtlantisUri",function(a,b,c,d,e){function f(){var d=e.Geo()+"?api="+c.api;b.get(d).success(function(b,c){202==c&&(a.home=b.atlantis,a.devices=b.positions)})}a.admin=0==c.user.type,f(),a.request=function(a){var f=e.Geo()+"?api="+c.api;switch(a){case"p":b.post(f).success(function(a,b){202==b&&showToast(d,"Demande envoyée !")});break;case"s":f+="&secret=oui",b.post(f).success(function(a,b){202==b&&showToast(d,"Demande envoyée !")})}},a.getLocation=function(){f()}}]),nApp.controller("LoginCtrl",["$scope","$http","base64","$sessionStorage","$rootScope","$location","AtlantisUri",function(a,b,c,d,e,f,g){a.auth=function(){var h=a.user.username,i=a.user.password,j=g.Login();b.defaults.headers.common.Authorization=c.encode(h+":"+i),b.get(j).success(function(c,g){202==g?(a.user=null,e.navigation=!1,d.user=c,d.api=c.cle,delete b.defaults.headers.common.Authorization,f.path("/home")):console.log(g,c)})}}]),nApp.controller("MusicCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","AtlantisUri",function(a,b,c,d,e,f){function g(){var e=f.Music()+"?api="+c.api;b.get(e).success(function(b,c){202==c&&(a.on=1==b.on,a.welcome=-1!=b.welcome.id,a.playlists=d("filter")(b.songs,{type:"playlist"}),a.songs=d("filter")(b.songs,{type:"song"}),a.volume=b.vol,a.welcomeSong=b.welcome.id)})}g(),a.types=[{id:0,title:"Tout afficher"},{id:1,title:"Listes de lecture"},{id:2,title:"Morceaux"}],a.type={id:1,title:"Listes de lecture"},a.toggleMusic=function(){var d=f.Music()+"?api="+c.api+"&action=";d+=a.on?"on":"off",b.put(d)},a.action=function(a){var d=f.Music()+"?api="+c.api;switch(a){case"stop":d+="&action=stop",b.put(d).success(function(a,b){});break;case"pause":d+="&action=pause",b.put(d).success(function(a,b){});break;case"repeat":d+="&action=repeat",b.put(d).success(function(a,b){});break;case"previous":d+="&action=previous",b.put(d).success(function(a,b){});break;case"next":d+="&action=next",b.put(d).success(function(a,b){});break;case"shuffle":d+="&action=shuffle",b.put(d).success(function(a,b){});break;case"refresh":b.post(d).success(function(a,b){g()})}},a.editPlaylist=function(a,b){e.show({templateUrl:"views/playlist_settings.html",targetEvent:b,controller:"PlaylistSettingsCtrl",locals:{playlist:a}}).then(function(a){g()})},a.play=function(a){if("song"==a.type){var d=f.Music()+"?api="+c.api+"&action=play&id="+a.id;b.put(d).success(function(a,b){})}else{var d=f.Music()+"?api="+c.api+"&action=playlistplay&playlist="+a.id;b.put(d).success(function(a,b){})}},a.speak=function(){var d=f.Speech()+"?api="+c.api;d+="&action=speaker&text="+encodeURI(a.message),b.get(d).success(function(a,b){})},a.changeVol=function(a){var d=f.Music()+"?api="+c.api;d+="&action=vol&source=1&level="+a,b.put(d).success(function(a,b){})},a.songPic=function(b){switch(b.type){case"playlist":return"images/music/ng_note_double.png";case"song":return b.id==a.welcomeSong?"images/music/ng_note_white.png":"images/music/ng_note.png"}},a.editSong=function(b,c){e.show({templateUrl:"views/song_settings.html",targetEvent:c,controller:"SongSettingsCtrl",locals:{song:b,playlists:a.playlists}}).then(function(){g()})}}]),nApp.controller("HomeCtrl",["$scope","$rootScope","$http","$sessionStorage","$filter","$mdDialog","$mdToast","$window","AtlantisUri",function(a,b,c,d,e,f,g,h,i){function j(){var b=i.Courses()+"?api="+d.api;c.get(b).success(function(b,c){202==c&&(a.courses=b)})}function k(){var b=i.Home()+"?api="+d.api;c.get(b).success(function(b,c){a.alarm=b.alarm,d.rooms=b.rooms,a.weather=[],a.day1=l(b.weather[0].code),a.day2=l(b.weather[1].code),a.meteo1=e("firstUpper")(b.weather[0].description)+" "+b.weather[0].temperature+"°",a.meteo2=e("firstUpper")(b.weather[1].description)+" "+b.weather[1].temperature+"°"})}function l(a){var b=e("limitTo")(a,a.length-1);switch(b){case"01":return"images/weather/ng_weather_sun.png";case"02":case"03":case"04":case"11":return"images/weather/ng_weather_cloud.png";case"09":case"10":return"images/weather/ng_weather_heavy.png";case"13":return"images/weather/ng_weather_snow.png";case"50":return"images/weather/ng_weather_fog.png"}}k(),j(),a.toggleAlarm=function(){var b=i.Home()+"?api="+d.api+"&alarm="+a.alarm;c.put(b).success(function(a,b){console.log(b),console.log(a)})},a.addCourse=function(b){var e=i.Courses()+"?api="+d.api;if(null!=a.item){var f=a.item.split(",");e+="&name="+f[0],2==f.length&&(e+="&quantity="+parseInt(f[1])),c.post(e).success(function(b,c){202==c&&(j(),a.item=null)})}},a.notifyCourses=function(){var a=i.Notify()+"?api="+d.api;a+="&msg=Liste de courses modifiée !",c.post(a).success(function(a,b){showToast(g,"Notification envoyée !")})},a.modifyCourse=function(b,e){var f=i.Courses()+"?api="+d.api+"&id="+b.id;switch(e){case"+":f+="&quantity="+(parseInt(b.quantity)+1),c.put(f).success(function(a,c){console.log(c),console.log(a),202==c&&(b.quantity=parseInt(b.quantity)+1)});break;case"-":f+="&quantity="+(parseInt(b.quantity)-1),c.put(f).success(function(a,c){202==c&&(b.quantity=parseInt(b.quantity)-1)});break;case".":c["delete"](f).success(function(c,d){if(202==d){var e=a.courses.indexOf(b);a.courses.splice(e,1)}})}},a.editHome=function(a){f.show({templateUrl:"views/room.html",controller:"RoomCtrl"})},a.planHome=function(a){f.show({templateUrl:"views/home_plan.html",controller:"HomePlanCtrl"}).then(function(){h.location.reload()})}}]),nApp.controller("LightSettingsCtrl",["$scope","$rootScope","$mdDialog","$http","$sessionStorage","AtlantisUri","light",function(a,b,c,d,e,f,g){a.cancel=function(){c.hide()},a.save=function(){var b=f.Lights()+"?api="+e.api;b+="&set="+a.light.id+"&room="+a.light.room+"&name="+a.light.name+"&uid="+a.light.uid,d.put(b).success(function(b,d){202==d&&c.hide(a.light)})},a.changeColor=function(b){var c=f.Lights()+"?api="+e.api;c+="&color="+a.light.uid+"&value="+b,d.put(c)},a["delete"]=function(){var b=f.Lights()+"?api="+e.api;b+="&id="+a.light.id,d["delete"](b).success(function(a,b){202==b?c.hide({result:"delete"}):console.log(b,a)})},a.rooms=b.rooms,a.light=g,0==e.user.type&&(a.admin=!0)}]),nApp.controller("DeconnexionCtrl",["$scope","$sessionStorage","$rootScope","$location",function(a,b,c,d){delete b.api,c.navigation=!0,d.path("/login")}]),nApp.controller("SettingsCtrl",["$scope","$mdDialog",function(a,b){function c(){var b=[];b.push({id:0,label:"Administrateur"}),b.push({id:1,label:"Utilisateur"}),b.push({id:2,label:"Visiteur"}),a.types=b}c()}]),nApp.factory("AtlantisUri",["$sessionStorage",function(a){var b="";return{Courses:function(){return b+"backend/at_courses.php"},Cuisine:function(){return b+"backend/at_cuisine.php"},Devices:function(){return b+"backend/at_ccdevices.php"},Ean:function(){return b+"backend/at_ean.php"},Entretien:function(){return b+"backend/at_entretien.php"},GCM:function(){return b+"backend/at_gcm.php"},Geo:function(){return b+"backend/at_geo.php"},History:function(){return b+"backend/at_history.php"},Home:function(){return b+"backend/at_home.php"},Lights:function(){return b+"backend/at_lights.php"},Login:function(){return b+"backend/at_auth.php"},Music:function(){return b+"backend/at_music.php"},Pharmacie:function(){return b+"backend/at_pharmacie.php"},Plantes:function(){return b+"backend/at_plants.php"},Notify:function(){return b+"backend/at_notify.php"},Rooms:function(){return b+"backend/at_rooms.php"},Sensors:function(){return b+"backend/at_sensors.php"},Speech:function(){return b+"backend/at_speech.php"},Settings:function(){return b+"backend/at_settings.php"},Users:function(){return b+"backend/at_users.php"}}}]),nApp.controller("UserAddCtrl",["$scope","$http","$sessionStorage","$mdDialog","$mdToast","AtlantisUri","user",function(a,b,c,d,e,f,g){function h(){var h=f.Users()+"?api="+c.api;null==g?(h+="&name="+a.user.name+"&type="+a.user.type.id+"&pwd="+a.user.pwd,b.post(h).success(function(a,b){202==b?d.hide(a):showToast(e,b,a)})):(h+="&id="+g.id+"&type="+a.user.type.id+"&mail="+a.user.mail+"&phone="+a.user.phone,null!=a.user.pwd&&""!=a.user.pwd&&(h+="&pwd="+a.user.pwd),b.put(h).success(function(b,c){showToast(e,c,b),202==c&&(g.mail=a.user.mail,g.phone=a.user.phone,g.type=a.user.type.id,d.hide(g))}))}a.types=[{id:0,label:"Administrateur"},{id:1,label:"Utilisateur"},{id:2,label:"Visiteur"}],null!=g?(a.user={name:g.nom,phone:g.phone,mail:g.mail,type:{id:g.type}},a.btnSubmit="Modifiez"):a.btnSubmit="Ajoutez",a.submit=function(){h()},a.cancel=function(){d.cancel()}}]),nApp.controller("UsersSettingsCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","AtlantisUri",function(a,b,c,d,e,f){a.addUser=function(b){e.show({templateUrl:"views/user_add.html",targetEvent:b,controller:"UserAddCtrl",locals:{user:null}}).then(function(b){null!=b&&a.users.push(b)},function(){})},a.changeAPI=function(a){var d=f.Users()+"?api="+c.api+"&cle="+a.id;b.put(d).success(function(b,c){202==c&&(a.cle=b)})},a.editUser=function(a,b){e.show({templateUrl:"views/user_add.html",targetEvent:b,controller:"UserAddCtrl",locals:{user:a}}).then(function(b){a=b},function(){})},a.delUser=function(d){var e=f.Users()+"?api="+c.api+"&id="+d.id;b["delete"](e).success(function(b,c){if(202==c){var e=a.users.indexOf(d);a.users.splice(e,1)}})};var g=f.Users()+"?api="+c.api;b.get(g).success(function(b,c){202==c&&(a.users=b)})}]),nApp.controller("AtlantisSettingsCtrl",["$scope","$http","$sessionStorage","$mdToast","AtlantisUri",function(a,b,c,d,e){var f=e.Settings()+"?api="+c.api;b.get(f).success(function(b,c){202==c&&(a.atlantis=b)}),a.saveGeneral=function(){var f=a.atlantis.Atlantis.url,g=a.atlantis.Atlantis.dep,h=a.atlantis.Atlantis.city,i=a.atlantis.Atlantis.lat,j=a.atlantis.Atlantis["long"],k=a.atlantis.Atlantis.radius,l=e.Settings()+"?api="+c.api;l+="&section=Atlantis&url="+f+"&dep="+g+"&city="+h+"&lat="+i+"&long="+j+"&radius="+k,b.put(l).success(function(a,b){showToast(d,b,a)})},a.saveNotification=function(){var f=a.atlantis.Notification.key,g=e.Settings()+"?api="+c.api;g+="&section=Notification&key="+f,b.put(g).success(function(a,b){showToast(d,b,a)})},a.saveSensors=function(){var f=a.atlantis.Hue.ip,g=a.atlantis.Hue.user,h=a.atlantis.Zwave.IP,i=a.atlantis.Zwave.Port,j=e.Settings()+"?api="+c.api;j+="&section=Zwave&ip="+h+"&port="+i,b.put(j).success(function(a,b){showToast(d,b,a)}),j=e.Settings()+"?api="+c.api,j+="&section=Hue&ip="+f+"&user="+g,b.put(j).success(function(a,b){showToast(d,b,a)})},a.saveFiles=function(){var f=a.atlantis.Files.music,g=e.Settings()+"?api="+c.api;g+="&section=Files&music="+f,b.put(g).success(function(a,b){showToast(d,b,a)})}}]),angular.module("atlantisWebAppApp").controller("SecuritySettingsCtrl",["$scope",function(a){a.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}]),nApp.directive("ngEnter",function(){return function(a,b,c){b.bind("keydown keypress",function(b){13===b.which&&(a.$apply(function(){a.$eval(c.ngEnter)}),b.preventDefault())})}}),nApp.controller("PlantesCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","AtlantisUri",function(a,b,c,d,e,f){function g(){var d=f.Plantes()+"?api="+c.api;b.get(d).success(function(b,c){202==c&&(a.data=b,a.plants=b)})}g(),a.getRoomLabel=function(a){if(null!=a.room){var b=d("filter")(c.rooms,{id:a.room});if(null!=b&&1==b.length)return"("+b[0].room+")"}},a.editPlant=function(a,b){e.show({templateUrl:"views/plant_settings.html",targetEvent:b,controller:"PlantSettingsCtrl",locals:{plant:a}}).then(function(a){null!=a&&g()})},a.filter=function(b){if(null==b||""==b)g();else{var c=[];angular.forEach(a.data,function(a){d("lowercase")(a.title).indexOf(d("lowercase")(b))>-1&&c.push(a)}),a.plants=c}},a.add=function(){var a=f.Plantes()+"?api="+c.api;b.post(a).success(function(a,b){g()})}}]),nApp.controller("RoomCtrl",["$scope","$rootScope","$http","$filter","$sessionStorage","$mdDialog","AtlantisUri",function(a,b,c,d,e,f,g){a.rooms=e.rooms,a.addRoom=function(b){var f=g.Rooms()+"?api="+e.api+"&label="+b;c.post(f).success(function(b,c){202==c&&(a.rooms.push(b),a.rooms=d("orderBy")(a.rooms,"room"),a.item=null)})},a.delRoom=function(b){var d=g.Rooms()+"?api="+e.api+"&id="+b.id;c["delete"](d).success(function(c,d){if(202==d){var e=a.rooms.indexOf(b);a.rooms.splice(e,1)}})},a.cancel=function(){e.rooms=a.rooms,f.cancel()}}]),nApp.controller("CuisineAddCtrl",["$scope","$http","$filter","$localStorage","$sessionStorage","$mdDialog","$mdToast","AtlantisUri",function(a,b,c,d,e,f,g,h){function i(){var c=h.Ean()+"?api="+e.api;b.get(c).success(function(b,c){d.ean=b,a.items=d.ean})}a.places=[{id:"placard",label:"Placard"},{id:"congelateur",label:"Congélateur"},{id:"frigidaire",label:"Frigidaire"}],null==d.ean||""==d.ean?i():a.items=d.ean,a.filterList=function(a){var b=d.ean,e=[];return angular.forEach(b,function(b){(c("lowercase")(b.ean).indexOf(c("lowercase")(a))>-1||c("lowercase")(b.nom).indexOf(c("lowercase")(a))>-1)&&e.push(b)}),e},a.itemChange=function(){a.codebarre=null},a.itemSelect=function(){null!=a.selectedItem.ean&&(a.codebarre=a.selectedItem.ean)},a.submit=function(){if(null==a.searchText||""==a.searchText||null==a.date||""==a.date)return void showToast(g,"Merci de remplir le nom et la date de péremption du produit !");var d=h.Cuisine()+"?api="+e.api;d+=null==a.selectedItem?null!=a.codebarre&&""!=a.codebarre?"&element="+a.codebarre+"&ean="+a.searchText:"&element="+a.searchText:"&element="+a.selectedItem.ean,d+="&peremption="+c("date")(a.date,"yyyy-MM-dd"),null!=a.place&&(d+="&endroit="+a.place.id),null!=a.quantity&&(d+="&quantite="+a.quantity),b.post(d).success(function(a,b){202==b&&f.hide(a)})},a.cancel=function(){f.cancel()}}]),nApp.controller("PharmacieAddCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","$mdToast","AtlantisUri",function(a,b,c,d,e,f,g){a.submit=function(){if(null==a.name||""==a.name||null==a.date||""==a.date)return void showToast(f,"Merci de remplir le nom et la date de péremption du médicament !");var h=g.Pharmacie()+"?api="+c.api;h+="&nom="+a.name+"&peremption="+d("date")(a.date,"yyyy-MM-dd"),null!=a.quantity&&a.quantity>1&&(h+="&qte="+a.quantity),b.post(h).success(function(a,b){202==b&&e.hide(a)})},a.cancel=function(){e.cancel()}}]),nApp.controller("EntretienAddCtrl",["$scope","$http","$sessionStorage","$filter","$mdDialog","$mdToast","AtlantisUri",function(a,b,c,d,e,f,g){a.submit=function(){if(null==a.name||""==a.name||null==a.date||""==a.date)return void showToast(f,"Merci de remplir le nom et la date de péremption du produit !");var h=g.Entretien()+"?api="+c.api;h+="&nom="+a.name+"&peremption="+d("date")(a.date,"yyyy-MM-dd"),null!=a.quantity&&a.quantity>1&&(h+="&qte="+a.quantity),b.post(h).success(function(a,b){202==b&&e.hide(a)})},a.cancel=function(){e.cancel()}}]),nApp.controller("DevicesAddCtrl",["$scope","$http","$sessionStorage","$mdDialog","$mdToast","AtlantisUri","users","device",function(a,b,c,d,e,f,g,h){a.types=[{id:"windows",label:"Microsoft Windows"},{id:"smartphone",label:"Smartphone"},{id:"linux",label:"Linux"},{id:"imprimante",label:"Imprimante"},{id:"autre",label:"Autre"}],a.connections=[{id:"wifi",label:"Wifi"},{id:"ethernet",label:"Ethernet"}],a.gcm=h.gcm,a.users=g,null!=h?(a.btnSubmit="Modifier",a.name=h.nom,a.ip=h.ip,a.mac=h.mac,a.type={id:h.type},a.connection={id:h.connexion},a.user={id:h.username}):a.btnSubmit="Ajouter",a.submit=function(){if(null==a.name||null==a.ip||null==a.mac||null==a.type||null==a.connection)return void showToast(e,"Merci de remplir les détails de l'appareil !");var g=f.Devices()+"?api="+c.api;g+="&title="+a.name+"&ip="+a.ip+"&mac="+a.mac+"&type="+a.type.id+"&connection="+a.connection.id,null!=a.user&&""!=a.user.id&&(g+="&user="+a.user.id),null==h?b.post(g).success(function(a,b){202==b&&d.hide(a)}):(g+="&id="+h.id,b.put(g).success(function(b,c){if(202==c){var e={nom:a.name,ip:a.ip,mac:angular.uppercase(a.mac),type:a.type.id,connexion:a.connection.id,username:a.user.id};d.hide(e)}}))},a.ring=function(){var d=f.Notify()+"?api="+c.api+"&cmd=ring&id="+a.mac;b.put(d).success(function(a,b){202==b&&showToast(e,"Notification envoyée !")})},a.unsuscribe=function(){var d=f.GCM()+"?api="+c.api+"&mac="+a.mac;b["delete"](d).success(function(b,c){console.log(c),202==c&&(a.gcm=null)})},a.cancel=function(){d.cancel()}}]),nApp.controller("HomePlanCtrl",["$scope","$sessionStorage","$mdDialog","$mdToast","Upload","$timeout","AtlantisUri",function(a,b,c,d,e,f,g){a.$watch("files",function(){if(a.files&&a.files.length){var f=g.Rooms();e.upload({url:f,fields:{api:b.api},file:a.files}).progress(function(a){parseInt(100*a.loaded/a.total)}).success(function(a,b,e,f){console.log(b,a),202==b?(showToast(d,"Plan envoyé avec succès !"),c.hide()):showToast(d,b,a)})}}),a.cancel=function(){c.cancel()}}]),nApp.controller("SensorsSettingsCtrl",["$scope","$mdDialog","$http","$sessionStorage","AtlantisUri",function(a,b,c,d,e){a.addSensors=function(){var a=e.Sensors()+"?api="+d.api;c.post(a).success(function(a,c){202==c&&b.hide({code:202})})},a.cancel=function(){b.cancel()}}]),nApp.controller("LightsSettingsCtrl",["$scope","$mdDialog","$http","$sessionStorage","AtlantisUri",function(a,b,c,d,e){a.cancel=function(){b.cancel()},a.addLights=function(){var a=e.Lights()+"?api="+d.api;c.post(a).success(function(a,c){b.hide({code:202})})}}]),nApp.controller("SensorSettingsCtrl",["$scope","$rootScope","$mdDialog","$filter","$sessionStorage","$http","AtlantisUri","sensor",function(a,b,c,d,e,f,g,h){a.sensor=h,a.rooms=b.rooms,a.cancel=function(){c.cancel()},0==e.user.type&&(a.admin=!0),a["delete"]=function(){},a.save=function(){var b=g.Sensors()+"?api="+e.api;b+="section"===h.type?"&device="+a.sensor.id+"&alias="+a.sensor.alias+"&room="+a.sensor.room:"&sensor="+a.sensor.id+"&history="+a.sensor.history+"&ignore="+a.sensor.ignore,f.put(b).success(function(b,d){202==d&&c.hide(a.sensor)})},"section"!=h.type&&(a.lastupdate=d("date")(1e3*h.update,"dd/MM/yyyy à hh:mm a"))}]),nApp.controller("PlantSettingsCtrl",["$scope","$sessionStorage","$http","$mdDialog","$mdToast","$timeout","AtlantisUri","Upload","plant",function(a,b,c,d,e,f,g,h,i){a.plant=i,a.rooms=b.rooms,0==b.user.type&&(a.admin=!0),a.cancel=function(){d.cancel()},a["delete"]=function(){},a.save=function(){var e=g.Plantes()+"?api="+b.api;e+="&id="+a.plant.id+"&title="+a.plant.title+"&room="+a.plant.room,c.put(e).success(function(b,c){202==c&&d.hide(a.plant)})},a.$watch("files",function(){if(a.files&&a.files.length){var c=g.Plantes();h.upload({url:c,fields:{api:b.api,id:i.id},file:a.files}).progress(function(a){var b=parseInt(100*a.loaded/a.total);console.log(b)}).success(function(a,b,c,d){console.log(b,a),202==b?showToast(e,"Image envoyée avec succès !"):showToast(e,b,a)})}})}]),nApp.controller("PlaylistSettingsCtrl",["$scope","$http","$sessionStorage","$mdDialog","AtlantisUri","playlist",function(a,b,c,d,e,f){function g(){var d=e.Music()+"?api="+c.api;d+="&action=playlistsongs&playlist="+f.id,b.put(d).success(function(b,c){a.songs=b})}a.playlist=f,0==c.user.type&&(a.admin=!0),g(),a.close=function(){d.cancel()},a["delete"]=function(){var a=e.Music()+"?api="+c.api;a+="&playlist="+f.id,b["delete"](a).success(function(a,b){202==b&&d.hide()})},a.del=function(d){var g=e.Music()+"?api="+c.api;g+="&action=playlistremove&playlist="+f.id+"&song="+d.id,b.put(g).success(function(b,c){if(202==c){var e=a.songs.indexOf(d);a.songs.splice(e,1)}})}}]),nApp.controller("SongSettingsCtrl",["$scope","$http","$sessionStorage","$mdDialog","AtlantisUri","song","playlists",function(a,b,c,d,e,f,g){
a.playlists=g,a.createPlaylist=function(){var d=e.Music()+"?api="+c.api+"&playlist="+a.newPlaylist;b.post(d).success(function(b,c){202==c&&(a.playlists.push(b),a.newPlaylist=null)})},a.addSong=function(){if(null!=a.song.playlist){var d=e.Music()+"?api="+c.api;d+="&playlist="+a.song.playlist+"&song="+f.id+"&action=playlistadd",b.put(d).success(function(a,b){console.log(a,b)})}},a.cancel=function(){d.cancel()}}]),nApp.controller("HistoryCtrl",["$scope","$http","$sessionStorage","$filter","AtlantisUri",function(a,b,c,d,e){function f(){var d=e.History()+"?api="+c.api;b.get(d).success(function(b,c){202==c&&(a.plantes=b.plants,a.capteurs=b.sensors)})}var g={datasetFill:!1};a.options=g,f(),a.loadPlant=function(){var f=e.History()+"?api="+c.api;f+="&plant="+a.plante.id+"&interval="+a.interval,b.get(f).success(function(b,c){var e=[],f=[],g=[];angular.forEach(b,function(a){e.push(d("date")(a.date,"EEEE d")),f.push(d("number")(a.moisture_m,1)),g.push(d("number")(a.soil_temperature_m,1))}),a.labels=e,a.data=[f,g],a.series=["Humidité du sol","Température du sol"]})},a.loadSensor=function(){var f=e.History()+"?api="+c.api;f+="&sensor="+a.capteur.id+"&interval="+a.interval,b.get(f).success(function(b,c){var e=[],f=[],g="";angular.forEach(b,function(a){f.push(d("number")(a.value,1)),a.date==g?e.push(a.time):(e.push(d("date")(a.date,"EEEE d")+" - "+a.time),g=a.date)}),a.labels=e,a.data=[f],a.series=[a.capteur.type+" "+a.capteur.unit]})}}]);