/**
 * 
 */
package com.talent.aio.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SystemTimer.
 *
 * @filename:  com.talent.threadpool.utils.SystemTimer
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年4月30日 上午10:01:58
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2015年4月30日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class SystemTimer
{

	/** The log. */
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(SystemTimer.class);

	/**
	 * Instantiates a new system timer.
	 */
	public SystemTimer()
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

	/** The Constant executor. */
	private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	/** The Constant tickUnit. */
	private static final long tickUnit = Long.parseLong(System.getProperty("notify.systimer.tick", "10"));

	/** The time. */
	private static volatile long time = System.currentTimeMillis();

	/**
	 * The Class TimerTicker.
	 */
	private static class TimerTicker implements Runnable
	{

		/** 
		 * @see java.lang.Runnable#run()
		 * 
		 * @重写人: tanyaowu
		 * @重写时间: 2016年11月15日 上午9:07:45
		 * 
		 */
		@Override
		public void run()
		{
			time = System.currentTimeMillis();
		}
	}

	/**
	 * Current time millis.
	 *
	 * @return the long
	 */
	public static long currentTimeMillis()
	{
		//    	return System.currentTimeMillis();
		return time;
	}

	static
	{
		executor.scheduleAtFixedRate(new TimerTicker(), tickUnit, tickUnit, TimeUnit.MILLISECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				executor.shutdown();
			}
		});
	}
}
