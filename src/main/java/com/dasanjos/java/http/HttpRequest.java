package com.dasanjos.java.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpRequest class parses the HTTP Request Line (method, URI, version) 
 * and Headers http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
 */
public class HttpRequest {

	private static Logger log = Logger.getLogger(HttpRequest.class);

	List<String> headers = new ArrayList<String>();

	Method method;

	String uri;

	String version;

	//用于从输入流中读取 HTTP 请求，并解析请求行和请求头
	public HttpRequest(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String str = reader.readLine();
		parseRequestLine(str);

		while (!str.equals("")) {
			str = reader.readLine();
			parseRequestHeader(str);
		}
	}

	//解析 HTTP 请求行，并将其拆分为方法、URI 和版本
	private void parseRequestLine(String str) {
		log.info(str);
		String[] split = str.split("\\s+");	//这行代码将请求行按照空白字符（空格、制表符等）拆分为多个部分。
		try {
			method = Method.valueOf(split[0]);
		} catch (Exception e) {
			method = Method.UNRECOGNIZED;
		}
		uri = split[1];
		version = split[2];
	}

	//解析 HTTP 请求头，并将其添加到 headers 列表中
	private void parseRequestHeader(String str) {
		log.info(str);			//日志记录器记录请求头的内容
		headers.add(str);		//将请求头行添加到 headers 列表中
	}

}
