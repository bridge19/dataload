package com.xy.logload.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.xy.logload.domain.User;
import com.xy.logload.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService, InitializingBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private List<User> userList = null;
	
	public UserServiceImpl(){

		LOGGER.info("构造...");
		
	}
	private void init(){
		LOGGER.info("初始化...");
		userList = new ArrayList<>();
		User user = new User();
		user.setId(1L);
		user.setLoginName("andy");
		user.setName("Andy");
		userList.add(user);
		user = new User();
		user.setId(2L);
		user.setLoginName("carl");
		user.setName("Carl");
		userList.add(user);
		user = new User();
		user.setId(3L);
		user.setLoginName("bruce");
		user.setName("Bruce");
		userList.add(user);
		user = new User();
		user.setId(4L);
		user.setLoginName("dolly");
		user.setName("Dolly");
		userList.add(user);
		
	}
	
	public List<User> getUserList() {
		// TODO Auto-generated method stub
		return userList;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		init();
	}

	
}
