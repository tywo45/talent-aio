/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月17日 下午1:12:56
 *
 * **************************************************************************
 */
package com.talent.aio.common.maintain;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.Packet;

// TODO: Auto-generated Javadoc
/**
 * The Class Remotes.
 *
 * @author tanyaowu
 * @param <Ext> the generic type
 * @param <P> the generic type
 * @param <R> the generic type
 * @创建时间 2016年11月17日 下午1:12:56
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 */
public class Connections<Ext, P extends Packet, R>
{

	/** remoteAndChannelContext key: "ip:port" value: ChannelContext. */
	private ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> set = new ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>(new HashSet<ChannelContext<Ext, P, R>>());

	/**
	 * Adds the.
	 *
	 * @param channelContext the channel context
	 */
	public void add(ChannelContext<Ext, P, R> channelContext)
	{
		Lock lock = set.getLock().writeLock();

		try
		{
			lock.lock();
			Set<ChannelContext<Ext, P, R>> m = set.getObj();
			m.add(channelContext);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * Removes the.
	 *
	 * @param channelContext the channel context
	 * @return the channel context
	 */
	public boolean remove(ChannelContext<Ext, P, R> channelContext)
	{
		Lock lock = set.getLock().writeLock();

		try
		{
			lock.lock();
			Set<ChannelContext<Ext, P, R>> m = set.getObj();
			return m.remove(channelContext);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * Instantiates a new remotes.
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午1:12:56
	 */
	public Connections()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午1:12:56
	 */
	public void main(String[] args)
	{

	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> getSet()
	{
		return set;
	}

}
