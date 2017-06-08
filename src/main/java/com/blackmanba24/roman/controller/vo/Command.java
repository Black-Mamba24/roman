package com.blackmanba24.roman.controller.vo;

import java.util.HashSet;
import java.util.Set;

public class Command {
	private String command;
	
	private String optionsStr;

	private Set<String> options;

	private String path;
	
	private String data;
	
	private Integer version;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getOptionsStr() {
		return optionsStr;
	}

	public void setOptionsStr(String optionsStr) {
		this.optionsStr = optionsStr;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Command [command=" + command + ", optionsStr=" + optionsStr + ", options=" + options + ", path=" + path + ", data=" + data + ", version=" + version + "]";
	}
	
}
