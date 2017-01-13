/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:22:48
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.Syns;
import com.talent.aio.common.task.CloseRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;
import com.talent.aio.common.utils.AioUtils;

/**
 * The Class Aio.
 *
 * @author tanyaowu
 * @创建时间 2016年11月15日 上午11:22:48
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 */
public class Aio
{

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(Aio.class);

	/**
	 * Instantiates a new aio.
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:22:48
	 */
	private Aio()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:22:48
	 */
	public static void main(String[] args)
	{

	}

	private static <Ext, P extends Packet, R> void close(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark, boolean isRemove)
	{
		channelContext.getDecodeRunnable().clearMsgQueue();
		channelContext.getHandlerRunnableNormPrior().clearMsgQueue();
		//		channelContext.getHandlerRunnableHighPrior().clearMsgQueue();
		channelContext.getSendRunnableNormPrior().clearMsgQueue();
		//		channelContext.getSendRunnableHighPrior().clearMsgQueue();

		channelContext.getDecodeRunnable().setCanceled(true);
		channelContext.getHandlerRunnableNormPrior().setCanceled(true);
		//		channelContext.getHandlerRunnableHighPrior().setCanceled(true);
		channelContext.getSendRunnableNormPrior().setCanceled(true);
		//		channelContext.getSendRunnableHighPrior().setCanceled(true);

		CloseRunnable<Ext, P, R> closeRunnable = channelContext.getCloseRunnable();
		if (closeRunnable.isWaitingExecute())
		{
			log.error("{},已经在等待关闭\r\n本次关闭备注:{}\r\n第一次的备注:{}\r\n本次关闭异常:{}\r\n第一次时异常:{}", channelContext, remark, closeRunnable.getRemark(), t, closeRunnable.getT());
			return;
		}
		synchronized (closeRunnable)
		{
			if (closeRunnable.isWaitingExecute())//double check
			{
				return;
			}
			closeRunnable.setRemove(isRemove);
			closeRunnable.setRemark(remark);
			closeRunnable.setT(t);
			closeRunnable.getExecutor().execute(closeRunnable);
			closeRunnable.setWaitingExecute(true);
		}
		//		closeRunnable.runTask();
	}

	/**
	 * Close.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param t the t
	 * @param remark the remark
	 */
	public static <Ext, P extends Packet, R> void close(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark)
	{
		close(channelContext, t, remark, false);
	}

	/**
	 * 和close方法一样，只不过不再进行重连等维护性的操作
	 * @param channelContext
	 * @param t
	 * @param remark
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:53:19
	 *
	 */
	public static <Ext, P extends Packet, R> void remove(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark)
	{
		close(channelContext, t, remark, true);
	}

	/**
	 * 
	 * @param channelContext
	 * @param remark
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:53:48
	 *
	 */
	public static <Ext, P extends Packet, R> void close(ChannelContext<Ext, P, R> channelContext, String remark)
	{
		close(channelContext, null, remark);
	}

	/**
	 * 和close方法一样，只不过不再进行重连等维护性的操作
	 * @param channelContext
	 * @param remark
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:53:53
	 *
	 */
	public static <Ext, P extends Packet, R> void remove(ChannelContext<Ext, P, R> channelContext, String remark)
	{
		remove(channelContext, null, remark);
	}

