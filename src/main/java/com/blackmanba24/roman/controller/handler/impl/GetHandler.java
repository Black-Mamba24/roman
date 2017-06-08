package com.blackmanba24.roman.controller.handler.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import static com.blackmanba24.roman.constant.Constants.*;

import java.util.Set;

import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.controller.vo.State;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.google.common.base.Preconditions;

public class GetHandler implements Handler {

	private CuratorFramework client;

	public GetHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkState(command.getVersion() == null, "get do not need version");
		Preconditions.checkState(StringUtils.isEmpty(command.getData()), "get do not need data");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("get".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			Set<String> options = command.getOptions();
			if (ZookeeperUtil.hasOption(options, _S)) {
				Stat stat = new Stat();
				byte[] bytes = client.getData().storingStatIn(stat).forPath(command.getPath());
				String data = bytes == null ? null : new String(bytes);
				State state = new State();
				BeanUtils.copyProperties(stat, state);
				return data + "\n" + state;
			} else {
				byte[] bytes = client.getData().forPath(command.getPath());
				String data = bytes == null ? null : new String(bytes);
				return data;
			}
		} else {
			return NON_EXISTENT_PATH;
		}
	}
}
