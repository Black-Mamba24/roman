package com.blackmanba24.roman.controller.handler.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import static com.blackmanba24.roman.constant.Constants.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.blackmanba24.roman.controller.handler.Handler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.controller.vo.State;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.google.common.base.Preconditions;

public class LsHandler implements Handler {

	private CuratorFramework client;

	public LsHandler(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public String handle(Command command) throws Exception {
		Preconditions.checkNotNull(command, "command in null");
		Preconditions.checkNotNull(command.getPath(), "command's path is null");
		Preconditions.checkState(command.getVersion() == null, "ls do not need version");
		Preconditions.checkState(StringUtils.isEmpty(command.getData()), "ls do not need data");
		Preconditions.checkArgument(!command.getPath().startsWith(SEP + ZOOKEEPER), "illegal path");
		Preconditions.checkArgument("ls".equals(command.getCommand()), "command is wrong");
		if (client.checkExists().forPath(command.getPath()) != null) {
			Set<String> options = command.getOptions();
			if (ZookeeperUtil.hasOption(options, _S)) {
				List<String> list = client.getChildren().forPath(command.getPath());
				if (SEP.equals(command.getPath())) {
					exclude(list);
				}
				Stat stat = new Stat();
				State state = new State();
				client.getChildren().storingStatIn(stat).forPath(command.getPath());
				BeanUtils.copyProperties(stat, state);
				return list + "\n" + state;
			} else {
				List<String> list = client.getChildren().forPath(command.getPath());
				if (SEP.equals(command.getPath())) {
					exclude(list);
				}
				return list.toString();
			}
		} else {
			return NON_EXISTENT_PATH;
		}
	}
	
	private void exclude(List<String> list) {
		Iterator<String> iterator = list.iterator();
		while(iterator.hasNext()) {
			String string = iterator.next();
			if (ZOOKEEPER.equals(string)) {
				iterator.remove();
			}
		}
		
	}
}
