/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

/**
 * 同步任务调度器:<br>
 * 用来调度那些实现了SynRunnableIntf的任务类对象.
 *
 * @author 谭耀武 2012-1-2 下午06:40:54
 * @param <T>            可执行的任务类，必须继承自SynRunnableIntf<E>
 */
public class SynThreadPoolExecutor<T extends SynRunnableIntf> extends ThreadPoolExecutor implements java.lang.Comparable<SynThreadPoolExecutor<T>>
{

	/** The Constant CORE_POOL_NUM. */
	public final static int CORE_POOL_NUM = 5;

	/** The Constant MAX_POOL_NUM. */
	public final static int MAX_POOL_NUM = 40;

	/** The Constant KEEP_ALIVE_TIME. */
	public final static int KEEP_ALIVE_TIME = 90;

	/** The Constant TIME_UNIT. */
	public final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	/** The Constant RUNNABLE_QUEUE. */
	public final static SynchronousQueue<Runnable> RUNNABLE_QUEUE = new SynchronousQueue<Runnable>(); // 存放runnable的队列

//	/** The task act submit count. */
//	private java.util.concurrent.atomic.AtomicLong taskActSubmitCount = new AtomicLong(0);

//	/** The task submit count. */
//	private java.util.concurrent.atomic.AtomicLong taskSubmitCount = new AtomicLong(0);

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(SynThreadPoolExecutor.class);

