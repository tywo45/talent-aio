package com.talent.aio.common.maintain;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithLock;
import com.talent.aio.common.intf.Packet;

/**
 *
 *
 * @author tanyaowu
 * @param <Ext> the generic type
 * @param <P> the generic type
 * @param <R> the generic type
 * @创建时间 2016年11月17日 下午1:12:56
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 */
class ChannelContextSetWithLock<SessionContext, P extends Packet, R>
{

	/** remoteAndChannelContext key: "ip:port" value: ChannelContext. */
	private ObjWithLock<Set<ChannelContext<SessionContext, P, R>>> setWithLock = new ObjWithLock<Set<ChannelContext<SessionContext, P, R>>>(new HashSet<ChannelContext<SessionContext, P, R>>());

	/**
	 * Adds the.
	 *
	 * @param channelContext the channel context
	 */
	public void add(ChannelContext<SessionContext, P, R> channelContext)
	{
		Lock lock = setWithLock.getLock().writeLock();

		try
		{
			lock.lock();
			Set<ChannelContext<SessionContext, P, R>> m = setWithLock.getObj();
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
	public boolean remove(ChannelContext<SessionContext, P, R> channelContext)
	{
		Lock lock = setWithLock.getLock().writeLock();

		try
		{
			lock.lock();
			Set<ChannelContext<SessionContext, P, R>> m = setWithLock.getObj();
			return m.remove(channelContext);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}
	
	public int size()
	{
		Lock lock = setWithLock.getLock().readLock();

		try
		{
			lock.lock();
			Set<ChannelContext<SessionContext, P, R>> m = setWithLock.getObj();
			return m.size();
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
	public ChannelContextSetWithLock()
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
	public ObjWithLock<Set<ChannelContext<SessionContext, P, R>>> getSetWithLock()
	{
		return setWithLock;
	}

}
