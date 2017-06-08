package com.blackmanba24.roman;

import java.util.HashSet;
import java.util.Set;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.blackmanba24.roman.controller.vo.Command;
import com.blackmanba24.roman.util.ZookeeperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RomanApplicationTests {
	MockMvc mvc;
	Command command;
	String url;
	String path;
	
	@Value(value="${zkUrl}")
	private String zkUrl;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Before
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		url = "/command";
		path = "/zhaiyi";
		command = new Command();
	}
	
	@Test
	public void mytest() throws Exception{
//		CuratorFramework client = ZookeeperUtil.getClient(zkUrl);
//		System.out.println(client.getNamespace()); // 空
//		System.out.println(client.getState()); //LATENT, STARTED, STOPPED
//		NodeCache nodeCache = new NodeCache(client, path);
//		nodeCache.start();
//		nodeCache.getListenable().addListener(new NodeCacheListener() {
//			
//			@Override
//			public void nodeChanged() throws Exception {
//				System.out.println(new String(nodeCache.getCurrentData().getData()));
//				
//			}
//		});
//		
//		PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
//		pathChildrenCache.start(); // boolean, mode
//		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
//			
//			@Override
//			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
//				event.getData().getPath();
//				event.getData().getData();
//				//...
//			}
//		});
//		Thread.sleep(1000 * 1000);
//		nodeCache.close();
//		pathChildrenCache.close();
	}

	@Test
	public void create() throws Exception {
		command.setCommand("create");
		command.setPath(path);
		command.setData("sub");
		// Set<String> set = new HashSet<>();
		// set.add("-e");
		// set.add("-s");
		// command.setOptions(set);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void set() throws Exception {
		command.setCommand("set");
		command.setPath(path);
		command.setData("set");
		Set<String> set = new HashSet<>();
		set.add("-s");
		set.add("-v");
		command.setOptions(set);
		command.setVersion(3);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void get() throws Exception {
		command.setCommand("get");
		command.setPath(path);
		Set<String> set = new HashSet<>();
		set.add("-w");
		set.add("-s");
		command.setOptions(set);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void ls() throws Exception {
		command.setCommand("ls");
		command.setPath("/");
		Set<String> set = new HashSet<>();
		set.add("-s");
		set.add("-w");
		command.setOptions(set);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void delete() throws Exception {
		command.setCommand("delete");
		command.setPath(path);
		Set<String> set = new HashSet<>();
		set.add("-v");
		command.setOptions(set);
		command.setVersion(0);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void deleteall() throws Exception {
		command.setCommand("deleteall");
		command.setPath(path);
		ObjectMapper mapper = new ObjectMapper();
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(command))).andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

}
