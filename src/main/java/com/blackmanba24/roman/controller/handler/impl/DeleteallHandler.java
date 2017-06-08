package com.blackmanba24.roman.controller.handler.impl;

import static com.blackmanba24.roman.constant.Constants.*;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.google.common.base.Preconditions;

public class DeleteallHandler implements Handler {

	private CuratorFramework client;

	public DeleteallHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkState(CollectionUtils.isEmpty(command.getOptions()), "deleteall/rmr do not need options");
		Preconditions.checkState(command.getVersion() == null, "deleteall/rmr do not need version");
		Preconditions.checkState(StringUtils.isEmpty(command.getData()), "deleteall/rmr do not need data");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("deleteall".equals(command.getCommand()) || "rmr".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			client.delete().deletingChildrenIfNeeded().forPath(command.getPath());
			return SUCCESS;
		} else {
			return NON_EXISTENT_PATH;
		}
	}

}
