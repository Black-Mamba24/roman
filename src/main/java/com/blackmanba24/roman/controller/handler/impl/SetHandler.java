package com.blackmanba24.roman.controller.handler.impl;

import java.util.Set;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeanUtils;
import static com.blackmanba24.roman.constant.Constants.*;
import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.controller.vo.State;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.google.common.base.Preconditions;

public class SetHandler implements Handler {

	private CuratorFramework client;

	public SetHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command is null");
		Preconditions.checkNotNull(command.getCommand(), "command's command is null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkNotNull(command.getData(), "command's data is null");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("set".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			Set<String> options = command.getOptions();
			if (ZookeeperUtil.hasOption(options, _S, _V)) {
				Preconditions.checkNotNull(command.getVersion(), "command's version is null");
				Stat stat = client.setData().withVersion(command.getVersion()).forPath(command.getPath(), command.getData().getBytes());
				State state = new State();
				BeanUtils.copyProperties(stat, state);
				return state.toString();
			} else if (ZookeeperUtil.hasOption(options, _S)) {
				Stat stat = client.setData().forPath(command.getPath(), command.getData().getBytes());
				State state = new State();
				BeanUtils.copyProperties(stat, state);
				return state.toString();
			} else if (ZookeeperUtil.hasOption(options, _V)) {
				Preconditions.checkNotNull(command.getVersion(), "command's version is null");
				client.setData().withVersion(command.getVersion()).forPath(command.getPath(), command.getData().getBytes());
				return SUCCESS;
			} else {
				client.setData().forPath(command.getPath(), command.getData().getBytes());
				return SUCCESS;
			}
		} else {
			return NON_EXISTENT_PATH;
		}
	}
}
