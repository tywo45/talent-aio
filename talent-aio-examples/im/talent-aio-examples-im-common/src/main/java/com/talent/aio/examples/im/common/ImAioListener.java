/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月8日 下午1:22:27
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.AioListener;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月8日 下午1:22:27
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月8日 | tanyaowu | 新建类
 *
 */
public class ImAioListener implements AioListener<Object, ImPacket, Object>
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:22:27
	 * 
	 */
	public ImAioListener()
	{}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:22:27
	 * 
	 */
	public static void main(String[] args)
	{}



	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月8日 下午1:23:18
	 * 
	 */
	@Override
	public void onAfterSent(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int packetSize)
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
	 * @重写时间: 2016年12月15日 上午10:23:42
	 * 
	 */
	@Override
	public void onAfterDecoded(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int packetSize)
	{
		CommandStat.getCount(packet.getCommand()).received.incrementAndGet();
		
	}

}
