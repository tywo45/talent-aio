/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月19日 下午1:44:39
 *
 * **************************************************************************
 */
package com.talent.aio.common.task;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractSynRunnable;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月19日 下午1:44:39
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月19日 | tanyaowu | 新建类
 *
 */
public class CloseRunnable<Ext, P extends Packet, R> extends AbstractSynRunnable
{
	private static Logger log = LoggerFactory.getLogger(CloseRunnable.class);

	private ChannelContext<Ext, P, R> channelContext;
	private String remark;
	private Throwable t;

	public CloseRunnable(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
		this.t = t;
		this.remark = remark;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#runTask()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月19日 下午1:46:31
	 * 
	 */
	@Override
	public void runTask()
	{
		if (channelContext == null)
		{
			log.warn("channelContext == null");
			return;
		}

		if (channelContext.isClosed())
		{
			return;
		}

		synchronized (channelContext)
		{
			if (channelContext.isClosed())
			{
				return;
			}

			if (StringUtils.isNotBlank(remark))
			{
				log.error("关闭连接:" + channelContext.toString() + ", 备注: " + remark);
			}

			GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
			//			Semaphore semaphore = groupContext.getCloseSemaphore();
			try
			{
				//				long starttime = SystemTimer.currentTimeMillis();
				//				semaphore.acquire();
				//				long endtime = SystemTimer.currentTimeMillis();
				//				long cost = (endtime - starttime);
				//				if (cost > 100)
				//				{
				//					log.error("等关闭权限耗时:{}ms", cost);
				//				}
				try
				{
					groupContext.getAioHandler().onClose(channelContext, t, remark);
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				}

				try
				{
					AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
					if (asynchronousSocketChannel != null)
					{
						asynchronousSocketChannel.close();
					}
				} catch (Throwable e)
				{
					log.error(e.toString());
				}

				//删除集合中的维护信息 start
				try
				{
					groupContext.getClientNodes().remove(channelContext);
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				}
				try
				{
					groupContext.getConnections().remove(channelContext);
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				}
				try
				{
					groupContext.getUsers().remove(channelContext);
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				}
				try
				{
					groupContext.getGroups().remove(channelContext);
				} catch (Throwable e)
				{
					log.error(e.toString(), e);
				}
				channelContext.setClosed(true);
				channelContext.getGroupContext().getGroupStat().getClosed().incrementAndGet();
				//删除集合中的维护信息 end

			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			} finally
			{
				//				semaphore.release();
			}
		}

	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#isNeededExecute()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月3日 下午6:01:11
	 * 
	 */
	@Override
	public boolean isNeededExecute()
	{
		return false;
	}

	/**
	 * @return the channelContext
	 */
	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	/**
	 * @return the t
	 */
	public Throwable getT()
	{
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(Throwable t)
	{
		this.t = t;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()).append(":");
		builder.append(channelContext.toString());
		return builder.toString();
	}
}
