package com.blackmanba24.roman.util;

import java.util.Set;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperUtil {
	private static CuratorFramework client;
	public final static int baseSleepTimeMs = 1000;
	public final static int maxRetries = 3;
	
	public static CuratorFramework getClient(String connectString){
		if (client == null) {
			synchronized (ZookeeperUtil.class) {
				if (client == null) {
					RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
					client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
					client.start();
				}
			}
		}
		return client;
	}
	
	public static boolean hasOption(Set<String> options, String... os) {
		if (options == null) {
			return false;
		}
		for(String o : os) {
			if (!options.contains(o)) {
				return false;
			}
		}
		return true;
	}
}
