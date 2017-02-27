/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.threadpool.intf.QueueRunnableIntf;

/**
 * The Class AbstractQueueRunnable.
 *
 * @author 谭耀武
 * @param <T> 队列中存的数据类型
 * @date 2012-1-4
 */
public abstract class AbstractQueueRunnable<T> extends AbstractSynRunnable implements QueueRunnableIntf<T>
{
	private static final Logger log = LoggerFactory.getLogger(HandlerRunnable.class);
	
	/**
	 * Instantiates a new abstract queue runnable.
	 */
	public AbstractQueueRunnable(Executor executor)
	{
		super(executor);
	}

	public boolean isNeededExecute()
	{
		return msgQueue.size() > 0;
	}

	/** The msg queue. */
	protected ConcurrentLinkedQueue<T> msgQueue = new ConcurrentLinkedQueue<T>();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

	}
	
	/**
	 * 添加要处理的消息
	 * 
	 * @param packet
	 */
	public void addMsg(T t)
	{
		if (this.isCanceled())
		{
			log.error("任务已经取消");
			return;
		}
		
		getMsgQueue().add(t);
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.QueueRunnableIntf#getMsgQueue()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public ConcurrentLinkedQueue<T> getMsgQueue()
	{
		return msgQueue;
	}

	/**
	 * Sets the msg queue.
	 *
	 * @param msgQueue the new msg queue
	 */
	public void setMsgQueue(ConcurrentLinkedQueue<T> msgQueue)
	{
		this.msgQueue = msgQueue;
	}

}
