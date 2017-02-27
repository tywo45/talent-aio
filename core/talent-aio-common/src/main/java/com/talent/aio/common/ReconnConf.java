/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年1月11日 下午3:53:46
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.DefaultThreadFactory;

/**
 * 目前只支持
 * @author tanyaowu 
 * @创建时间 2017年1月11日 下午3:53:46
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月11日 | tanyaowu | 新建类
 *
 */
public class ReconnConf<SessionContext, P extends Packet, R>
{

	/**
	 * 重连的间隔时间，单位毫秒
	 */
	private long interval = 5000;

	/**
	 * 连续重连次数，当连续重连这么多次都失败时，不再重连。0和负数则一直重连
	 */
	private int retryCount = 0;

	LinkedBlockingQueue<ChannelContext<SessionContext, P, R>> queue = new LinkedBlockingQueue<ChannelContext<SessionContext, P, R>>();

	//用来重连的线程池
	private ThreadPoolExecutor threadPoolExecutor = null;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午3:53:46
	 * 
	 */
	public ReconnConf()
	{
		if (threadPoolExecutor == null)
		{
			synchronized (ReconnConf.class)
			{
				if (threadPoolExecutor == null)
				{
					threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), 60L, TimeUnit.SECONDS,
							new LinkedBlockingQueue<Runnable>(), DefaultThreadFactory.getInstance("t-aio-client-reconn"));
				}
			}

		}

	}

	/**
	 * @param interval
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午4:27:30
	 * 
	 */
	public ReconnConf(long interval)
	{
		this();
		this.setInterval(interval);
	}

	/**
	 * @param interval
	 * @param retryCount
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月13日 下午3:00:45
	 * 
	 */
	public ReconnConf(long interval, int retryCount)
	{
		this();
		this.interval = interval;
		this.retryCount = retryCount;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午3:53:46
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * @return the interval
	 */
	public long getInterval()
	{
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	/**
	 * @return the queue
	 */
	public LinkedBlockingQueue<ChannelContext<SessionContext, P, R>> getQueue()
	{
		return queue;
	}

	/**
	 * @return the retryCount
	 */
	public int getRetryCount()
	{
		return retryCount;
	}

	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(int retryCount)
	{
		this.retryCount = retryCount;
	}

	/**
	 * @return the threadPoolExecutor
	 */
	public ThreadPoolExecutor getThreadPoolExecutor()
	{
		return threadPoolExecutor;
	}

//	/**
//	 * @param threadPoolExecutor the threadPoolExecutor to set
//	 */
//	public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor)
//	{
//		this.threadPoolExecutor = threadPoolExecutor;
//	}

}