	/**
	 * 
	 * @param groupContext
	 * @param remoteip
	 * @param remoteport
	 * @param t
	 * @param remark
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:53:59
	 *
	 */
	public static <Ext, P extends Packet, R> void close(GroupContext<Ext, P, R> groupContext, String remoteip, Integer remoteport, Throwable t, String remark)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getClientNodes().find(remoteip, remoteport);
		close(channelContext, t, remark);
	}

	/**
	 * 和close方法一样，只不过不再进行重连等维护性的操作
	 * @param groupContext
	 * @param remoteip
	 * @param remoteport
	 * @param t
	 * @param remark
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:54:03
	 *
	 */
	public static <Ext, P extends Packet, R> void remove(GroupContext<Ext, P, R> groupContext, String remoteip, Integer remoteport, Throwable t, String remark)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getClientNodes().find(remoteip, remoteport);
		remove(channelContext, t, remark);
	}

	/**
	 * 
	 * @param groupContext
	 * @param remoteip
	 * @param remoteport
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:42:25
	 *
	 */
	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByRemote(GroupContext<Ext, P, R> groupContext, String remoteip, Integer remoteport)
	{
		return groupContext.getClientNodes().find(remoteip, remoteport);
	}

	/**
	 * 一个组有哪些客户端
	 *
	 * @param groupid the groupid
	 * @return the obj with read write lock
	 */
	public static <Ext, P extends Packet, R> ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> getChannelContextsByGroup(GroupContext<Ext, P, R> groupContext, String groupid)
	{
		return groupContext.getGroups().clients(groupid);
	}

	/**
	 * 绑定用户
	 * @param groupContext
	 * @param channelContext
	 * @param userid
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:51:43
	 *
	 */
	public static <Ext, P extends Packet, R> void bindUser(ChannelContext<Ext, P, R> channelContext, String userid)
	{
		channelContext.getGroupContext().getUsers().bind(userid, channelContext);
	}

	/**
	 * 解绑用户
	 * @param groupContext
	 * @param channelContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:54:31
	 *
	 */
	public static <Ext, P extends Packet, R> void unbindUser(ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getGroupContext().getUsers().unbind(channelContext);
	}

	/**
	 * 绑定群组
	 * @param channelContext
	 * @param groupid
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:51:43
	 *
	 */
	public static <Ext, P extends Packet, R> void bindGroup(ChannelContext<Ext, P, R> channelContext, String groupid)
	{
		channelContext.getGroupContext().getGroups().bind(groupid, channelContext);
	}

	/**
	 * 解绑群组
	 * @param groupContext
	 * @param channelContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:54:31
	 *
	 */
	public static <Ext, P extends Packet, R> void unbindGroup(ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getGroupContext().getUsers().unbind(channelContext);
	}

	/**
	 * 
	 * @param groupContext
	 * @param userid
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:43:59
	 *
	 */
	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByUserid(GroupContext<Ext, P, R> groupContext, String userid)
	{
		return groupContext.getUsers().find(userid);
	}

	/**
	 * 
	 * @param groupContext
	 * @param userid
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:42:33
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToUser(GroupContext<Ext, P, R> groupContext, String userid, P packet)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getUsers().find(userid);
		send(channelContext, packet);
	}

	/**
	 * Send.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param packet the packet
	 */
	public static <Ext, P extends Packet, R> void send(ChannelContext<Ext, P, R> channelContext, P packet)
	{
		if (channelContext == null)
		{
			log.error("channelContext == null");
			return;
		}
		SendRunnable<Ext, P, R> sendRunnable = AioUtils.selectSendRunnable(channelContext, packet);
		sendRunnable.addMsg(packet);
		SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor = AioUtils.selectSendExecutor(channelContext, packet);
		synThreadPoolExecutor.execute(sendRunnable);
	}

	/**
	 * 发送到指定的ip和port
	 * @param groupContext
	 * @param ip
	 * @param port
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:28:42
	 *
	 */
	public static <Ext, P extends Packet, R> void send(GroupContext<Ext, P, R> groupContext, String ip, int port, P packet)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getClientNodes().find(ip, port);
		if (channelContext != null)
		{
			send(channelContext, packet);
		}
	}

	/**
	 * 发消息到组
	 * @param groupContext
	 * @param groupid
	 * @param packet
	 * @param channelContextFilter filter方法返回为false的ChannelContext将会过滤掉(即不会把消息发给这个ChannelContext)
	 *
	 * @author: tanyaowu
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToGroup(GroupContext<Ext, P, R> groupContext, String groupid, P packet, ChannelContextFilter<Ext, P, R> channelContextFilter)
	{
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = groupContext.getGroups().clients(groupid);
		if (objWithReadWriteLock == null)
		{
			log.debug("组[{}]不存在", groupid);
			return;
		}

		Lock lock = objWithReadWriteLock.getLock().readLock();
		try
		{
			lock.lock();
			Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
			if (set.size() == 0)
			{
				log.debug("组[{}]里没有客户端", groupid);
				return;
			}
			for (ChannelContext<Ext, P, R> channelContext : set)
			{
				if (channelContextFilter != null)
				{
					boolean isfilter = channelContextFilter.filter(channelContext);
					if (!isfilter)
					{
						continue;
					}
				}
				send(channelContext, packet);
			}
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * 
	 * @param groupContext
	 * @param groupid
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月13日 下午3:33:54
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToGroup(GroupContext<Ext, P, R> groupContext, String groupid, P packet)
	{
		sendToGroup(groupContext, groupid, packet, null);
	}

	/**
	 * 同步发送消息.<br>
	 * 注意：<br>
	 * 1、参数packet的synSeq不为空且大于0（null、等于小于0都不行）<br>
	 * 2、对端收到此消息后，需要回一条synSeq一样的消息<br>
	 * 3、对于同步发送，框架层面并不会帮应用去调用handler.handler(packet, channelContext)方法，应用需要自己去处理响应的消息包，参考：groupContext.getAioHandler().handler(packet, channelContext);<br>
	 * @param channelContext
	 * @param packet
	 * @param timeout 超时时间，单位：毫秒
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月1日 下午12:52:11
	 *
	 */
	@SuppressWarnings("finally")
	public static <Ext, P extends Packet, R> P synSend(ChannelContext<Ext, P, R> channelContext, P packet, long timeout)
	{
		if (channelContext == null)
		{
			throw new RuntimeException("channelContext == null");
		}

		Integer synSeq = packet.getSynSeq();
		if (synSeq == null || synSeq <= 0)
		{
			throw new RuntimeException("synSeq必须大于0");
		}

		Syns<Ext, P, R> syns = channelContext.getGroupContext().getSyns();
		try
		{
			syns.put(synSeq, packet);

			synchronized (packet)
			{
				send(channelContext, packet);
				try
				{
					packet.wait(timeout);
				} catch (InterruptedException e)
				{
					log.error(e.toString(), e);
				}
			}
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			P respPacket = syns.remove(synSeq);
			if (respPacket == null)
			{
				log.error("respPacket == null,{}", channelContext);
				return null;
			}
			if (respPacket == packet)
			{
				log.error("同步发送超时,{}", channelContext);
				return null;
			}
			return respPacket;
		}
	}

}
