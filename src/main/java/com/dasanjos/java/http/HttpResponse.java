package com.dasanjos.java.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;


/**
 * HttpResponse class defines the HTTP Response Status Line (method, URI, version)
 * and Headers http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html
 */
public class HttpResponse {

	private static Logger log = Logger.getLogger(HttpResponse.class);

	public static final String VERSION = "HTTP/1.0";

	public static String requestedUri;

	List<String> headers = new ArrayList<String>();

	byte[] body;

	public HttpResponse(HttpRequest req) throws IOException {

		switch (req.method) {
			case HEAD:
				fillHeaders(Status._200);
				break;
			case GET:
				try {
					requestedUri = req.uri; // 获取请求的URI
					File file = new File("." + requestedUri); // 根据URI构建File对象

					if (file.isDirectory()) {
						// 如果是目录，显示目录内容和上传表单
						fillHeaders(Status._200); // 设置响应状态为200

						// 构建HTML响应
						StringBuilder result = new StringBuilder("<html>");
						result.append("<meta charset=\"UTF-8\">");  // 指定字符编码为 UTF-8
						result.append("<head><title>计算机网络课程设计-陈宇璐-孙驰-任海涵");
						result.append(requestedUri);
						result.append("</title>");
						result.append("<style>");
						result.append("body { font-family: Arial, sans-serif; }");
						result.append("h1 { color: #333; }");
						result.append("a { text-decoration: none; color: #007BFF; }");
						result.append("a:hover { text-decoration: underline; }");
						result.append("table { width: 100%; border-collapse: collapse; }");
						result.append("th, td { padding: 8px 12px; border: 1px solid #ccc; text-align: left; }");
						result.append("th { background-color: #f4f4f4; }");
						result.append("</style>");
						result.append("</head><body><h1>Index of ");
						result.append(requestedUri);
						result.append("</h1><hr>");

						result.append("<table><tr><th>Name</th><th>Size</th><th>Last Modified</th></tr>");

						// 添加父目录链接
						if (!requestedUri.equals("/")) {
							String parentUri = requestedUri.endsWith("/") ? requestedUri.substring(0, requestedUri.length() - 1) : requestedUri;
							parentUri = parentUri.substring(0, parentUri.lastIndexOf('/'));
							result.append("<tr><td><a href=\"").append(parentUri.isEmpty() ? "/" : parentUri).append("\">parent directory</a></td><td></td><td></td></tr>");
						}

						// 列出目录内容
						File[] files = file.listFiles();
						if (files != null) {
							for (File subfile : files) {
								String subfileUri = requestedUri + "/" + subfile.getName();
								subfileUri = subfileUri.replaceAll("/+", "/"); // 规范化URI
								result.append("<tr><td><a href=\"").append(subfileUri).append("\">").append(subfile.getName()).append("</a></td>");
								result.append("<td>").append(subfile.isDirectory() ? "-" : subfile.length() + " bytes").append("</td>");
								result.append("<td>").append(new java.util.Date(subfile.lastModified()).toString()).append("</td></tr>");
							}
						}
						result.append("</table><hr></body></html>");
						fillResponse(result.toString()); // 填充响应内容

					} else if (file.exists()) {
						// 如果是文件，返回文件内容
						fillHeaders(Status._200); // 设置响应状态为200
						setContentType(requestedUri, headers); // 设置响应内容类型
						fillResponse(getBytes(file)); // 获取文件内容并填充响应
					} else {
						// 文件不存在，返回404错误
						// 构建HTML响应
						StringBuilder sresult = new StringBuilder();
						sresult.append("<html>")
								.append("<head>")
								.append("<meta charset=\"UTF-8\">")  // 指定字符编码为 UTF-8
								.append("<title>404 Page Not Found</title>")
								.append("<style>")
								.append("body { font-family: 'Arial', sans-serif; background-color: #f0f0f0; text-align: center; }")
								.append("h1 { color: #333333; }")
								.append("p { color: #666666; }")
								.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border-radius: 5px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }")
								.append("</style>")
								.append("</head>")
								.append("<body>")
								.append("<div class=\"container\">")
								.append("<header style=\"background-color: #ff6347; color: #ffffff; padding: 10px; border-radius: 5px 5px 0 0;\">")
								.append("<h1>404 Page Not Found</h1>")
								.append("</header>")
								.append("<main style=\"margin: 20px;\">")
								.append("<img src=\"images/404.jpg\" alt=\"404 Not Found\" style=\"max-width: 100%; height: auto; border-radius: 5px;\">")
								.append("<p>Sorry, the page you are looking for could not be found.</p>")
								.append("<p><a href=\"/\">Go to Home Page</a></p>")
								.append("</main>")
								.append("<footer style=\"background-color: #ff6347; color: #ffffff; padding: 10px; border-radius: 0 0 5px 5px;\">")
								.append("<h3>&copy; 2024 - 陈宇璐 - 孙驰 - 任海涵 - 计算机网络课程设计.</h3>")
								.append("</footer>")
								.append("</div>")
								.append("</body>")
								.append("</html>");

						log.info("文件未找到：" + requestedUri);
						fillHeaders(Status._404); // 设置响应状态为404
//						fillResponse(Status._404.toString()); // 填充404响应内容
						fillResponse(sresult.toString()); // 填充404响应内容
					}
				} catch (Exception e) {
					// 处理异常情况，返回500错误
					log.error("处理URI请求时出错：" + req.uri, e);
					fillHeaders(Status._500); // 设置响应状态为500
					fillResponse(Status._500.toString()); // 填充500响应内容
				}
				break;


			case UNRECOGNIZED:
				fillHeaders(Status._400);
				fillResponse(Status._400.toString());
				break;
			default:
				fillHeaders(Status._501);
				fillResponse(Status._501.toString());
		}

	}

	private byte[] getBytes(File file) throws IOException {
		int length = (int) file.length();
		byte[] array = new byte[length];
		InputStream in = new FileInputStream(file);
		int offset = 0;
		while (offset < length) {
			int count = in.read(array, offset, (length - offset));
			offset += count;
		}
		in.close();
		return array;
	}

	private void fillHeaders(Status status) {
		headers.add(HttpResponse.VERSION + " " + status.toString());
		headers.add("Connection: close");
		headers.add("Server: SimpleWebServer");
	}

	private void fillResponse(String response) {

		body = response.getBytes();
	}

	private void fillResponse(byte[] response) {
		body = response;
	}

	public void write(OutputStream os) throws IOException {
		DataOutputStream output = new DataOutputStream(os);
		for (String header : headers) {
			output.writeBytes(header + "\r\n");
		}
		output.writeBytes("\r\n");
		if (body != null) {
			output.write(body);
		}
		output.writeBytes("\r\n");
		output.flush();
	}

	//根据给定的 URI 确定内容类型，并将其添加到一个列表中
	private void setContentType(String uri, List<String> list) {
		try {
			String ext = uri.substring(uri.indexOf(".") + 1);
			list.add(ContentType.valueOf(ext.toUpperCase()).toString());
		} catch (Exception e) {
			log.error("ContentType not found: " + e, e);
		}
	}
}
