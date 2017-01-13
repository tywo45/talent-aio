/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-client
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月16日 下午5:52:06
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.server;

import java.nio.channels.AsynchronousSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.server.AioServer;
import com.talent.aio.server.intf.ServerAioListener;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月16日 下午5:52:06
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月16日 | tanyaowu | 新建类
 *
 */
public class ImServerAioListener implements ServerAioListener<Object, ImPacket, Object>
{
private static Logger log = LoggerFactory.getLogger(ImServerAioListener.class);
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public ImServerAioListener()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/** 
	 * @see com.talent.aio.examples.im.common.ImAioListener#onBeforeClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月16日 下午5:52:24
	 * 
	 */
	@Override
	public void onBeforeClose(ChannelContext<Object, ImPacket, Object> channelContext, Throwable throwable, String remark)
	{
		log.info("即将关闭连接:{}", channelContext);
	}

	/** 
	 * @see com.talent.aio.server.intf.ServerAioListener#onAfterAccepted(java.nio.channels.AsynchronousSocketChannel, com.talent.aio.server.AioServer)
	 * 
	 * @param asynchronousSocketChannel
	 * @param aioServer
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:03:45
	 * 
	 */
	@Override
	public boolean onAfterAccepted(AsynchronousSocketChannel asynchronousSocketChannel, AioServer<Object, ImPacket, Object> aioServer)
	{
		return true;
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterConnected(com.talent.aio.common.ChannelContext)
	 * 
	 * @param channelContext
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:08:44
	 * 
	 */
	@Override
	public boolean onAfterConnected(ChannelContext<Object, ImPacket, Object> channelContext)
	{
		return true;
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onBeforeSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:08:44
	 * 
	 */
	@Override
	public void onBeforeSent(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet)
	{
		CommandStat.getCount(packet.getCommand()).sent.incrementAndGet();
		
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterDecoded(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:08:44
	 * 
	 */
	@Override
	public void onAfterDecoded(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int packetSize)
	{
		CommandStat.getCount(packet.getCommand()).received.incrementAndGet();
		
	}


}
