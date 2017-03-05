package com.talent.aio.common;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ReconnConf;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.MaintainUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年3月1日 下午1:52:12
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年3月1日 | tanyaowu | 新建类
 *
 */
public class CloseRunnable<SessionContext, P extends Packet, R>  implements Runnable
{
	
	private static Logger log = LoggerFactory.getLogger(CloseRunnable.class);

	private ChannelContext<SessionContext, P, R> channelContext;
	private Throwable throwable;
	private String remark;
	private boolean isNeedRemove;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年3月1日 下午1:52:12
	 * 
	 */
	public CloseRunnable(ChannelContext<SessionContext, P, R> channelContext, Throwable throwable, String remark, boolean isNeedRemove)
	{
		this.channelContext = channelContext;
		this.throwable = throwable;
		this.remark = remark;
		this.isNeedRemove = isNeedRemove;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年3月1日 下午1:52:12
	 * 
	 */
	public static void main(String[] args)
	{}

	/** 
	 * @see java.lang.Runnable#run()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2017年3月1日 下午1:54:34
	 * 
	 */
	@Override
	public void run()
	{
		try
		{
			
			try
			{
				AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
				if (asynchronousSocketChannel != null && asynchronousSocketChannel.isOpen())
				{
					try
					{
						asynchronousSocketChannel.close();
					} catch (Exception e)
					{
						log.error(e.toString(), e);
					}
				}
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
			
			boolean isClientChannelContext = channelContext instanceof ClientChannelContext;
//			ReconnConf<SessionContext, P, R> reconnConf = channelContext.getGroupContext().getReconnConf();
			boolean isRemove = this.isNeedRemove;
			if (!isRemove)
			{
//				if (reconnConf != null && reconnConf.getInterval() > 0)
//				{
//					if (reconnConf.getRetryCount() <= 0 || reconnConf.getRetryCount() >= channelContext.getReconnCount())
//					{
//						//需要重连，所以并不删除
//					} else
//					{
//						isRemove = true;
//					}
//				} else
//				{
//					isRemove = true;
//				}
				
				if (isClientChannelContext)
				{
					ClientChannelContext<SessionContext, P, R> clientChannelContext = (ClientChannelContext<SessionContext, P, R>) channelContext;

					if (!ReconnConf.isNeedReconn(clientChannelContext, false))
					{
						isRemove = true;
					}
				} else
				{
					isRemove = true;
				}
			}

			ReentrantReadWriteLock reentrantReadWriteLock = channelContext.getCloseLock();//.getLock();
			WriteLock writeLock = reentrantReadWriteLock.writeLock();
			boolean isLock = writeLock.tryLock();

			try
			{
				if (!isLock)
				{
					if (isRemove)
					{
						if (channelContext.isRemoved())
						{
							return;
						} else
						{
							writeLock.lock();
						}
					} else
					{
						return;
					}
				}

				if (channelContext.isClosed() && !isRemove)
				{
					log.info("{}已经关闭，备注:{}，异常:{}", channelContext, remark, throwable == null ? "无" : throwable.toString());
					return;
				}

				if (channelContext.isRemoved())
				{
					log.info("{}已经删除，备注:{}，异常:{}", channelContext, remark, throwable == null ? "无" : throwable.toString());
					return;
				}

				//必须先取消任务再清空队列
				channelContext.getDecodeRunnable().setCanceled(true);
				channelContext.getHandlerRunnableNormPrior().setCanceled(true);
				//		channelContext.getHandlerRunnableHighPrior().setCanceled(true);
				channelContext.getSendRunnableNormPrior().setCanceled(true);
				//		channelContext.getSendRunnableHighPrior().setCanceled(true);

				channelContext.getDecodeRunnable().clearMsgQueue();
				channelContext.getHandlerRunnableNormPrior().clearMsgQueue();
				//		channelContext.getHandlerRunnableHighPrior().clearMsgQueue();
				channelContext.getSendRunnableNormPrior().clearMsgQueue();
				//		channelContext.getSendRunnableHighPrior().clearMsgQueue();

				log.info("准备关闭连接:{}, isNeedRemove:{}, {}", channelContext, isRemove, remark);

				GroupContext<SessionContext, P, R> groupContext = channelContext.getGroupContext();
				AioListener<SessionContext, P, R> aioListener = groupContext.getAioListener();

				try
				{
					if (isRemove)
					{
						MaintainUtils.removeFromMaintain(channelContext);
					} else
					{
						groupContext.getCloseds().add(channelContext);
						groupContext.getConnecteds().remove(channelContext);
					}

					try
					{
						channelContext.setClosed(true);
						channelContext.setRemoved(isRemove);
						channelContext.getGroupContext().getGroupStat().getClosed().incrementAndGet();
						channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());
					} catch (Exception e)
					{
						log.error(e.toString(), e);
					}

					try
					{
						aioListener.onAfterClose(channelContext, throwable, remark, isRemove);
					} catch (Throwable e)
					{
						log.error(e.toString(), e);
					}
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				} finally
				{
					if (!isRemove && channelContext.isClosed() && (isClientChannelContext)) //不删除且没有连接上，则加到重连队列中
					{
						ClientChannelContext<SessionContext, P, R> clientChannelContext = (ClientChannelContext<SessionContext, P, R>) channelContext;
						ReconnConf.put(clientChannelContext);
					}
				}

			} catch (Exception e)
			{
				log.error(throwable.toString(), e);
			} finally
			{
				writeLock.unlock();
			}
		} finally
		{
			channelContext.setWaitingClose(false);
		}
	}

}
