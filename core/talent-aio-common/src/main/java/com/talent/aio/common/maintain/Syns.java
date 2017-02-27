package com.talent.aio.common.maintain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.talent.aio.common.ObjWithLock;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月12日 下午8:21:44
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月12日 | tanyaowu | 新建类
 *
 */
public class Syns<SessionContext, P extends Packet, R>
{

	/** remoteAndChannelContext key: "ip:port" value: ChannelContext. */
	private ObjWithLock<Map<Integer, P>> map = new ObjWithLock<Map<Integer, P>>(new HashMap<Integer, P>());

	/**
	 * Adds the.
	 *
	 * @param channelContext the channel context
	 */
	public void put(Integer synSeq, P packet)
	{
		Lock lock = map.getLock().writeLock();
		try
		{
			lock.lock();
			Map<Integer, P> m = map.getObj();
			m.put(synSeq, packet);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	public P remove(Integer synSeq)
	{
		Lock lock = map.getLock().writeLock();
		try
		{
			lock.lock();
			Map<Integer, P> m = map.getObj();
			P packet = m.remove(synSeq);
			return packet;
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
	public Syns()
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
	public ObjWithLock<Map<Integer, P>> getMap()
	{
		return map;
	}

}
