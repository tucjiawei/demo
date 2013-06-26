package com.demo.util;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.springframework.web.context.ContextLoader;


public class ScanPackage {
	public static void main(String[] args) throws Exception {
		List<String> result = new ScanPackage().getClassNameList("org.springframework.web.context", ContextLoader.class,true);
		for(String name:result){
			System.out.println(name);
		}
	}

	public List<String> getClassNameList(String prefix,Class<?> clazz,boolean isRecurse) throws Exception{
		String pathName = clazz.getName().replace(".", "/")+".class";
		URL url =clazz.getClassLoader().getResource(pathName);
		prefix = prefix.replace(".", "/");
		List<String> result = null;
		if(url.getProtocol().indexOf("jar")!=-1){
			result = doScanPackageFromJar(prefix,url,isRecurse);
		}else{
			result = doScanPackageFromFs(prefix,url,isRecurse);
		}
		return result;
	}

	private List<String> doScanPackageFromJar(String prefix, URL url, boolean isRecurse) throws IOException {
		List<String> result = new ArrayList<String>();
		JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
		Enumeration<JarEntry> entries = jar.entries();
		while(entries.hasMoreElements()){
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if(name.startsWith(prefix)){
				if(isRecurse&&Pattern.matches(prefix+"/[\\w/]*\\.class", name)){
					result.add(name);
				}
				else if(Pattern.matches(prefix+"/\\w+\\.class", name)){
					result.add(name);
				}
			}
		}
		return result;
	}

	private List<String> doScanPackageFromFs(String prefix, URL url, boolean isRecurse) {
		List<String> result = new ArrayList<String>();
		String pathName = url.getPath().substring(0,url.getPath().lastIndexOf(prefix)+prefix.length());
		File f= new File(pathName);
		iterate(prefix,f,result,isRecurse);
		return result;
	}

	private void iterate(String prefix, File f, List<String> result, boolean isRecurse) {
		if(f.isDirectory()){
			for(File file:f.listFiles()){
				if(file.isFile()&&file.getName().endsWith("class")){
					result.add(prefix+"/"+file.getName());
				}else if(isRecurse&&file.isDirectory()){
					iterate(prefix+"/"+file.getName(),file,result,isRecurse);
				}
			}
		}
	}
}
