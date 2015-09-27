function dynamicOptions() {
	var options = [];
	var now = Date.now();
	for (var i = 0; i < 7; i++) {
		options.push([ String(new Date(now)).substring(0, 3), 'DAY' + i ]);
		now += 24 * 60 * 60 * 1000;
	}
	console.log(sessionStorage.getItem("ngStorage-api").replace(new RegExp('"', 'g'), '') + " bonjour");
	return options;
}

Blockly.Blocks['at_switch'] = {
  init: function() {
    this.appendDummyInput()
    	.appendField("Prise")
        .appendField(new Blockly.FieldDropdown(dynamicOptions), "SENSORS");
    this.appendValueInput("STATUS")
        .setCheck("Boolean")
        .appendField("Etat");
    this.setInputsInline(true);
    this.setPreviousStatement(false);
    this.setNextStatement(false);
    this.setColour(190);
    this.setTooltip('');
  }
};

Blockly.Blocks['at_gcm'] = {
	init : function() {
		this.appendDummyInput().appendField("Envoyer un message");
		this.appendDummyInput().appendField("Destinataire").appendField(new Blockly.FieldDropdown([ [ "Administrateurs", "0" ], [ "Utilisateurs", "1" ], [ "Invités", "2" ] ]), "USERS");
		this.appendValueInput("MSG").setCheck("String").appendField("Message");
		this.setPreviousStatement(true);
		this.setNextStatement(true);
		this.setColour(190);
		this.setTooltip('Envoyer une notification');
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