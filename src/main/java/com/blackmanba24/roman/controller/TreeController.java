package com.blackmanba24.roman.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static com.blackmanba24.roman.constant.Constants.*;
import com.blackmanba24.roman.util.ZookeeperUtil;

@Controller
public class TreeController {
	private static final Logger logger = LoggerFactory.getLogger(TreeController.class);

	@Value(value = "${zkUrl}")
	private String zkUrl;

	@RequestMapping(value = { "/", "/index", "/roman" })
	public String roman() {
		return "roman";
	}

	@RequestMapping(value = { "/getChildren" })
	public @ResponseBody List<Map<String, String>> getChildren(String id) {
		CuratorFramework client = ZookeeperUtil.getClient(zkUrl);
		List<Map<String, String>> results = new ArrayList<>();
		Map<String, String> map = null;
		if (StringUtils.isEmpty(id)) {
			map = new HashMap<>();
			map.put("id", SEP);
			map.put("text", SEP);
			map.put("state", "closed");
			results.add(map);
		} else {
			try {
				List<String> children = client.getChildren().forPath(id);
				for (String child : children) {
					String path_ = null;
					map = new HashMap<>();
					if (id.endsWith(SEP)) {
						if (ZOOKEEPER.equals(child)) {
							continue;
						} else {
							path_ = id + child;
							map.put("id", path_);
							map.put("text", child);
							if (client.getChildren().forPath(path_).size() != 0) {
								map.put("state", "closed");
							} else {
								map.put("state", "open");
							}
						}
					} else {
						path_ = id + SEP + child;
						map.put("id", path_);
						map.put("text", child);
						if (client.getChildren().forPath(path_).size() != 0) {
							map.put("state", "closed");
						} else {
							map.put("state", "open");
						}
					}
					results.add(map);
				}
			} catch (Exception e) {
				logger.error("getChildren exception, path {} ", id, e);
			}
		}
		return results;
	}

	@RequestMapping(value = { "/getData" })
	public @ResponseBody String getData(String id) {
		CuratorFramework client = ZookeeperUtil.getClient(zkUrl);
		if (StringUtils.isEmpty(id) || SEP.equals(id)) {
			logger.error("getData illegal path {}", id);
		}
		String result = null;
		if (id.startsWith(SEP + ZOOKEEPER)) {
			return result;
		}
		try {
			if (client.checkExists().forPath(id) != null) {
				byte[] bytes = client.getData().forPath(id);
				result = bytes == null ? null : new String(bytes);
			}
		} catch (Exception e) {
			logger.error("getData exception, path {} ", id, e);
		}
		return result;
	}
}
