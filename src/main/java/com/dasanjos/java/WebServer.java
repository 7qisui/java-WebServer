package com.dasanjos.java;  // 定义包名

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;  // 导入 IOException 类
import java.net.ServerSocket;  // 导入 ServerSocket 类
import java.util.concurrent.ExecutorService;  // 导入 ExecutorService 接口
import java.util.concurrent.Executors;  // 导入 Executors 类

import com.dasanjos.java.http.HttpResponse;
import org.apache.log4j.Logger;  // 导入 log4j Logger 类

import javax.swing.*;

/**
 * Class <code>WebServer</code> - Main class that starts the Web Server Thread Pool in port 8080 (default)
 * WebServer 类是启动 Web 服务器线程池的主类，默认端口为 8000
 */
public class WebServer extends JFrame implements ActionListener {

	static String []arry;
	static boolean running = false;
	private ServerSocket s;
	private ExecutorService executor;
	static int couter = 1;

	static int port;
	// GUI 组件
	static JPanel panel = new JPanel();
	static JLabel state = new JLabel("关闭状态        ");
	static JTextField portText = new JTextField("8080", 8);

	static JButton start = new JButton("开始", new ImageIcon("images/start.gif"));
	static JButton stop = new JButton("停止", new ImageIcon("images/stop.gif"));
	static JButton clear = new JButton("清空", new ImageIcon("images/clear.gif"));
	static JTextArea textArea = new JTextArea();
	static JScrollPane js = new JScrollPane(textArea);

	public WebServer(){
		// 设置框架
		super("服务器");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 添加组件到面板
		// 设置字体为 Arial, 加粗, 大小为 40
		Font font = new Font("宋体", Font.BOLD, 20);
		textArea.setFont(font);

		panel.add(new JLabel("端口:"));
		panel.add(portText);
		panel.add(start);
		panel.add(stop);
		panel.add(clear);
		panel.add(state);

		// 将面板和滚动条添加到框架
		add(panel, "North");
		add(js, "Center");

		// 注册按钮监听器
		start.addActionListener(this);
		stop.addActionListener(this);
		clear.addActionListener(this);

		// 停止按钮初始不可用
//		stop.setEnabled(false);
	}


	private static Logger log = Logger.getLogger(WebServer.class);  // 使用 log4j 记录日志

	private static final int DEFAULT_PORT = 8000;  // 默认端口号

	private static final int N_THREADS = 3;  // 线程池中线程数量

//	public static void main(String args[]) {
//		try {
//			new WebServer().start(getValidPortParam(args));  // 创建 WebServer 对象并启动服务器，端口由命令行参数决定
//		} catch (Exception e) {
//			log.error("Startup Error", e);  // 启动过程中捕获异常并记录错误日志
//		}
//	}

	//************
	@Override
	public void actionPerformed(ActionEvent e) {

		port = getValidPortParam();	//获取文本作为端口号

		if (e.getSource() == start){	//启动服务器
			state.setText("开启状态        ");
			start.setEnabled(false);	//启动后,start 不可用,stop 可用
			stop.setEnabled(true);

			running = true;		//服务器开启，可以进行请求
			try{
				textArea.insert("服务器已开启\n",0);
				textArea.append("服务器已启动，端口: " + getValidPortParam() + "\n");
				textArea.append("......");

				s = new ServerSocket(port);  // 创建 ServerSocket 对象并绑定到指定端口
				System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)");  // 打印服务器启动信息
				//匿名内部类
				// 创建一条新线程出来 创建 Thread类的对象
				new Thread(){
					public void run(){
						try {
							ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);  // 创建固定大小的线程池
							//无限循环，用于接受多个客户请求
							while (running) {
								executor.submit(new RequestHandler(s.accept()));  // 接受客户端连接，并将处理任务提交给线程池
								textArea.append("\n报告，收到请求：  " + String.valueOf(couter++)+"\n");
								textArea.append("全局："+com.dasanjos.java.Logger.getMessage()+"\n");
								textArea.append("Http版本："+HttpResponse.VERSION+"\n");
								textArea.append("URL："+HttpResponse.requestedUri+"\n");
//								textArea.append(com.dasanjos.java.Logger.info());
								if (e.getSource() == stop){
									s.close();
								}
							}
						}catch (Exception e){}
					};
				}.start();
			}catch (Exception e1){}
		}
		else if (e.getSource() == stop) {
			//服务器关闭，不能进行请求访问
			running = false;
			//关闭服务器套接字
//			try{
//				if(serverSocket != null)
//				{
//					serverSocket.close();
//					textArea.insert("\n服务器已关闭\n",0);
//				}
//			}catch(Exception e2){}
			try {
//				System.out.println("让我看看这部分代码有没有被执行！");
				running = false;
				couter = 1;		//重置计数
//				new WebServer().stop();  // 停止服务器
				if (s != null) {
					s.close(); // 关闭 ServerSocket
				}
				if (executor != null) {
					executor.shutdown(); // 关闭线程池
				}
				textArea.append("\n服务器已关闭\n");
			} catch (Exception e2) {
				log.error("Stop Error", e2);  // 停止过程中捕获异常并记录错误日志
			}
			state.setText("关闭状态     ");
			stop.setEnabled(false);
			start.setEnabled(true);
			JOptionPane.showMessageDialog(this, "服务器成功关闭！");
		} else if (e.getSource() == clear) {
			textArea.setText("");
		}
	}

