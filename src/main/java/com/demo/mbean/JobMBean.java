package com.demo.mbean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
@Component
@ManagedResource(objectName = "com.demo:jobMBean=jobMBean", description = "作业")
public class JobMBean<T> {
	
	private int count;

	@ManagedAttribute(description="作业运行状态")
	public String getRunState() {
		return "start";
	}
	
	@ManagedOperation(description="启动作业")
	public void startup() {
	}
	
	@ManagedOperation(description="设置总数")
	public void setCount(int count){
		this.count=count;
	}
	
	@ManagedAttribute(description="总数")
	public int getCount(){
		return count;
	}
}
