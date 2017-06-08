package com.blackmanba24.roman.controller.handler.impl;

import static com.blackmanba24.roman.constant.Constants.*;

import java.util.Set;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.util.StringUtils;

import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.google.common.base.Preconditions;

public class DeleteHandler implements Handler {

	private CuratorFramework client;

	public DeleteHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkState(StringUtils.isEmpty(command.getData()), "delete do not need data");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("delete".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			Set<String> options = command.getOptions();
			if (ZookeeperUtil.hasOption(options, _V)) {
				Preconditions.checkNotNull(command.getVersion(), "command's version is null");
				client.delete().withVersion(command.getVersion()).forPath(command.getPath());
			} else {
				client.delete().forPath(command.getPath());
			}
			return SUCCESS;
		} else {
			return NON_EXISTENT_PATH;
		}
	}

}
