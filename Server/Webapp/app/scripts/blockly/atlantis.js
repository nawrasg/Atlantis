function getRooms() {
	var rooms = sessionStorage.getItem("ngStorage-rooms");
	var nRooms = JSON.parse(rooms);
	var nResult = [];
	for (var i = 0; i < nRooms.length; i++) {
		nResult.push([ nRooms[i].room, nRooms[i].id ]);
	}
	return nResult;
}

Blockly.Blocks['at_switch'] = {
	init : function() {
		this.appendDummyInput().appendField("Prise").appendField(
				new Blockly.FieldDropdown(getRooms), "ROOM");
		this.appendValueInput("STATUS").setCheck("Boolean").appendField("Etat");
		this.setInputsInline(true);
		this.setPreviousStatement(false);
		this.setNextStatement(false);
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