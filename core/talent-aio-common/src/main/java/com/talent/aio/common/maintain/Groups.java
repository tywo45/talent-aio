/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月17日 下午1:28:28
 *
 * **************************************************************************
 */
package com.talent.aio.common.maintain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.Packet;

/**
 * The Class Groups.
 *
 * @author tanyaowu
 * @创建时间 2016年11月17日 下午1:28:28
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 */
public class Groups<Ext, P extends Packet, R>
{

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(Groups.class);

	/** 一个组有哪些客户端 key: groupid value: Set<ChannelContext<?, ?, ?>. */
	private ObjWithReadWriteLock<Map<String, ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>>> groupmap = new ObjWithReadWriteLock<Map<String, ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>>>(
			new ConcurrentHashMap<String, ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>>());

	/** 一个客户端在哪组组中 key: ChannelContext value: Set<groupid<?, ?, ?>. */
	private ObjWithReadWriteLock<Map<ChannelContext<Ext, P, R>, ObjWithReadWriteLock<Set<String>>>> channelmap = new ObjWithReadWriteLock<Map<ChannelContext<Ext, P, R>, ObjWithReadWriteLock<Set<String>>>>(
			new ConcurrentHashMap<ChannelContext<Ext, P, R>, ObjWithReadWriteLock<Set<String>>>());

	/**
	 * 与组解除绑定
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 */
	public void unbind(ChannelContext<Ext, P, R> channelContext)
	{
		Lock lock = channelmap.getLock().writeLock();

		try
		{
			ObjWithReadWriteLock<Set<String>> set = null;
			try
			{
				lock.lock();
				Map<ChannelContext<Ext, P, R>, ObjWithReadWriteLock<Set<String>>> m = channelmap.getObj();
				set = m.get(channelContext);
				m.remove(channelContext);
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			} finally
			{
				lock.unlock();
			}

			if (set != null)
			{
				Set<String> groups = set.getObj();
				if (groups != null && groups.size() > 0)
				{
					for (String groupid : groups)
					{
						unbind(groupid, channelContext);
					}
				}
			}
		} catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * 与组解除绑定
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param groupid the groupid
	 * @param channelContext the channel context
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午4:10:59
	 */
	private void unbind(String groupid, ChannelContext<Ext, P, R> channelContext)
	{
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> set = groupmap.getObj().get(groupid);

		if (set != null)
		{
			Lock lock1 = set.getLock().writeLock();
			try
			{
				lock1.lock();
				set.getObj().remove(channelContext);
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			} finally
			{
				lock1.unlock();
			}

			if (set.getObj().size() == 0)
			{
				Lock lock2 = groupmap.getLock().writeLock();
				try
				{
					lock2.lock();
					groupmap.getObj().remove(groupid);
				} catch (Exception e)
				{
					log.error(e.toString(), e);
				} finally
				{
					lock2.unlock();
				}
			}
		}
	}

	/**
	 * 和组绑定
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param groupid the userid
	 * @param channelContext the channel context
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:25:46
	 */
	public void bind(String groupid, ChannelContext<Ext, P, R> channelContext)
	{
		Lock lock1 = groupmap.getLock().writeLock();
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> channelContexts = null;
		try
		{
			lock1.lock();
			channelContexts = groupmap.getObj().get(groupid);
			if (channelContexts == null)
			{
				channelContexts = new ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>(new HashSet<ChannelContext<Ext, P, R>>());
			}
			groupmap.getObj().put(groupid, channelContexts);
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			lock1.unlock();
		}

		if (channelContexts != null)
		{
			Lock lock11 = channelContexts.getLock().writeLock();
			try
			{
				lock11.lock();
				channelContexts.getObj().add(channelContext);
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			} finally
			{
				lock11.unlock();
			}
		}

		Lock lock2 = channelmap.getLock().writeLock();
		ObjWithReadWriteLock<Set<String>> groups = null;// = channelmap.getObj().get(channelContext);
		try
		{
			lock2.lock();
			groups = channelmap.getObj().get(channelContext);
			if (groups == null)
			{
				groups = new ObjWithReadWriteLock<Set<String>>(new HashSet<String>());
			}
			channelmap.getObj().put(channelContext, groups);
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			lock2.unlock();
		}

		if (groups != null)
		{
			Lock lock22 = groups.getLock().writeLock();
			try
			{
				lock22.lock();
				groups.getObj().add(groupid);
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			} finally
			{
				lock22.unlock();
			}
		}
	}

	/**
	 * 一个组有哪些客户端
	 *
	 * @param groupid the groupid
	 * @return the obj with read write lock
	 */
	public ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> clients(String groupid)
	{
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> set = groupmap.getObj().get(groupid);
		return set;
	}

	/**
	 * 某个客户端在哪些组中
	 * @param channelContext
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午4:31:27
	 *
	 */
	public ObjWithReadWriteLock<Set<String>> groups(ChannelContext<Ext, P, R> channelContext)
	{
		ObjWithReadWriteLock<Set<String>> set = channelmap.getObj().get(channelContext);
		return set;
	}

	/**
	 * Instantiates a new groups.
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午1:28:28
	 */
	public Groups()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午1:28:28
	 */
	public static void main(String[] args)
	{

	}

}
