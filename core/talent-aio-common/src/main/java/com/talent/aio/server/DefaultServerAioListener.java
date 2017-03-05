/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月4日 下午9:39:34
 *
 * **************************************************************************
 */
package com.talent.aio.server;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.server.intf.ServerAioListener;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月4日 下午9:39:34
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月4日 | tanyaowu | 新建类
 *
 */
public class DefaultServerAioListener<SessionContext, P extends Packet, R> implements ServerAioListener<SessionContext, P, R>
{
	
	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月4日 下午9:39:34
	 * 
	 */
	public static void main(String[] args)
	{}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterConnected(com.talent.aio.common.ChannelContext, boolean, boolean)
	 * 
	 * @param channelContext
	 * @param isConnected
	 * @param isReconnect
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:40:14
	 * 
	 */
	@Override
	public void onAfterConnected(ChannelContext<SessionContext, P, R> channelContext, boolean isConnected, boolean isReconnect)
	{}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, boolean)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param isSentSuccess
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:40:14
	 * 
	 */
	@Override
	public void onAfterSent(ChannelContext<SessionContext, P, R> channelContext, P packet, boolean isSentSuccess)
	{}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterReceived(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:40:14
	 * 
	 */
	@Override
	public void onAfterReceived(ChannelContext<SessionContext, P, R> channelContext, P packet, int packetSize)
	{}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String, boolean)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @param isRemove
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:40:14
	 * 
	 */
	@Override
	public void onAfterClose(ChannelContext<SessionContext, P, R> channelContext, Throwable throwable, String remark, boolean isRemove)
	{}

//	/** 
//	 * @see com.talent.aio.server.intf.ServerAioListener#onAfterAccepted(java.nio.channels.AsynchronousSocketChannel, com.talent.aio.server.AioServer)
//	 * 
//	 * @param asynchronousSocketChannel
//	 * @param aioServer
//	 * @return
//	 * @重写人: tanyaowu
//	 * @重写时间: 2017年2月4日 下午9:40:14
//	 * 
//	 */
//	@Override
//	public boolean onAfterAccepted(AsynchronousSocketChannel asynchronousSocketChannel, AioServer<SessionContext, P, R> aioServer)
//	{
//		// TODO Auto-generated method stub
//		return true;
//	}

}
