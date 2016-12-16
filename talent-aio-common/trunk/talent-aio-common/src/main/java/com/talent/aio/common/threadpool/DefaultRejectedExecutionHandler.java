/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.threadpool.intf.SynRunnableIntf;



/**
 * 默认的RejectedExecutionHandler实现<br>
 * 如果Runnable提交被拒绝，本拒绝处理器会将Runnable放到一个队列中，并延时将该Runnable提交给ThreadPool执行。.
 *
 * @filename:  com.talent.threadpool.DefaultRejectedExecutionHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年10月18日 上午10:05:16
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2013年10月18日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class  DefaultRejectedExecutionHandler <R extends SynRunnableIntf> implements  RejectedExecutionHandler
{
	
	/** The log. */
	private static Logger log = LoggerFactory.getLogger(DefaultRejectedExecutionHandler.class);
	
	/** The timer seq. */
	private static AtomicInteger timerSeq = new AtomicInteger();
	
	/** The rejected count. */
	private AtomicLong rejectedCount = new AtomicLong();

	/**
	 * The Class SubmitTaskRunnable.
	 */
	public static class SubmitTaskRunnable <R extends SynRunnableIntf> implements Runnable
	{
		
		/** The deque. */
		LinkedBlockingDeque<SynRunnableIntf> deque = null;
		
		/** The executor. */
		SynThreadPoolExecutor<SynRunnableIntf> executor = null;

		/**
		 * Instantiates a new submit task runnable.
		 *
		 * @param deque the deque
		 * @param executor the executor
		 */
		public SubmitTaskRunnable(LinkedBlockingDeque<SynRunnableIntf> deque, SynThreadPoolExecutor<SynRunnableIntf> executor)
		{
			super();
			this.deque = deque;
			this.executor = executor;
		}

		/** 
		 * @see java.lang.Runnable#run()
		 * 
		 * @重写人: tanyaowu
		 * @重写时间: 2016年11月15日 上午9:07:01
		 * 
		 */
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					if (log.isInfoEnabled())
					{
						log.info("deque in run:{},hashcode:{}", deque.size(), deque.hashCode());
					}
					
					if (deque.size() > 0)
					{
						int maximumPoolSize = executor.getMaximumPoolSize();
						int poolSize = executor.getPoolSize();
						int activeCount = executor.getActiveCount();
						log.info("poolsize:{}, activeCount:{}, maximumsize:{}", poolSize, activeCount, maximumPoolSize);
						if (maximumPoolSize == activeCount)
						{
							log.warn("maximumPoolSize({}) == activeCount({})", maximumPoolSize, activeCount);
							try
							{
								long sleeptime = 1;
								Thread.sleep(sleeptime);
							} catch (java.lang.Throwable e)
							{
								log.error(e.toString(), e);
							}
							continue;
						}
					}

					Runnable r = deque.poll(6, TimeUnit.SECONDS);
					if (r != null)
					{
						//						if (r instanceof SynRunnableIntf)
						//						{
						//							synchronized (r)
						//							{
						//								SynRunnableIntf synRunnableIntf = ((SynRunnableIntf) r);
						//								synRunnableIntf.setInSchedule(false);
						//								synRunnableIntf.setRunning(false);
						//								executor.execute(r);
						//							}
						//						} else
						//						{
						//							executor.execute(r);
						//						}

						executor.execute(r);
						log.warn("submit a runnable, {} runnables waiting for submit", deque.size());
					}
				} catch (java.lang.Throwable e)
				{
					log.error(e.toString(), e);
				}
			}

		}

		/**
		 * Gets the deque.
		 *
		 * @return the deque
		 */
		public LinkedBlockingDeque<SynRunnableIntf> getDeque()
		{
			return deque;
		}

		/**
		 * Sets the deque.
		 *
		 * @param deque the new deque
		 */
		public void setDeque(LinkedBlockingDeque<SynRunnableIntf> deque)
		{
			this.deque = deque;
		}
	}

	/** The submit task thread. */
	private Thread submitTaskThread;
	
	/** The submit task runnable. */
	private SubmitTaskRunnable<R> submitTaskRunnable;

	//	public MyTimerTask getMyTimerTask()
	//	{
	//		return myTimerTask;
	//	}

	//	public void setMyTimerTask(MyTimerTask myTimerTask)
	//	{
	//		this.myTimerTask = myTimerTask;
	//	}

	/**
	 * Instantiates a new default rejected execution handler.
	 *
	 * @param synThreadPoolExecutor the syn thread pool executor
	 */
	public DefaultRejectedExecutionHandler(SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor)
	{
		String threadname = synThreadPoolExecutor.getName() + "-rejected-handler-" + timerSeq.incrementAndGet();
		LinkedBlockingDeque<SynRunnableIntf> deque = new LinkedBlockingDeque<>();
		submitTaskRunnable = new SubmitTaskRunnable<>(deque, synThreadPoolExecutor);
		submitTaskThread = new Thread(submitTaskRunnable, threadname);
		submitTaskThread.start();

		//		myTimerTask = new MyTimerTask(new LinkedBlockingQueue<Runnable>(), synThreadPoolExecutor);
		//		timer.schedule(myTimerTask, 1000);
	}

	/** 
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
	 * 
	 * @param r
	 * @param executor
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
	{
		SynRunnableIntf rr = null;
		if (r instanceof SynRunnableIntf)
		{
			rr = (SynRunnableIntf) r;
		} else
		{
			log.error("只支持SynRunnableIntf");
			return;
		}
		
		if (rr.isCanceled())
		{
			log.error("任务已经取消");
			return;
		}
		
		rejectedCount.incrementAndGet();


		//		LinkedBlockingQueue<Runnable> deque = myTimerTask.getQueue();
		//		synchronized (deque)
		//		{
		LinkedBlockingDeque<SynRunnableIntf> deque = submitTaskRunnable.deque;
		//		log.info("deque in rejectedExecution, size:{}, deque:{}", deque.size(), deque.hashCode());
		if (deque.contains(r))
		{
			log.warn("{} has contained in deque, deque size is {}", r, deque.size());
		} else
		{
			if (submitTaskThread == Thread.currentThread())
			{
				log.info("thread is same--submitTaskThread:{}, currentThread:{}", submitTaskThread,
						Thread.currentThread());
				deque.addFirst(rr);
			} else
			{
				log.info("thread is diff--submitTaskThread:{}, currentThread:{}", submitTaskThread,
						Thread.currentThread());
				deque.addLast(rr);
			}
		}
		//		}
		log.warn("{} is rejected, {} tasks is waiting!", r, deque.size());
	}

	/**
	 * Gets the submit task thread.
	 *
	 * @return the submit task thread
	 */
	public Thread getSubmitTaskThread()
	{
		return submitTaskThread;
	}

	/**
	 * Sets the submit task thread.
	 *
	 * @param submitTaskThread the new submit task thread
	 */
	public void setSubmitTaskThread(Thread submitTaskThread)
	{
		this.submitTaskThread = submitTaskThread;
	}

	/**
	 * Gets the submit task runnable.
	 *
	 * @return the submit task runnable
	 */
	public SubmitTaskRunnable getSubmitTaskRunnable()
	{
		return submitTaskRunnable;
	}

	/**
	 * Sets the submit task runnable.
	 *
	 * @param submitTaskRunnable the new submit task runnable
	 */
	public void setSubmitTaskRunnable(SubmitTaskRunnable submitTaskRunnable)
	{
		this.submitTaskRunnable = submitTaskRunnable;
	}

	/**
	 * Gets the rejected count.
	 *
	 * @return the rejected count
	 */
	public AtomicLong getRejectedCount()
	{
		return rejectedCount;
	}

	/**
	 * Sets the rejected count.
	 *
	 * @param rejectedCount the new rejected count
	 */
	public void setRejectedCount(AtomicLong rejectedCount)
	{
		this.rejectedCount = rejectedCount;
	}

}
