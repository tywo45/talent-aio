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

import java.util.concurrent.locks.Lock;

import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithLock;
import com.talent.aio.common.intf.Packet;

/**
 * The Class Users.
 *
 * @param <Ext> the generic type
 * @param <P> the generic type
 * @param <R> the generic type
 */
public class Users<SessionContext, P extends Packet, R>
{

	/**
	 * key: userid
	 * value: ChannelContext
	 */
	private ObjWithLock<DualHashBidiMap<String, ChannelContext<SessionContext, P, R>>> map = new ObjWithLock<DualHashBidiMap<String, ChannelContext<SessionContext, P, R>>>(
			new DualHashBidiMap<String, ChannelContext<SessionContext, P, R>>());

	/**
	 * @return the map
	 */
	public ObjWithLock<DualHashBidiMap<String, ChannelContext<SessionContext, P, R>>> getMap()
	{
		return map;
	}

	/**
	 * 解除绑定
	 *
	 * @param channelContext the channel context
	 */
	public void unbind(ChannelContext<SessionContext, P, R> channelContext)
	{
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap<String, ChannelContext<SessionContext, P, R>> m = map.getObj();
		try
		{
			lock.lock();
			m.removeValue(channelContext);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * 解除绑定
	 *
	 * @param userid the userid
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:43:28
	 */
	public void unbind(String userid)
	{
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap<String, ChannelContext<SessionContext, P, R>> m = map.getObj();
		try
		{
			lock.lock();
			m.remove(userid);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * 绑定userid.
	 *
	 * @param userid the userid
	 * @param channelContext the channel context
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:25:46
	 */
	public void bind(String userid, ChannelContext<SessionContext, P, R> channelContext)
	{
		String key = userid;
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap<String, ChannelContext<SessionContext, P, R>> m = map.getObj();

		try
		{
			lock.lock();
			m.put(key, channelContext);
			channelContext.setUserid(userid);
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * Find.
	 *
	 * @param userid the userid
	 * @return the channel context
	 */
	public ChannelContext<SessionContext, P, R> find(String userid)
	{
		String key = userid;
		Lock lock = map.getLock().readLock();
		DualHashBidiMap<String, ChannelContext<SessionContext, P, R>> m = map.getObj();

		try
		{
			lock.lock();
			return (ChannelContext<SessionContext, P, R>) m.get(key);
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
	public Users()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午1:12:56
	 */
	public static void main(String[] args)
	{
		DualHashBidiMap<String, String> dualHashBidiMap = new DualHashBidiMap<>();
		dualHashBidiMap.put("111", "111111");
		dualHashBidiMap.put("222", "111111");
		System.out.println(dualHashBidiMap.getKey("111111"));
	}

}
