package com.demo.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.InputSource;

public class XMLUtil {
	
	public static String marshal(Object object){
		return marshal(object, "GBK");
	}
	
	public static String marshal(Object object,String encoding){
		String content = null;
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			marshaller.marshal(object,baos);
			content = baos.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	public static <T> T unMarshal(String xml,Class<T> clazz){
		return unMarshal(xml, clazz, "GBK");
	}
	
	public static <T> T unMarshal(String xml,Class<T> clazz,String encoding){
		T result = null;
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
			JAXBContext context = JAXBContext.newInstance(clazz);
			result = (T)context.createUnmarshaller().unmarshal(new InputSource(new InputStreamReader(is,encoding)));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return  result;
	
	}

}
