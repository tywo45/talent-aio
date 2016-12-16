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

import org.apache.commons.collections.bidimap.DualHashBidiMap;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.Packet;

/**
 * The Class Remotes.
 *
 * @author tanyaowu
 * @创建时间 2016年11月17日 下午1:12:56
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 */
public class Users <Ext, P extends Packet, R>
{

	/** remoteAndChannelContext 
	 * key: "ip:port" 
	 * value: ChannelContext. 
	 **/
	private ObjWithReadWriteLock<DualHashBidiMap> map = new ObjWithReadWriteLock<>(new DualHashBidiMap());


	/**
	 * Removes映射.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 */
	public void remove(ChannelContext<Ext, P, R> channelContext)
	{
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap m = map.getObj();
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
	 * 
	 * @param userid
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:43:28
	 *
	 */
	public void remove(String userid)
	{
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap m = map.getObj();
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
	 * 添加映射.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param userid the userid
	 * @param channelContext the channel context
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:25:46
	 */
	public void put(String userid, ChannelContext<Ext, P, R> channelContext)
	{
		String key = userid;
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap m = map.getObj();

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
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param userid the userid
	 * @return the channel context
	 */
	@SuppressWarnings("unchecked")
	public ChannelContext<Ext, P, R> find(String userid)
	{
		String key = userid;
		Lock lock = map.getLock().readLock();
		DualHashBidiMap m = map.getObj();

		try
		{
			lock.lock();
			return (ChannelContext<Ext, P, R>) m.get(key);
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
		DualHashBidiMap dualHashBidiMap = new DualHashBidiMap();
		dualHashBidiMap.put("111", "111111");
		dualHashBidiMap.put("222", "111111");
		System.out.println(dualHashBidiMap.getKey("111111"));
	}

}
