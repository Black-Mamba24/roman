package com.blackmanba24.roman.controller.handler.impl;

import static com.blackmanba24.roman.constant.Constants.*;
import java.util.Set;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.google.common.base.Preconditions;

public class CreateHandler implements Handler {

	private CuratorFramework client;

	public CreateHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("create".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) == null) {
			CreateMode createMode = null;
			Set<String> options = command.getOptions();
			if (ZookeeperUtil.hasOption(options, _E, _S)) {
				createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
			} else if (ZookeeperUtil.hasOption(options, _S)) {
				createMode = CreateMode.PERSISTENT_SEQUENTIAL;
			} else if (ZookeeperUtil.hasOption(options, _E)) {
				createMode = CreateMode.EPHEMERAL;
			} else {
				createMode = CreateMode.PERSISTENT;
			}
			return client.create().withMode(createMode).forPath(command.getPath(), command.getData() == null ? null : command.getData().getBytes());
		} else {
			return EXISTENT_PATH;
		}
	}
}
