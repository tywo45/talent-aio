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
package com.talent.aio.examples.helloworld.client;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.Node;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.examples.helloworld.common.Const;
import com.talent.aio.examples.helloworld.common.HelloPacket;

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
	private static Node serverNode = null;
	private static AioClient<Object, HelloPacket, Object> aioClient;
	private static ClientGroupContext<Object, HelloPacket, Object> clientGroupContext = null;
	private static ClientAioHandler<Object, HelloPacket, Object> aioClientHandler = null;
	private static ClientAioListener<Object, HelloPacket, Object> aioListener = null;
	private static ReconnConf<Object, HelloPacket, Object> reconnConf = new ReconnConf<Object, HelloPacket, Object>(5000L);//用来自动连接的，不想自动连接请传null

	public static void main(String[] args) throws Exception
	{
		String serverIp = "127.0.0.1";
		int serverPort = Const.PORT;
		serverNode = new Node(serverIp, serverPort);
		aioClientHandler = new HelloClientAioHandler();
		aioListener = null;

		clientGroupContext = new ClientGroupContext<>(aioClientHandler, aioListener, reconnConf);
		aioClient = new AioClient<>(clientGroupContext);

		ClientChannelContext<Object, HelloPacket, Object> clientChannelContext = aioClient.connect(serverNode);

		//以下内容不是启动的过程，而是属于发消息的过程
		HelloPacket packet = new HelloPacket();
		packet.setBody("hello world".getBytes(HelloPacket.CHARSET));
		Aio.send(clientChannelContext, packet);
	}
}
