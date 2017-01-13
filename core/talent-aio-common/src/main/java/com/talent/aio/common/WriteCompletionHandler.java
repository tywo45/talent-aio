/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午1:31:04
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午1:31:04
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class WriteCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<Integer, Integer>
{

	private static Logger log = LoggerFactory.getLogger(WriteCompletionHandler.class);

	private ChannelContext<Ext, P, R> channelContext = null;

	private java.util.concurrent.Semaphore writeSemaphore = new Semaphore(1);
	//	private P packet;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public WriteCompletionHandler(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param result
	 * @param packetCount
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void completed(Integer result, Integer packetCount)
	{
		this.writeSemaphore.release();
		if (result > 0)
		{
			GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
			GroupStat groupStat = groupContext.getGroupStat();

			groupStat.getSentPacket().addAndGet(packetCount);//.incrementAndGet();
			groupStat.getSentBytes().addAndGet(result);
			channelContext.getStat().setTimeLatestSentMsg(SystemTimer.currentTimeMillis());
		} else if (result == 0)
		{
			log.error("发送长度为{}", result);
			Aio.close(channelContext, "写数据返回:" + result);
		} else if (result < 0)
		{
			log.error("发送长度为{}", result);
			Aio.close(channelContext, "写数据返回:" + result);
		}

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void failed(Throwable exc, Integer packetCount)
	{
		try
		{
			this.writeSemaphore.release();
		} finally
		{
			Aio.close(channelContext, exc, "写数据时发生异常");
		}

	}

	//	/**
	//	 * @return the packet
	//	 */
	//	public P getPacket()
	//	{
	//		return packet;
	//	}
	//
	//
	//	/**
	//	 * @param packet the packet to set
	//	 */
	//	public void setPacket(P packet)
	//	{
	//		this.packet = packet;
	//	}

	/**
	 * @return the channelContext
	 */
	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	//
	//	/**
	//	 * @param channelContext the channelContext to set
	//	 */
	//	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	//	{
	//		this.channelContext = channelContext;
	//	}

	/**
	 * @return the writeSemaphore
	 */
	public java.util.concurrent.Semaphore getWriteSemaphore()
	{
		return writeSemaphore;
	}

}
