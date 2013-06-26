package com.demo.mbean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
@Component
@ManagedResource(objectName = "com.demo:jobMBean=jobMBean", description = "��ҵ")
public class JobMBean<T> {

	@ManagedAttribute(description="��ҵ����״̬")
	public String getRunState() {
		return "start";
	}
	
	@ManagedOperation(description="������ҵ")
	public void startup() {
	}
}
