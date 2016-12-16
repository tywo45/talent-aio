/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

/**
 * The Class AbstractSynRunnable.
 *
 * @filename:  com.talent.threadpool.AbstractSynRunnable
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2012-5-18 下午3:30:30
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2012-5-18</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public abstract class AbstractSynRunnable implements SynRunnableIntf
{

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(AbstractSynRunnable.class);

	private ReadWriteLock runningLock = new ReentrantReadWriteLock();

	//	/** 正在运行该任务的线程数. */
	//	private java.util.concurrent.atomic.AtomicInteger currThreads = new java.util.concurrent.atomic.AtomicInteger();

	private Executor executor;

	/**
	 * Instantiates a new abstract syn runnable.
	 */
	protected AbstractSynRunnable(Executor executor)
	{
		this.setExecutor(executor);
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#runningLock()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月3日 下午1:53:03
	 * 
	 */
	@Override
	public ReadWriteLock runningLock()
	{
		return runningLock;
	}

	//	/**
	//	 * 等待运行本任务的线程个数.
	//	 *
	//	 * @return the int
	//	 */
	//	public int waitingRunCount()
	//	{
	//		int size = getCurrThreadCount().get() - 1;
	//		if (size > 0)
	//		{
	//			log.info("{} threads wait for me, i am {}", size, this);
	//		}
	//		return size;
	//	}

	/** 
	 * @see java.lang.Runnable#run()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public final void run()
	{
		if (isCanceled())  //任务已经被取消
		{
			return;
		}
		
		ReadWriteLock runningLock = runningLock();
		Lock writeLock = runningLock.writeLock();
		boolean trylock = writeLock.tryLock();
		if (!trylock)
		{
			return;
		}
		
		try
		{
			runTask();
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			writeLock.unlock();
			if (isNeededExecute())
			{
//				log.error(this + "-----------------------------------------------------------------------------------------需要运行");
				getExecutor().execute(this);
			}
		}

	}

	private boolean isCanceled = false;

	public boolean isCanceled()
	{
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled)
	{
		this.isCanceled = isCanceled;
	}

	//	/** 
	//	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#setRunning(boolean)
	//	 * 
	//	 * @param isRunning
	//	 * @重写人: tanyaowu
	//	 * @重写时间: 2016年11月15日 上午9:07:01
	//	 * 
	//	 */
	//	@Override
	//	public void setRunning(boolean isRunning)
	//	{
	//		this.isRunning = isRunning;
	//	}

	//	/** 
	//	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#isRunning()
	//	 * 
	//	 * @return
	//	 * @重写人: tanyaowu
	//	 * @重写时间: 2016年11月15日 上午9:07:01
	//	 * 
	//	 */
	//	@Override
	//	public boolean isRunning()
	//	{
	//		return isRunning;
	//	}

	//	/** 
	//	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#setInSchedule(boolean)
	//	 * 
	//	 * @param isInSchedule
	//	 * @重写人: tanyaowu
	//	 * @重写时间: 2016年11月15日 上午9:07:01
	//	 * 
	//	 */
	//	@Override
	//	public void setInSchedule(boolean isInSchedule)
	//	{
	//		this.isInSchedule = isInSchedule;
	//	}

	//	/** 
	//	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#isInSchedule()
	//	 * 
	//	 * @return
	//	 * @重写人: tanyaowu
	//	 * @重写时间: 2016年11月15日 上午9:07:01
	//	 * 
	//	 */
	//	@Override
	//	public boolean isInSchedule()
	//	{
	//		return isInSchedule;
	//	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

	}

	//	/** 
	//	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#getCurrThreadCount()
	//	 * 
	//	 * @return
	//	 * @重写人: tanyaowu
	//	 * @重写时间: 2016年11月15日 上午9:07:01
	//	 * 
	//	 */
	//	@Override
	//	public AtomicInteger getCurrThreadCount()
	//	{
	//		return currThreads;
	//	}

	/**
	 * @return the executor
	 */
	public Executor getExecutor()
	{
		return executor;
	}

	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(Executor executor)
	{
		this.executor = executor;
	}

	//	public void setCurrThreads(Set<Thread> currThreads)
	//	{
	//		this.currThreads = currThreads;
	//	}
}
