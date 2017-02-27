/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-utils
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月5日 下午1:50:34
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自带读写锁的对象.
 *
 * @author tanyaowu
 * @param <T> the generic type
 * @创建时间 2016年11月5日 下午1:50:34
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月5日 | tanyaowu | 新建类
 */
public class ObjWithLock<T>
{

	/** The log. */
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ObjWithLock.class);

	/** The obj. */
	private T obj = null;

	/**
	 * The lock.
	 *
	 * @含义: 
	 * @类型: ReentrantReadWriteLock
	 */
	private ReentrantReadWriteLock lock = null;

	/**
	 * Instantiates a new obj with read write lock.
	 *
	 * @param obj the obj
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:50:34
	 */
	public ObjWithLock(T obj)
	{
		this(obj, new ReentrantReadWriteLock());
	}

	/**
	 * Instantiates a new obj with read write lock.
	 *
	 * @param obj the obj
	 * @param lock the lock
	 * @author: tanyaowu
	 * @创建时间:　2016年11月7日 上午10:16:08
	 */
	public ObjWithLock(T obj, ReentrantReadWriteLock lock)
	{
		super();
		this.obj = obj;
		this.lock = lock;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:50:34
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * Gets the lock.
	 *
	 * @return the lock
	 * @author: tanyaowu
	 * @创建时间:　2016年11月7日 上午10:17:27
	 */
	public ReentrantReadWriteLock getLock()
	{
		return lock;
	}

	//	/**
	//	 * Sets the lock.
	//	 *
	//	 * @param lock the new lock
	//	 * @author: tanyaowu
	//	 * @创建时间:　2016年11月7日 上午10:17:31
	//	 */
	//	public void setLock(ReentrantReadWriteLock lock)
	//	{
	//		this.lock = lock;
	//	}

	/**
	 * Gets the obj.
	 *
	 * @return the obj
	 * @author: tanyaowu
	 * @创建时间:　2016年11月7日 上午10:17:34
	 */
	public T getObj()
	{
		return obj;
	}

	/**
	 * Sets the obj.
	 *
	 * @param obj the new obj
	 * @author: tanyaowu
	 * @创建时间:　2016年11月7日 上午10:17:37
	 */
	public void setObj(T obj)
	{
		this.obj = obj;
	}
}
