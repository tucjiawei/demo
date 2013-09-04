package com.demo.xml;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;

public class TestJaxb {

	public static void main(String[] args) throws JAXBException, UnsupportedEncodingException, InterruptedException, ExecutionException {
		TestXml jaxbElement = new TestXml();
		jaxbElement.setName("jiawei");
		jaxbElement.setList(Arrays.asList("ÄãºÃ","world"));
		String content = XMLUtil.marshal(jaxbElement);
		System.out.println(content);
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testXml><listElement><list>asd</list><list>world</list></listElement></testXml>";
		jaxbElement = XMLUtil.unMarshal(xml,TestXml.class);
		System.out.println();
		System.out.println(jaxbElement.getList());

	}
}
