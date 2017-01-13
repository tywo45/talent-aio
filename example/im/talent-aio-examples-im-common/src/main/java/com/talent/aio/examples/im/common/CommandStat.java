/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月6日 下午5:32:31
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月6日 下午5:32:31
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月6日 | tanyaowu | 新建类
 *
 */
public class CommandStat
{

	public final static Map<Short, CommandStat> commandAndCount = new ConcurrentHashMap<>();
	
	public final AtomicLong received = new AtomicLong();
	public final AtomicLong handled = new AtomicLong();
	public final AtomicLong sent = new AtomicLong();

	public static CommandStat getCount(Short command)
	{
		CommandStat ret = commandAndCount.get(command);
		if (ret != null)
		{
			return ret;
		}

		synchronized (commandAndCount)
		{
			ret = commandAndCount.get(command);
			if (ret != null)
			{
				return ret;
			}
			ret = new CommandStat();
			commandAndCount.put(command, ret);
		}
		return ret;
	}
	

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午5:32:31
	 * 
	 */
	public CommandStat()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午5:32:31
	 * 
	 */
	public static void main(String[] args)
	{
	}


	/**
	 * @return the receivedCount
	 */
	public AtomicLong getReceived()
	{
		return received;
	}


	/**
	 * @return the handledCount
	 */
	public AtomicLong getHandled()
	{
		return handled;
	}


	/**
	 * @return the sentCount
	 */
	public AtomicLong getSent()
	{
		return sent;
	}

}
