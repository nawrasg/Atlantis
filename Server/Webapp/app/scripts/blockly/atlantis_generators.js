Blockly.PHP['at_switch'] = function(block) {
	var status = Blockly.PHP.valueToCode(block, 'STATUS', Blockly.PHP.ORDER_ATOMIC);
	var sensor = block.getFieldValue('SENSORS');
	var code = '$zwave = new Zwave();\n';
	code += '$zwave->command(' + sensor + ', ' + status + ');\n';
	return code;
}

Blockly.PHP['at_gcm'] = function(block) {
	var message = Blockly.PHP.valueToCode(block, 'MSG', Blockly.PHP.ORDER_ATOMIC);
	var users = block.getFieldValue('USERS');
	var code = '$msg = new PushMessage();\n';
	code += '$msg->sendMessage("Atlantis", ' + message + ', ' + users + ');\n';
	return code;
}

Blockly.PHP['at_sleep'] = function(block) {
	var sec = Blockly.PHP.valueToCode(block, 'SEC', Blockly.PHP.ORDER_ATOMIC);
	var code = 'sleep(' + sec + ');\n';
	return code;
}

Blockly.PHP['at_light'] = function(block) {
	var room = block.getFieldValue('ROOM');
	var on = Blockly.PHP.valueToCode(block, 'ON', Blockly.PHP.ORDER_ATOMIC);
	var intensity = Blockly.PHP.valueToCode(block, 'INTENSITY', Blockly.PHP.ORDER_ATOMIC);
	var color = block.getFieldValue('COLOR');
	var code = '$light = Light::byRoom(' + room + ');\n';
	code += '$light->color(\'' + color + '\');\n';
	code += '$light->on(' + on + ');\n';
	code += '$light->brightness(' + intensity + ');\n';
	return code;
}