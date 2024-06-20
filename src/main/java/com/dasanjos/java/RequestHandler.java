package com.dasanjos.java;

import java.net.Socket; // 导入 Socket 类用于网络通信

import org.apache.log4j.Logger; // 导入 Logger 类用于日志记录

import com.dasanjos.java.http.HttpRequest; // 导入自定义包中的 HttpRequest 类
import com.dasanjos.java.http.HttpResponse; // 导入自定义包中的 HttpResponse 类

/**
 * 类 <code>RequestHandler</code> - 线程类，用于处理套接字中的请求
 */
public class RequestHandler implements Runnable {

	private static Logger log = Logger.getLogger(RequestHandler.class); // 定义日志记录器，用于记录运行时信息

	private Socket socket; // 定义套接字变量

	// 构造函数，初始化套接字
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	// 重写 run 方法，处理请求
	public void run() {
		try {
			// 从套接字的输入流中创建一个 HttpRequest 对象
			HttpRequest req = new HttpRequest(socket.getInputStream());

			// 根据请求创建一个 HttpResponse 对象
			HttpResponse res = new HttpResponse(req);

			// 将响应写入套接字的输出流
			res.write(socket.getOutputStream());

			// 处理完请求后关闭套接字
			socket.close();
		} catch (Exception e) {
			// 如果运行过程中出现异常，记录错误信息
			log.error("运行时错误", e);
		}
	}
}
