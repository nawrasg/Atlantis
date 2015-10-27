function getRooms() {
	var rooms = sessionStorage.getItem("ngStorage-rooms");
	var nRooms = JSON.parse(rooms);
	var nResult = [];
	for (var i = 0; i < nRooms.length; i++) {
		nResult.push([ nRooms[i].room, nRooms[i].id ]);
	}
	return nResult;
}
function getLights(){
	if(window.lights == null){
		return [['', '']];
	}
	var nResult = [];
	for(var i = 0; i < window.lights.length; i++){
		var name = window.lights[i].name;
		var id = window.lights[i].id;
		nResult.push([name, id]);
	}
	return nResult;
}
function getSwitches(){
	
}

Blockly.Blocks['at_switch'] = {
	init : function() {
		this.appendDummyInput().appendField("Prise").appendField(
				new Blockly.FieldDropdown(getRooms), "ROOM");
		this.appendValueInput("STATUS").setCheck("Boolean").appendField("Allumé (V/F)");
		this.setInputsInline(false);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Contrôler une prise électrique.');
	}
};

Blockly.Blocks['at_gcm'] = {
	init : function() {
		this.appendDummyInput().appendField("Envoyer un message");
		this.appendDummyInput().appendField("Destinataire")
				.appendField(
						new Blockly.FieldDropdown([ [ "Administrateurs", "0" ],
								[ "Utilisateurs", "1" ], [ "Invités", "2" ] ]),
						"USERS");
		this.appendValueInput("MSG").setCheck("String").appendField("Message");
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Envoyer une notification.');
	}
};

Blockly.Blocks['at_sleep'] = {
	init : function() {
		this.appendValueInput("SEC").setCheck("Number").appendField(
				"Pause (secondes)");
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Faire une pause');
	}
};

Blockly.Blocks['at_light'] = {
	init : function() {
		this.appendDummyInput().appendField("Ampoule").appendField(
				new Blockly.FieldDropdown(getRooms), "ROOM");
		this.appendValueInput("ON").setCheck("Boolean").appendField("Allumé");
		this.appendValueInput("INTENSITY").setCheck("Number").appendField(
				"Intensité (0-255)");
		this.appendDummyInput().appendField("Couleur")
				.appendField(
						new Blockly.FieldDropdown([ [ "Rouge", "red" ],
								[ "Vert", "green" ], [ "Bleu", "blue" ],
								[ "Jaune", "yellow" ], [ "Blanc", "white" ] ]),
						"COLOR");
		this.setInputsInline(false);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Contrôler les ampoules');
	}
};
Blockly.Blocks['at_alarm_status'] = {
	init : function() {
		this.appendDummyInput().appendField("Alarme activée");
		this.setOutput(true);
		this.setColour(190);
		this.setTooltip('Connaître l\'état de l\'alarme.');
		this.setHelpUrl('http://www.example.com/');
	}
};

Blockly.Blocks['at_alarm'] = {
	init : function() {
		this.appendDummyInput().appendField("Alarme");
		this.appendValueInput("STATUS").setCheck("Boolean").appendField("Etat");
		this.setInputsInline(true);
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Changer l\'état de l\'alarme.');
		this.setHelpUrl('http://www.example.com/');
	}
};

Blockly.Blocks['at_music'] = {
	init : function() {
		this.appendDummyInput().appendField("Lecteur de musique");
		this.appendValueInput("PLAY").setCheck("Boolean")
				.appendField("Lecture (V/F)");
		this.appendValueInput("VOLUME").setCheck("Number").appendField(
				"Volume (0-100)");
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Contrôler le lecteur de musique.');
	}
};

Blockly.Blocks['at_light_status'] = {
	init : function() {
		this.appendDummyInput().appendField("Ampoule Allumée")
				.appendField(new Blockly.FieldDropdown(getLights), "ID");
		this.setOutput(true, "Boolean");
		this.setColour(190);
		this.setTooltip('Obtenir l\'état de l\'ampoule (allumée ou éteinte).');
	}
};

Blockly.Blocks['at_time'] = {
	init : function() {
		this.appendDummyInput().appendField("Heure actuelle").appendField(
				new Blockly.FieldDropdown([ [ "=", "=" ], [ "<", "<" ],
						[ "<=", "<=" ], [ ">", ">" ], [ ">=", ">=" ] ]),
				"OPERATION");
		this.appendValueInput("TIME").setCheck("String").appendField("(HH:MM)");
		this.setInputsInline(true);
		this.setOutput(true, "Boolean");
		this.setColour(190);
		this.setTooltip('');
		this.setHelpUrl('http://www.example.com/');
	}
};