/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-utils
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月4日 上午9:59:52
 *
 * **************************************************************************
 */
package com.talent.aio.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author tanyaowu 
 * @创建时间 2016年11月4日 上午9:59:52
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月4日 | tanyaowu | 新建类
 */
public class SynUtils
{
	private static Logger log = LoggerFactory.getLogger(SynUtils.class);

	/**
	 * 同步读操作
	 * @param readLock
	 * @param synTask
	 * @return
	 * @throws Exception
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:45:18
	 */
	public static <T> T synRead(ReadLock readLock, SynTask<T> synTask) throws Exception
	{
		return synRun(readLock,  synTask);
	}
	
	/**
	 * 同步写操作
	 * @param writeLock
	 * @param synTask
	 * @return
	 * @throws Exception
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:45:24
	 */
	public static <T> T synWrite(WriteLock writeLock, SynTask<T> synTask) throws Exception
	{
		return synRun(writeLock,  synTask);
	}
	
	/**
	 * 
	 * @param writeLock
	 * @param collection
	 * @param object
	 * @return
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:45:29
	 */
	public static <T> boolean synAdd(WriteLock writeLock, java.util.Collection<T> collection, T object)
	{
		try
		{
			return SynUtils.synWrite(writeLock, new SynTask<Boolean>()
			{
				@Override
				public Boolean runTask() throws Exception
				{
					return collection.add(object);
				}
			});
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 
	 * @param writeLock
	 * @param collection
	 * @param object
	 * @return
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:46:17
	 */
	public static <T> boolean synRemove(WriteLock writeLock, java.util.Collection<T> collection, T object)
	{
		try
		{
			return SynUtils.synWrite(writeLock, new SynTask<Boolean>()
			{
				@Override
				public Boolean runTask() throws Exception
				{
					return collection.remove(object);
				}
			});
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * true: 如果该key已经存在，则会替换旧值，false: 如果该key已经存在，则直接返回
	 * @param writeLock
	 * @param map
	 * @param key
	 * @param value
	 * @param isReplaceOld
	 * @return
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:46:12
	 */
	public static <K, V> V synPut(WriteLock writeLock, Map<K, V> map, K key, V value, boolean isReplaceOld)
	{
		try
		{
			return SynUtils.synWrite(writeLock, new SynTask<V>()
			{
				@Override
				public V runTask() throws Exception
				{
					if (!isReplaceOld)
					{
						V v = map.get(key);
						if (v != null)
						{
							return v;
						}
					}
					map.put(key, value);
					return value;

				}
			});
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 
	 * @param writeLock
	 * @param map
	 * @param key
	 * @return
	 * @author: tanyaowu
	 * @创建时间:　2016年11月5日 下午1:47:05
	 */
	public static <K, V> V synRemove(WriteLock writeLock, Map<K, V> map, K key)
	{
		try
		{
			return SynUtils.synWrite(writeLock, new SynTask<V>()
			{
				@Override
				public V runTask() throws Exception
				{
					return map.remove(key);
				}
			});
		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 同步安全执行
	 * @param lock
	 * @param synTask
	 * @return
	 * @throws Exception
	 * @author: tanyaowu
	 * @创建时间:　2016年11月4日 上午11:06:16
	 */
	public static <T> T synRun(Lock lock, SynTask<T> synTask) throws Exception
	{
		try
		{
			lock.lock();
			return synTask.runTask();
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * @param args
	 * @author: tanyaowu
	 * @创建时间:　2016年11月4日 上午9:59:52
	 */
	public static void main(String[] args)
	{}
	


	public static interface SynTask <T>
	{
		T runTask() throws Exception;
	}
	
	
	
	
}
