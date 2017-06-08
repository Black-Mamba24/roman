var myCommand={
	command: null,
	optionsStr:null,
	path:null,
	data:null,
	version:null
};

$(document).ready(function() {
	showZkTree();
});

var pathExist = false;
var commandExist = false;

function showZkTree() {
	$('#zkTree').tree({
		url : '/getChildren',
		method : 'get',
		animate : true,
		lines : true,
		onSelect : function(node) {
				$.get('/getData', {
					id : node.id
				}, function(data) {
					$('#resultStr').val("")
					$('#resultStr').val(data);
				});
				if(!commandExist) {
					$('#whole_command').val(node.id);
				} else if (!pathExist) {
					var old_value = $('#whole_command').val();
					$('#whole_command').val(old_value + node.id + " ");
					pathExist = true;
				}
		}
	});
}

function command(command_id) {
	var commands = allCommands();
	commands.delete(command_id);
	// command
	$('#'+command_id).attr("disabled", true);
	for(var but of commands) {
		$('#'+but).css("text-decoration", "line-through");
		$('#'+but).attr("disabled", true);
	}
	// option
	var com = command_id.substring(0, command_id.indexOf('_'));
	var options;
	var left_options;
	switch(com) {
	case 'create':
		options = createOptions();
		break;
	case 'set':
		options = setOptions();
		break;
	case 'get':
		options = getOptions();
		break;
	case 'ls':
		options = lsOptions();
		break;
	case 'delete':
		options = deleteOptions();
		break;
	case 'deleteall':
		options = new Set();
		break;
	case 'rmr':
		options = new Set();
		break;
	case 'sync':
		options = new Set();
		break;
	default:
		return;
	}
	var left_options = allOptions();
	for(var o of options) {
		left_options.delete(o);
	}
	for(var left of left_options) {
		$('#'+left).css("text-decoration", "line-through");
		$('#'+left).attr("disabled", true);
		if(left == 'v_opt') {
			$('#version').attr("disabled", true);
		}
	}
	// textarea
	if($('#whole_command').val() != null) {
		$('#whole_command').val("");
	}
	$('#whole_command').val(com + ' ');
	commandExist = true;
}

function option(opt_id) {
	$('#'+opt_id).attr("disabled", true);
	var opt = '-' + opt_id.substring(0, opt_id.indexOf('_'));
	var old_value = $('#whole_command').val();
	$('#whole_command').val(old_value + opt + " ");
}

function versionChange() {
	var version = $('#version').val();
	var old_value = $('#whole_command').val();
	$('#whole_command').val(old_value + version + " ");
	$('#version').attr("disabled", true);
}

function clear_() {
	for(var but of allCommands()) {
		$('#'+but).css("text-decoration", "none");
		$('#'+but).attr("disabled", false);
	}
	for(var opt of allOptions()) {
		$('#'+opt).css("text-decoration", "none");
		$('#'+opt).attr("disabled", false);
	}
	$('#whole_command').val("");
	$('#version').val("");
	$('#version').attr("disabled", false);
	commandExist = pathExist = false;
}

function submit_() {
	var whole_command = $('#whole_command').val().trim();
	whole_command = whole_command.replace(/\s+/g, ' ');
	var components = whole_command.split(" ");
	var i;
	for(i = 0; i<components.length; i++) {
		if(i == 0) {
			myCommand.command = components[0];
		} else if(components[i] == "-s" || components[i] == "-w" || components[i] == "-e" || components[i] == "-v") {
			var optionsStr = myCommand.optionsStr;
			if(optionsStr == null) {
				optionsStr = components[i];
			} else {
				optionsStr = optionsStr + "," + components[i];
			}
			myCommand.optionsStr = optionsStr;
			if(components[i] == "-v") {
				var version = components[++i];
				if(version % 1 == 0)
					myCommand.version = version;
				else
					throw new Error("version illegal");
			}
		} else if(components[i].charAt(0) == "/") {
			if(myCommand.path == null){
				myCommand.path = components[i];
			} else {
				clearMyCommand()
				throw new Error("path already exists");
			}
		} else {
			if(myCommand.data == null) {
				myCommand.data = components[i];
			} else {
				clearMyCommand()
				throw new Error("data already exists");
			}
		}
	}
	
	$.ajax({
		url:"/command",
		contentType:"application/json",
		type:"get",
		data:myCommand,
		success: function(result){
			$('#resultStr').val("");
			$('#resultStr').val(result);
		}
	});
	clearMyCommand();
	commandExist = pathExist = false;
}

function refresh(){
	//获取选中节点
	var node = $('#zkTree').tree('getSelected');
	if(node == null) {
		return;
	}
	$.get('/getChildren', {id:node.id}, function(result){
		//获取选中节点所有子节点，并全部删除
		var allChildren = $('#zkTree').tree('getChildren',node.target);
		for(var i = 0; i < allChildren.length; i++){
			$('#zkTree').tree('remove', allChildren[i].target);
		}
		//在当前节点下添加新子节点
		$('#zkTree').tree('append', {
			parent:node.target,
			data:result
		});
	});
};

function clearMyCommand() {
	myCommand.command = null;
	myCommand.optionsStr = null;
	myCommand.path = null;
	myCommand.data = null;
	myCommand.version = null;
}
