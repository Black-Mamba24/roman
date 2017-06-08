package com.blackmanba24.roman.controller.handler.impl;

import static com.blackmanba24.roman.constant.Constants.*;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.google.common.base.Preconditions;

public class SyncHandler implements Handler {
	
	private CuratorFramework client;
	
	public SyncHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkState(CollectionUtils.isEmpty(command.getOptions()), "sync do not need options");
		Preconditions.checkState(command.getVersion() == null, "sync do not need version");
		Preconditions.checkState(StringUtils.isEmpty(command.getData()), "sync do not need data");
		Preconditions.checkArgument("sync".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			client.sync().forPath(command.getPath());
			return SUCCESS;
		} else {
			return NON_EXISTENT_PATH;
		}
	}

}
