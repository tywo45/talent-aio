/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的ThreadFactory，在生成Thread对象时，会使用应用提供的名字+序号作为线程的名字，这样方便大家辨认线程。<br>
 * 生成的Thread对象，其名字形如：myname-1, myname-2等.
 *
 * @filename: com.talent.threadpool.DefaultThreadFactory
 * @copyright: Copyright (c)2012
 * @company: talent
 * @author: 谭耀武
 * @version: 1.0
 * @create time: 2012-5-2 下午5:38:35
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 *         <thead style="font-weight:bold;background-color:#e3e197">
 *         <tr>
 *         <td>date</td>
 *         <td>author</td>
 *         <td>version</td>
 *         <td>description</td>
 *         </tr>
 *         </thead> <tbody style="background-color:#ffffeb">
 *         <tr>
 *         <td>2012-5-2</td>
 *         <td>谭耀武</td>
 *         <td>1.0</td>
 *         <td>create</td>
 *         </tr>
 *         </tbody>
 *         </table>
 */
public class DefaultThreadFactory implements ThreadFactory
{

	/** The log. */
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(DefaultThreadFactory.class);

	/** The thread pool name. */
	private String threadPoolName = null;

	/** The map of name and thread factory. */
	private static Map<String, DefaultThreadFactory> mapOfNameAndThreadFactory = new HashMap<String, DefaultThreadFactory>();

	/** The map of name and atomic integer. */
	private static Map<String, AtomicInteger> mapOfNameAndAtomicInteger = new HashMap<String, AtomicInteger>();

	/** The priority. */
	private int priority = Thread.NORM_PRIORITY;

	/**
	 * Gets the single instance of DefaultThreadFactory.
	 *
	 * @param threadName the thread name
	 * @param priority the priority
	 * @return single instance of DefaultThreadFactory
	 */
	public static DefaultThreadFactory getInstance(String threadName, Integer priority)
	{
		DefaultThreadFactory defaultThreadFactory = mapOfNameAndThreadFactory.get(threadName);
		if (defaultThreadFactory == null)
		{
			defaultThreadFactory = new DefaultThreadFactory();
			if (priority != null)
			{
				defaultThreadFactory.priority = priority;
			}

			defaultThreadFactory.setThreadName(threadName);
			mapOfNameAndThreadFactory.put(threadName, defaultThreadFactory);
			mapOfNameAndAtomicInteger.put(threadName, new AtomicInteger());
		}
		return defaultThreadFactory;
	}

	public static DefaultThreadFactory getInstance(String threadName)
	{
		return getInstance(threadName, Thread.NORM_PRIORITY);
	}

	/**
	 * Instantiates a new default thread factory.
	 */
	private DefaultThreadFactory()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 * 
	 * @param r
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public Thread newThread(Runnable r)
	{
		AioThread thread = new AioThread(r);
		thread.setName(this.getThreadPoolName() + "-" + mapOfNameAndAtomicInteger.get(this.getThreadPoolName()).incrementAndGet());
		thread.setPriority(priority);
		return thread;
	}

	/**
	 * Gets the thread pool name.
	 *
	 * @return the thread pool name
	 */
	public String getThreadPoolName()
	{
		return threadPoolName;
	}

	/**
	 * Sets the thread name.
	 *
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName)
	{
		this.threadPoolName = threadName;
	}

}
