package com.talent.aio.common.utils;

import java.nio.channels.AsynchronousSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月17日 上午11:10:22
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 *
 */
public class AioUtils
{
	private static Logger log = LoggerFactory.getLogger(AioUtils.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午11:10:22
	 * 
	 */
	public AioUtils()
	{

	}

	public static <SessionContext, P extends Packet, R> boolean checkBeforeIO(ChannelContext<SessionContext, P, R> channelContext)
	{
		AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
		if (!asynchronousSocketChannel.isOpen())
		{
			log.warn("asynchronousSocketChannel is not open, {}", channelContext.toString());
			Aio.close(channelContext, "asynchronousSocketChannel is not open");
			return false;
		}
		return true;
	}

	public static <SessionContext, P extends Packet, R> SendRunnable<SessionContext, P, R> selectSendRunnable(ChannelContext<SessionContext, P, R> channelContext, Packet packet)
	{
//		byte priority = packet.getPriority();
//		if (priority == com.talent.aio.common.intf.Packet.PRIORITY_HIGH)
//		{
//			return channelContext.getSendRunnableHighPrior();
//		} else
//		{
			return channelContext.getSendRunnableNormPrior();
//		}
	}

	public static <SessionContext, P extends Packet, R> SynThreadPoolExecutor<SynRunnableIntf> selectSendExecutor(ChannelContext<SessionContext, P, R> channelContext, Packet packet)
	{
//		byte priority = packet.getPriority();
//		if (priority == com.talent.aio.common.intf.Packet.PRIORITY_HIGH)
//		{
//			return channelContext.getGroupContext().getSendExecutorHighPrior();
//		} else
//		{
			return channelContext.getGroupContext().getSendExecutorNormPrior();
//		}
	}

	public static <SessionContext, P extends Packet, R> HandlerRunnable<SessionContext, P, R> selectHandlerRunnable(ChannelContext<SessionContext, P, R> channelContext, Packet packet)
	{
//		byte priority = packet.getPriority();
//		if (priority == com.talent.aio.common.intf.Packet.PRIORITY_HIGH)
//		{
//			return channelContext.getHandlerRunnableHighPrior();
//		} else
//		{
			return channelContext.getHandlerRunnableNormPrior();
//		}
	}

	public static <SessionContext, P extends Packet, R> SynThreadPoolExecutor<SynRunnableIntf> selectHandlerExecutor(ChannelContext<SessionContext, P, R> channelContext, Packet packet)
	{
//		byte priority = packet.getPriority();
//		if (priority == com.talent.aio.common.intf.Packet.PRIORITY_HIGH)
//		{
//			return channelContext.getGroupContext().getHandlerExecutorHighPrior();
//		} else
//		{
			return channelContext.getGroupContext().getHandlerExecutorNormPrior();
//		}
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午11:10:22
	 * 
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
