function allCommands() {
	var set = new Set();
	set.add('create_but');
	set.add('set_but');
	set.add('get_but');
	set.add('ls_but');
	set.add('delete_but');
	set.add('deleteall_but');
	set.add('rmr_but');
	set.add('sync_but');
	return set;
}

function allOptions() {
	var set = new Set();
	set.add('s_opt');
	set.add('w_opt');
	set.add('e_opt');
	set.add('v_opt');
	return set;
}

function createOptions() {
	var set = new Set();
	set.add('s_opt');
	set.add('e_opt');
	return set;
}

function setOptions() {
	var set = new Set();
	set.add('s_opt');
	set.add('v_opt');
	return set;
}

function getOptions() {
	var set = new Set();
	set.add('s_opt');
	set.add('w_opt');
	return set;
}

function lsOptions() {
	var set = new Set();
	set.add('s_opt');
	set.add('w_opt');
	return set;
}

function deleteOptions() {
	var set = new Set();
	set.add('v_opt');
	return set;
}