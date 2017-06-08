package com.blackmanba24.roman.controller.handler;

import com.blackmanba24.roman.controller.vo.Command;

public interface Handler {
	String handle(Command command) throws Exception;
}
