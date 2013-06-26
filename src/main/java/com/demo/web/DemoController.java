package com.demo.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.service.DemoService;

@Controller
@RequestMapping
public class DemoController {

	@Resource
	private DemoService service ;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@RequestMapping(value="/execute")
	@ResponseBody
	public String execute(){
		logger.info("controller...");
		this.service.execute();
		return "ÄãºÃ£¬ÊÀ½ç";
	}
}
