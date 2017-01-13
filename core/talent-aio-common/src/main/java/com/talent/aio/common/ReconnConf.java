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

import com.talent.aio.common.intf.Packet;

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
public class ReconnConf<Ext, P extends Packet, R>
{

	/**
	 * 重连的间隔时间，单位毫秒
	 */
	private long interval = 5000;
	
	/**
	 * 连续重连次数，当连续重连这么多次都失败时，不再重连。0和负数则一直重连
	 */
	private int retryCount = 0;

	LinkedBlockingQueue<ChannelContext<Ext, P, R>> queue = new LinkedBlockingQueue<ChannelContext<Ext, P, R>>();

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午3:53:46
	 * 
	 */
	public ReconnConf()
	{
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
		super();
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
	public LinkedBlockingQueue<ChannelContext<Ext, P, R>> getQueue()
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

}
