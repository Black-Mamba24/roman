package com.blackmanba24.roman.controller.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blackmanba24.roman.controller.handler.impl.CreateHandler;
import com.blackmanba24.roman.controller.handler.impl.DeleteHandler;
import com.blackmanba24.roman.controller.handler.impl.DeleteallHandler;
import com.blackmanba24.roman.controller.handler.impl.GetHandler;
import com.blackmanba24.roman.controller.handler.impl.LsHandler;
import com.blackmanba24.roman.controller.handler.impl.SetHandler;
import com.blackmanba24.roman.controller.handler.impl.SyncHandler;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.util.ZookeeperUtil;

@Service
public class HandlerManager {
	
	@Value(value = "${zkUrl}")
	private String zkUrl;

	private CuratorFramework client;

	private Map<String, Handler> hmap = new HashMap<>();

	@PostConstruct
	private void init() {
		client = ZookeeperUtil.getClient(zkUrl);
		hmap.put("set", new SetHandler(client));
		hmap.put("get", new GetHandler(client));
		hmap.put("ls", new LsHandler(client));
		hmap.put("delete", new DeleteHandler(client));
		hmap.put("deleteall", new DeleteallHandler(client));
		hmap.put("rmr", new DeleteallHandler(client));
		hmap.put("create", new CreateHandler(client));
		hmap.put("sync", new SyncHandler(client));
	}

	public Handler getHandler(Command command) {
		return hmap.get(command.getCommand());
	}
}
