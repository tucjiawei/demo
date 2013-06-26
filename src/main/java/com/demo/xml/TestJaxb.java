package com.demo.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.InputSource;

public class TestJaxb {

	public static void main(String[] args) throws JAXBException, UnsupportedEncodingException, InterruptedException, ExecutionException {
		TestXml jaxbElement = new TestXml();
		jaxbElement.setName("jiawei");
		jaxbElement.setList(Arrays.asList("ÄãºÃ","world"));
		JAXBContext context = JAXBContext.newInstance(TestXml.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
		marshaller.marshal(jaxbElement, System.out);
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testXml><listElement><list>asd</list><list>world</list></listElement></testXml>";
		jaxbElement = null;
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
		jaxbElement = (TestXml) context.createUnmarshaller().unmarshal(new InputSource(new InputStreamReader(is,"GBK")));
		System.out.println();
		System.out.println(jaxbElement.getList());
//		ExecutorService executor = Executors.newSingleThreadExecutor();
//		Future<Integer> future = executor.submit(new Callable<Integer>() {
//			
//			public Integer call() {
//				while(!Thread.currentThread().isInterrupted()){
//					System.out.println("tests");
//					
//				}
//				return 1000;
//			}
//
//					});
//		Thread.sleep(1000);
//		executor.shutdown();
//		executor.shutdownNow();
//		
//		Thread.sleep(1000);
//		System.out.println("isdone:"+future.isDone());
//		System.out.println("isterminated:"+executor.isTerminated());
//		System.out.println(future.get());
	}
}
