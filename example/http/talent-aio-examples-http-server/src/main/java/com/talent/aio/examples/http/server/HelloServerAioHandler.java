/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:13:15
 *
 * **************************************************************************
 */
package com.talent.aio.examples.http.server;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.http.common.HelloAbsAioHandler;
import com.talent.aio.examples.http.common.HelloPacket;
import com.talent.aio.server.intf.ServerAioHandler;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:13:15
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class HelloServerAioHandler extends HelloAbsAioHandler implements ServerAioHandler<Object, HelloPacket, Object>
{
	/** 
	 * 处理消息
	 */
	@Override
	public Object handler(HelloPacket packet, ChannelContext<Object, HelloPacket, Object> channelContext) throws Exception
	{
		byte[] body = packet.getBody();
		if (body != null)
		{
			String str = new String(body, HelloPacket.CHARSET);
			System.out.println("收到消息：" + str);

			HelloPacket resppacket = new HelloPacket();
			resppacket.setBody(("收到了你的消息，你的消息是:" + str).getBytes(HelloPacket.CHARSET));
			Aio.send(channelContext, resppacket);
		}
		return null;
	}
}
