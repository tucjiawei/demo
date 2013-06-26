package com.demo.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.dao.DemoDao;


@Service
public class DemoService {

	@Resource
	private DemoDao dao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public void execute() {
		logger.info("service...");
		this.dao.execute();
	}
}
