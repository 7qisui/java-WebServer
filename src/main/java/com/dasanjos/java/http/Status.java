package com.dasanjos.java.http;

/**
 * 枚举 <code>Status</code> 映射了 HTTP/1.1 可用的响应状态码
 * 参考：http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html
 */
public enum Status {
	_100("100 Continue"), // 100 继续
	_101("101 Switching Protocols"), // 101 切换协议
	_200("200 OK"), // 200 成功
	_201("201 Created"), // 201 已创建
	_202("202 Accepted"), // 202 已接受
	_203("203 Non-Authoritative Information"), // 203 非权威信息
	_204("204 No Content"), // 204 无内容
	_205("205 Reset Content"), // 205 重置内容
	_206("206 Partial Content"), // 206 部分内容
	_300("300 Multiple Choices"), // 300 多种选择
	_301("301 Moved Permanently"), // 301 永久移动
	_302("302 Found"), // 302 临时移动
	_303("303 See Other"), // 303 查看其他位置
	_304("304 Not Modified"), // 304 未修改
	_305("305 Use Proxy"), // 305 使用代理
	_307("307 Temporary Redirect"), // 307 临时重定向
	_400("400 Bad Request"), // 400 错误请求
	_401("401 Unauthorized"), // 401 未授权
	_402("402 Payment Required"), // 402 需要付款
	_403("403 Forbidden"), // 403 禁止
	_404("404 Not Found"), // 404 未找到
	_405("405 Method Not Allowed"), // 405 方法不允许
	_406("406 Not Acceptable"), // 406 不接受
	_407("407 Proxy Authentication Required"), // 407 代理认证要求
	_408("408 Request Time-out"), // 408 请求超时
	_409("409 Conflict"), // 409 冲突
	_410("410 Gone"), // 410 已删除
	_411("411 Length Required"), // 411 需要长度
	_412("412 Precondition Failed"), // 412 前提条件失败
	_413("413 Request Entity Too Large"), // 413 请求实体过大
	_414("414 Request-URI Too Large"), // 414 请求URI过长
	_415("415 Unsupported Media Type"), // 415 不支持的媒体类型
	_416("416 Requested range not satisfiable"), // 416 请求范围不满足
	_417("417 Expectation Failed"), // 417 期望失败
	_500("500 Internal Server Error"), // 500 服务器内部错误
	_501("501 Not Implemented"), // 501 未实现
	_502("502 Bad Gateway"), // 502 错误网关
	_503("503 Service Unavailable"), // 503 服务不可用
	_504("504 Gateway Time-out"), // 504 网关超时
	_505("505 HTTP Version not supported"); // 505 不支持的HTTP版本

	private final String status; // 定义状态码的字符串表示

	// 构造函数，初始化状态码字符串
	Status(String status) {
		this.status = status;
	}

	// 重写 toString 方法，返回状态码的字符串表示
	@Override
	public String toString() {
		return status;
	}
}
