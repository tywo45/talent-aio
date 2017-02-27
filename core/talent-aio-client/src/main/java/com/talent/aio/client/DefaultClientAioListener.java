/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月4日 下午9:46:05
 *
 * **************************************************************************
 */
package com.talent.aio.client;

import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月4日 下午9:46:05
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月4日 | tanyaowu | 新建类
 *
 */
public class DefaultClientAioListener<SessionContext, P extends Packet, R> implements ClientAioListener<SessionContext, P, R>
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月4日 下午9:46:05
	 * 
	 */
	public DefaultClientAioListener()
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterConnected(com.talent.aio.common.ChannelContext, boolean, boolean)
	 * 
	 * @param channelContext
	 * @param isConnected
	 * @param isReconnect
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:46:22
	 * 
	 */
	@Override
	public void onAfterConnected(ChannelContext<SessionContext, P, R> channelContext, boolean isConnected, boolean isReconnect)
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, boolean)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param isSentSuccess
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:46:22
	 * 
	 */
	@Override
	public void onAfterSent(ChannelContext<SessionContext, P, R> channelContext, P packet, boolean isSentSuccess)
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterReceived(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:46:22
	 * 
	 */
	@Override
	public void onAfterReceived(ChannelContext<SessionContext, P, R> channelContext, P packet, int packetSize)
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String, boolean)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @param isRemove
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:46:22
	 * 
	 */
	@Override
	public void onAfterClose(ChannelContext<SessionContext, P, R> channelContext, Throwable throwable, String remark, boolean isRemove)
	{
	}

	/** 
	 * @see com.talent.aio.client.intf.ClientAioListener#onAfterReconnected(com.talent.aio.common.ChannelContext, boolean)
	 * 
	 * @param channelContext
	 * @param isConnected
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月4日 下午9:46:22
	 * 
	 */
//	@Override
//	public void onAfterReconnected(ChannelContext<SessionContext, P, R> channelContext, boolean isConnected)
//	{
//	}

}
