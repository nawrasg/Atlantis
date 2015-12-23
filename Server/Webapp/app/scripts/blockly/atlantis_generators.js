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
	if(value_play === 'true'){
		code = '$music = new Music();\n';
		code += '$music->play();\n';
		if(value_volume != '' && typeof parseInt(value_volume) == 'number'){
			code += '$music->volume(' + value_volume + ', true);\n';
		}
	}else{
		code = '(new Music())->stop();\n';
	}
	return code;
};

Blockly.PHP['at_light_status'] = function(block) {
	var dropdown_id = block.getFieldValue('ID');
	var code = 'Light::byID(' + dropdown_id + ')->isOn()';
	return [ code, Blockly.PHP.ORDER_NONE ];
};

Blockly.PHP['at_alarm_status'] = function(block) {
	var code = '(new Alarm())->isOn()';
	return [ code, Blockly.PHP.ORDER_NONE ];
};

Blockly.PHP['at_time'] = function(block) {
	var dropdown_operation = block.getFieldValue('OPERATION');
	var value_time = Blockly.PHP.valueToCode(block, 'TIME', Blockly.PHP.ORDER_ATOMIC);
	var code = '';
	if(value_time != ''){
		switch(dropdown_operation){
		case '=':
			code = '(time() == strtotime(' + value_time + '))';
		case '<':
			code = '(time() < strtotime(' + value_time + '))';
		case '<=':
			code = '(time() <= strtotime(' + value_time + '))';
		case '>':
			code = '(time() > strtotime(' + value_time + '))';
		case '>=':
			code = '(time() >= strtotime(' + value_time + '))';
		}
	}
	return [ code, Blockly.PHP.ORDER_NONE ];
};

Blockly.PHP['at_mail'] = function(block) {
	var value_to = Blockly.PHP.valueToCode(block, 'TO', Blockly.PHP.ORDER_ATOMIC);
	var value_subject = Blockly.PHP.valueToCode(block, 'SUBJECT', Blockly.PHP.ORDER_ATOMIC);
	var value_body = Blockly.PHP.valueToCode(block, 'BODY', Blockly.PHP.ORDER_ATOMIC);
	
	var code = '(new Mail())->sendTo(' + value_to + ', ' + value_subject + ', ' + value_body + ');\n';
	return code;
};