//	private void startServer(){
//
//		state.setText("运行状态");
//		start.setEnabled(false);
//		stop.setEnabled(true);
//		textArea.append("服务器已启动，端口: " + port + "\n");
//
//		try {
//			new WebServer().start(getValidPortParam());  // 创建 WebServer 对象并启动服务器，端口由命令行参数决定
//		} catch (Exception e) {
//			log.error("Startup Error", e);  // 启动过程中捕获异常并记录错误日志
//		}
//	}

//	private ExecutorService serverExecutor = Executors.newSingleThreadExecutor();  // 创建单线程线程池
//	private void startServer() {
//
////		state.setText("运行状态");
////		start.setEnabled(false);
////		stop.setEnabled(true);
////		textArea.append("服务器已启动，端口: " + getValidPortParam() + "\n");
//
//		running  = true;
//
//		// 创建新线程来启动服务器
////		Thread serverThread2 = new Thread(() -> {
//		serverExecutor.submit(()->{
//			try {
//				new WebServer().start(getValidPortParam());  // 创建 WebServer 对象并启动服务器，端口由命令行参数决定
//			} catch (Exception e) {
//				log.error("Startup Error", e);  // 启动过程中捕获异常并记录错误日志
//			}
//		});
//
//
//		// 启动新线程
////		serverThread2.start();
//	}
//
//	private void stopServer() {
//		// 更新界面状态
//		state.setText("关闭状态");
//		start.setEnabled(true);
//		stop.setEnabled(false);
//		textArea.append("服务器已停止\n");
//
////		// 创建新线程来停止服务器
////		Thread stopThread = new Thread(() -> {
////			try {
////				new WebServer().stop();  // 停止服务器
////			} catch (Exception e) {
////				log.error("Stop Error", e);  // 停止过程中捕获异常并记录错误日志
////			}
////		});
////
////		// 启动新线程
////		stopThread.start();
//	}
//
//
////	private void stopServer(){
////
////
////		try{
////			new WebServer().stop();	//停止服务器
////		}catch(Exception e){
////			log.error("Stop Error", e);
////		}
////		state.setText("关闭状态");
////		start.setEnabled(true);
////		stop.setEnabled(false);
////		textArea.append("服务器已停止\n");
////	}
//
//	public void start(int port) throws IOException {
//
////			state.setText("运行状态");
////			running = true;
////			start.setEnabled(false);
////			stop.setEnabled(true);
////			textArea.append("服务器已启动，端口: " + port + "\n");
//
//			// 启动新线程来接受客户端连接
////			new Thread(() -> {
//		serverSocket = new ServerSocket(port); // 创建 ServerSocket 对象并绑定到指定端口
//		System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)"); // 打印服务器启动信息
//		executor = Executors.newFixedThreadPool(N_THREADS); // 创建固定大小的线程池
//		new Thread(() -> {
//            while (running) {
//                try {
//                    executor.submit(new RequestHandler(serverSocket.accept())); // 接受客户端连接，并将处理任务提交给线程池
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
////			}).start();
//
////		serverSocket = new ServerSocket(port); // 创建 ServerSocket 对象并绑定到指定端口
////		System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)"); // 打印服务器启动信息
////		executor = Executors.newFixedThreadPool(N_THREADS); // 创建固定大小的线程池
////		port = Integer.parseInt(portText.getText());
////		running = true; // 设置服务器运行状态
////		state.setText("运行状态");
//////		System.out.println("****************************************");
////		start.setEnabled(false);
////		stop.setEnabled(true);
////		textArea.append("服务器已启动，端口: " + port + "\n");
//		// 使用 runOnUiThread 确保 UI 更新在主线程上执行
////		runOnUiThread(new Runnable() {
////			@Override
////			public void run() {
////				state.setText("运行状态");
////				start.setEnabled(false);
////				stop.setEnabled(true);
////				textArea.append("服务器已启动，端口: " + port + "\n");
////			}
////		});
//
//
////		serverSocket = new ServerSocket(port); // 创建 ServerSocket 对象并绑定到指定端口
////		System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)"); // 打印服务器启动信息
////		executor = Executors.newFixedThreadPool(N_THREADS); // 创建固定大小的线程池
////
////		while (running) {
////			stop.setEnabled(true);
////			try {
////				executor.submit(new RequestHandler(serverSocket.accept())); // 接受客户端连接，并将处理任务提交给线程池
////			} catch (IOException e) {
////				if (!running) { // 如果不是由于关闭服务器而引发的异常，则打印异常信息
////					System.out.println("Server stopped.");
////				} else {
////					throw e; // 否则重新抛出异常
////				}
////			}
////		}
//	}
//
////	private void runOnUiThread(Runnable running) {
////		SwingUtilities.invokeLater(running);
////	}
//
//	public void stop() throws IOException {
//		running = false; // 设置服务器运行状态为停止
//		if (serverSocket != null && !serverSocket.isClosed()) {
//			serverSocket.close(); // 关闭 ServerSocket
//		}
//		if (executor != null && !executor.isShutdown()) {
//			executor.shutdown(); // 关闭线程池
//		}
//	}

