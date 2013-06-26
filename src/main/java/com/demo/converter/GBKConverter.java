package com.demo.converter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;

public class GBKConverter implements BeanPostProcessor {

	private static String  defalutCharset = "GBK";
	
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		// 修改StringHttpMessageConverter的supportedMediaTypes，解决response乱码
		if (bean instanceof StringHttpMessageConverter) {
			MediaType mediaType = new MediaType("text", "plain", Charset.forName(defalutCharset));
			List<MediaType> types = new ArrayList<MediaType>();
			types.add(mediaType);
			((StringHttpMessageConverter) bean).setSupportedMediaTypes(types);
		}
		
		// 修改StringHttpMessageConverter的supportedMediaTypes，解决response乱码
		if (bean instanceof MarshallingHttpMessageConverter) {
			MediaType mediaType = new MediaType("application", "xml", Charset.forName(defalutCharset));
			List<MediaType> types = new ArrayList<MediaType>();
			types.add(mediaType);
			((MarshallingHttpMessageConverter) bean).setSupportedMediaTypes(types);
		}
		// 修改Jaxb2RootElementHttpMessageConverter的supportedMediaTypes，解决response乱码
		if (bean instanceof Jaxb2RootElementHttpMessageConverter) {
			MediaType mediaType = new MediaType("application", "xml", Charset.forName(defalutCharset));
			List<MediaType> types = new ArrayList<MediaType>();
			types.add(mediaType);
			((Jaxb2RootElementHttpMessageConverter) bean).setSupportedMediaTypes(types);
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
}
