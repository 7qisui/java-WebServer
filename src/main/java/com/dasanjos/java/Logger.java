package com.dasanjos.java;

import java.util.logging.ConsoleHandler; // 导入 ConsoleHandler 类，用于将日志输出到控制台
import java.util.logging.Level; // 导入 Level 类，用于定义日志级别

/**
 * 类 <code>Logger</code> 使用标准的 Java Logger 来无缝替换 Log4J Logger（并减少最终 jar 包的大小）。
 * 要启用 Log4J，请在 pom.xml 中添加 log4J 依赖，并将 Java 类更改为导入 org.apache.log4j.Logger。
 */
public class Logger {

	static java.util.logging.Logger logger; // 定义 Java 自带的 Logger 对象

	// 构造函数，根据传入的名称创建 Logger 实例
	public Logger(String name) {
		logger = java.util.logging.Logger.getLogger(name); // 获取 Logger 对象
		logger.setLevel(Level.INFO); // 设置日志级别为 INFO
		logger.addHandler(new ConsoleHandler()); // 添加控制台处理器，将日志输出到控制台
	}


	// 静态方法，获取 Logger 实例
	@SuppressWarnings("rawtypes") // 抑制编译器的原始类型警告
	public static Logger getLogger(Class cl) {
		return new Logger(cl.getName()); // 返回以类名为标识的 Logger 实例
	}

	// 记录错误信息和异常
	public void error(String msg, Throwable thrown) {
		logger.log(Level.SEVERE, msg, thrown); // 以 SEVERE 级别记录日志
	}

	// 记录信息
	public static void info(String msg) {
		logger.info(msg); // 以 INFO 级别记录日志
	}

	public static String getMessage(){
		return java.util.logging.Logger.GLOBAL_LOGGER_NAME;
	}
}
