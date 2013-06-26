package com.demo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestXml {
@XmlElement(required=true)
private String name;
@XmlElementWrapper(name="listElement")
private List<String> list;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public List<String> getList() {
	return list;
}
public void setList(List<String> list) {
	this.list = list;
}
}
