package com.demo.mbean;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
@Component
@ManagedResource(objectName = "com.demo:jobMBean=jobMBean", description = "��ҵ")
public class JobMBean<T> {
	
	private int count;

	@ManagedAttribute(description="��ҵ����״̬")
	public String getRunState() {
		return "start";
	}
	
	@ManagedOperation(description="������ҵ")
	public void startup() {
	}
	
	@ManagedOperation(description="��������")
	public void setCount(int count){
		this.count=count;
	}
	
	@ManagedAttribute(description="����")
	public int getCount(){
		return count;
	}
}
