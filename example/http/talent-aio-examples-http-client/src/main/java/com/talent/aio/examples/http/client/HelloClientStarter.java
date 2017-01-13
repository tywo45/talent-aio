/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月17日 下午5:59:24
 *
 * **************************************************************************
 */
package com.talent.aio.examples.http.client;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.examples.http.common.HelloPacket;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月17日 下午5:59:24
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 *
 */
public class HelloClientStarter
{
	private static String serverIp = null; //服务器的IP地址
	private static int serverPort = 0; //服务器的PORT
	private static AioClient<Object, HelloPacket, Object> aioClient;
	private static ClientGroupContext<Object, HelloPacket, Object> clientGroupContext = null;
	private static ClientAioHandler<Object, HelloPacket, Object> aioClientHandler = null;
	private static ClientAioListener<Object, HelloPacket, Object> aioListener = null;
	private static ReconnConf<Object, HelloPacket, Object> reconnConf = new ReconnConf<Object, HelloPacket, Object>(5000L);//用来自动连接的，不想自动连接请传null

	public static void main(String[] args) throws Exception
	{
		serverIp = "127.0.0.1";
		serverPort = com.talent.aio.examples.http.common.Const.PORT;
		aioClientHandler = new HelloClientAioHandler();
		aioListener = null;

		clientGroupContext = new ClientGroupContext<>(serverIp, serverPort, aioClientHandler, aioListener, reconnConf);
		aioClient = new AioClient<>(clientGroupContext);

		String bindIp = null;
		int bindPort = 0;
		ClientChannelContext<Object, HelloPacket, Object> clientChannelContext = aioClient.connect(bindIp, bindPort);

		//以下内容不是启动的过程，而是属于发消息的过程
		HelloPacket packet = new HelloPacket();
		packet.setBody("hello world".getBytes(HelloPacket.CHARSET));
		Aio.send(clientChannelContext, packet);
	}
}
