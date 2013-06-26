package com.demo.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class DemoDao {

	@Resource(name="productB2cSqlSession")
	public SqlSession session;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void execute() {
		logger.info("dao...");
	}
}
