package com.blackmanba24.roman.controller;

import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.blackmanba24.roman.constant.Constants;
import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.handler.HandlerManager;
import com.blackmanba24.roman.controller.vo.Command;

@Controller
public class CommandController {
	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

	@Autowired
	private HandlerManager handlerManager;

	@RequestMapping(value = { "/command" })
	public @ResponseBody String command(Command command) {
		for(String opt : command.getOptionsStr().split(",")){
			if (command.getOptions() == null) {
				command.setOptions(new HashSet<String>());
			}
			if (!StringUtils.isEmpty(opt)) {
				command.getOptions().add(opt);
			}
		}
		
		Handler handler = handlerManager.getHandler(command);
		String result = null;
		if (handler != null) {
			try {
				result = handler.handle(command);
			} catch (Exception e) {
				logger.error("handle command error, command {} ", command, e);
				result = e.getMessage();
			}
		} else {
			result = Constants.WRONG_COMMAND;
		}
		return result;
	}
}
