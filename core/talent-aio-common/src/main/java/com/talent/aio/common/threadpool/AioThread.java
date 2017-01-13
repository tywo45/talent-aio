/**
 * 
 */
package com.talent.aio.common.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AioThread.
 *
 * @author 谭耀武
 * @date 2012-1-4
 */
public class AioThread extends Thread
{

	/** The Constant log. */
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AioThread.class);

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param target the target
	 * @param name the name
	 */
	public AioThread(Runnable target, String name)
	{
		super(target, name);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param target the target
	 */
	public AioThread(Runnable target)
	{
		super(target);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param name the name
	 */
	public AioThread(String name)
	{
		super(name);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param group the group
	 * @param target the target
	 * @param name the name
	 * @param stackSize the stack size
	 */
	public AioThread(ThreadGroup group, Runnable target, String name, long stackSize)
	{
		super(group, target, name, stackSize);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param group the group
	 * @param target the target
	 * @param name the name
	 */
	public AioThread(ThreadGroup group, Runnable target, String name)
	{
		super(group, target, name);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param group the group
	 * @param target the target
	 */
	public AioThread(ThreadGroup group, Runnable target)
	{
		super(group, target);
	}

	/**
	 * Instantiates a new aio thread.
	 *
	 * @param group the group
	 * @param name the name
	 */
	public AioThread(ThreadGroup group, String name)
	{
		super(group, name);
	}

	/**
	 * Instantiates a new aio thread.
	 */
	public AioThread()
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

}