	/** The name. */
	private String name = null;

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param name the name
	 */
	public SynThreadPoolExecutor(String name)
	{
		this(CORE_POOL_NUM, MAX_POOL_NUM, KEEP_ALIVE_TIME, (BlockingQueue<Runnable>) RUNNABLE_QUEUE, DefaultThreadFactory.getInstance(name, null), name);
	}

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param name the name
	 */
	public SynThreadPoolExecutor(int corePoolSize, int maximumPoolSize, String name)
	{
		this(corePoolSize, maximumPoolSize, KEEP_ALIVE_TIME, (BlockingQueue<Runnable>) RUNNABLE_QUEUE, DefaultThreadFactory.getInstance(name, null), name);
	}

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param runnableQueue the runnable queue
	 * @param name the name
	 */
	public SynThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> runnableQueue, String name)
	{
		this(corePoolSize, maximumPoolSize, keepAliveTime, (BlockingQueue<Runnable>) runnableQueue, DefaultThreadFactory.getInstance(name, null), name);
	}

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param runnableQueue the runnable queue
	 * @param handler the handler
	 * @param name the name
	 */
	public SynThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> runnableQueue, RejectedExecutionHandler handler, String name)
	{
		this(corePoolSize, maximumPoolSize, keepAliveTime, (BlockingQueue<Runnable>) runnableQueue, DefaultThreadFactory.getInstance(name, null), handler, name);
	}

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param runnableQueue the runnable queue
	 * @param threadFactory the thread factory
	 * @param handler the handler
	 * @param name the name
	 */
	public SynThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> runnableQueue, ThreadFactory threadFactory,
			RejectedExecutionHandler handler, String name)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, TIME_UNIT, (BlockingQueue<Runnable>) runnableQueue, threadFactory, handler);
		this.name = name;
	}

	/**
	 * Instantiates a new syn thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param runnableQueue the runnable queue
	 * @param threadFactory the thread factory
	 * @param name the name
	 */
	public SynThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue<Runnable> runnableQueue, ThreadFactory threadFactory, String name)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, TIME_UNIT, (BlockingQueue<Runnable>) runnableQueue, threadFactory);
		this.name = name;
		RejectedExecutionHandler handler = new DefaultRejectedExecutionHandler(this);
		this.setRejectedExecutionHandler(handler);
	}

	/** 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public void shutdown()
	{
		super.shutdown();
	}

	/** 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public List<Runnable> shutdownNow()
	{
		List<Runnable> ret = super.shutdownNow();
		return ret;
	}

	/** 
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 * 
	 * @param thread
	 * @param runnable
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	protected void beforeExecute(Thread thread, Runnable runnable)
	{

		//		
		//		try
		//		{
		//			super.beforeExecute(thread, runnable);
		//		} catch (Exception e)
		//		{
		//			log.error(e.toString(), e);
		//		} finally
		//		{
		//			synchronized (runnableTask)
		//			{
		//				runnableTask.setRunning(true);
		//				//				runnableTask.setInSchedule(false);
		//			}
		//
		//			
		//			
		//			
		//		}

	}

	//	@Override
	//	protected void afterExecute(Runnable runnable, Throwable throwable)
	//	{
	//		try
	//		{
	//			super.afterExecute(runnable, throwable);
	//			if (throwable != null)
	//			{
	//				log.error(throwable.getLocalizedMessage(), throwable);
	//			}
	//		} catch (Exception e)
	//		{
	//			log.error(e.toString(), e);
	//		} finally
	//		{
	//			@SuppressWarnings("unchecked")
	//			T runnableTask = (T) runnable;
	//			synchronized (runnableTask)
	//			{
	//				runnableTask.setRunning(false);
	//				//				runnableTask.setInSchedule(false);
	//			}
	//		}
	//	}

	/**
	 * 提交前作些检查，看是否有必要提交.
	 *
	 * @param runnable the runnable
	 * @return true:可以提交，false:不需要提交
	 */
	private boolean checkBeforeExecute(T runnable)
	{
		ReadWriteLock runningLock = runnable.runningLock();
		Lock writeLock = runningLock.writeLock();
		boolean tryLock = false;
		try
		{
			tryLock = writeLock.tryLock();
			return tryLock;
		} finally
		{
			if (tryLock)
			{
				writeLock.unlock();
			}
		}

	}

	/** 
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 * 
	 * @param _runnable
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Runnable _runnable)
	{
		T runnable = (T) _runnable;
//		taskSubmitCount.incrementAndGet();
		if (checkBeforeExecute(runnable))
		{
			//			synchronized (runnable)
			//			{
			//				runnable.setInSchedule(true);
			//				runnable.setRunning(false);
			super.execute(runnable);
//			taskActSubmitCount.incrementAndGet();
			//			}

		}
	}

	/** 
	 * @see java.util.concurrent.AbstractExecutorService#submit(java.lang.Runnable, java.lang.Object)
	 * 
	 * @param _runnable
	 * @param result
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> Future<R> submit(Runnable _runnable, R result)
	{
		T runnable = (T) _runnable;
		if (checkBeforeExecute(runnable))
		{
			//			synchronized (runnable)
			//			{
			//				runnable.setInSchedule(true);
			//			}

			Future<R> ret = super.submit(runnable, result);

			return ret;
		} else
		{
			return null;
		}
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/** 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * @param o
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public int compareTo(SynThreadPoolExecutor<T> o)
	{
		if (o.getCompletedTaskCount() > this.getCompletedTaskCount())
		{
			return -1;
		} else if (o.getCompletedTaskCount() < this.getCompletedTaskCount())
		{
			return 1;
		}

		return this.getName().compareTo(o.getName());

	}

//	/**
//	 * Gets the task act submit count.
//	 *
//	 * @return the task act submit count
//	 */
//	public java.util.concurrent.atomic.AtomicLong getTaskActSubmitCount()
//	{
//		return taskActSubmitCount;
//	}
//
//	/**
//	 * Sets the task act submit count.
//	 *
//	 * @param taskActSubmitCount the new task act submit count
//	 */
//	public void setTaskActSubmitCount(java.util.concurrent.atomic.AtomicLong taskActSubmitCount)
//	{
//		this.taskActSubmitCount = taskActSubmitCount;
//	}
//
//	/**
//	 * Gets the task submit count.
//	 *
//	 * @return the task submit count
//	 */
//	public java.util.concurrent.atomic.AtomicLong getTaskSubmitCount()
//	{
//		return taskSubmitCount;
//	}
//
//	/**
//	 * Sets the task submit count.
//	 *
//	 * @param taskSubmitCount the new task submit count
//	 */
//	public void setTaskSubmitCount(java.util.concurrent.atomic.AtomicLong taskSubmitCount)
//	{
//		this.taskSubmitCount = taskSubmitCount;
//	}

}
