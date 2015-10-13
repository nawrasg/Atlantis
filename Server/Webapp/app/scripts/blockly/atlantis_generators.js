Blockly.PHP['at_switch'] = function(block) {
	var status = Blockly.PHP.valueToCode(block, 'STATUS', Blockly.PHP.ORDER_ATOMIC);
	var room = block.getFieldValue('ROOM');
	var code = '$relay = Relay::byRoom(' + room + ');\n';
	code += '$relay->on(' + status + ');\n';
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

Blockly.PHP['at_alarm_status'] = function(block) {
	var code = '((new Settings())->getSettings(\'Alarm\', \'status\'))';
	return [code, Blockly.PHP.ORDER_NONE];
};

Blockly.PHP['at_alarm'] = function(block) {
	var status = Blockly.PHP.valueToCode(block, 'STATUS', Blockly.PHP.ORDER_ATOMIC);
	var code = '(new Settings())->setSettings(\'Alarm\', \'status\', ' + status + ');\n';
	return code;
};

Blockly.PHP['at_music'] = function(block) {
	var value_play = Blockly.PHP.valueToCode(block, 'PLAY', Blockly.PHP.ORDER_ATOMIC);
	var value_volume = Blockly.PHP.valueToCode(block, 'VOLUME', Blockly.PHP.ORDER_ATOMIC);
	var code = '';
	if(value_play){
		code = '$music = new Music();\n';
		code += '$music->play();\n';
		if(typeof value_volume === 'number'){
			code += '$music->volume(' + value_volume + ', true);\n';
		}
	}else{
		code = '(new Music())->stop();\n';
	}
	return code;
};