//	public void start(int port) throws IOException {
//		ServerSocket s = new ServerSocket(port);  // 创建 ServerSocket 对象并绑定到指定端口
//		System.out.println("Web server listening on port " + port + " (press CTRL-C to quit)");  // 打印服务器启动信息
//		ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);  // 创建固定大小的线程池
//		while (true) {
//			executor.submit(new RequestHandler(s.accept()));  // 接受客户端连接，并将处理任务提交给线程池
//		}
//	}

	/**
	 * Parse command line arguments (string[] args) for valid port number
	 * 解析命令行参数，获取有效的端口号
	 *
	 * @return int valid port number or default value (8080)
	 * 返回有效的端口号，如果无效则返回默认值（8080）
	 */
	static int getValidPortParam() throws NumberFormatException {
//		if (args.length > 0) {
//			int port = Integer.parseInt(args[0]);  // 将第一个参数解析为整数
//			if (port > 0 && port < 65535) {  // 检查端口号是否在有效范围内
//				return port;  // 返回有效端口号
//			} else {
//				throw new NumberFormatException("Invalid port! Port value is a number between 0 and 65535");  // 抛出异常
//			}
//		}
//		//System.out.println(args);
//		return DEFAULT_PORT;  // 如果没有提供参数，返回默认端口号
		port = Integer.parseInt(portText.getText());
		if (port > 0 && port < 65535) {  // 检查端口号是否在有效范围内
				return port;  // 返回有效端口号
			} else {
				throw new NumberFormatException("Invalid port! Port value is a number between 0 and 65535");  // 抛出异常
			}
//		return DEFAULT_PORT;  // 如果没有提供参数，返回默认端口号
	}
	static int getValidPortParam1(String []args) throws NumberFormatException {
		if (args.length > 0) {
			int port = Integer.parseInt(args[0]);  // 将第一个参数解析为整数
			if (port > 0 && port < 65535) {  // 检查端口号是否在有效范围内
				return port;  // 返回有效端口号
			} else {
				throw new NumberFormatException("Invalid port! Port value is a number between 0 and 65535");  // 抛出异常
			}
		}
		//System.out.println(args);
		return DEFAULT_PORT;  // 如果没有提供参数，返回默认端口号
	}

	public static void main(String[] args) {
		//赋值到全局变量
		arry = args;
		SwingUtilities.invokeLater(() -> {
			WebServer frame = new WebServer();
			frame.setVisible(true);
		});
	}
}
