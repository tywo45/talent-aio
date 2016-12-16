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
import com.talent.aio.common.Node;
import com.talent.aio.common.intf.Packet;

/**
 * The Class Remotes.
 *
 * @author tanyaowu
 * @创建时间 2016年11月17日 下午1:12:56
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 */
public class ClientNodes <Ext, P extends Packet, R>
{

	/** remoteAndChannelContext key: "ip:port" value: ChannelContext. */
	private ObjWithReadWriteLock<DualHashBidiMap> map = new ObjWithReadWriteLock<>(new DualHashBidiMap());

	/**
	 * Gets the key.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @return the key
	 */
	public String getKey(ChannelContext<Ext, P, R> channelContext)
	{
		Node remotenode = channelContext.getClientNode();
		if (remotenode == null)
		{
			throw new RuntimeException("remotenode is null");
		}
		String key = getKey(remotenode.getIp(), remotenode.getPort());
		return key;
	}
	
	/**
	 * Gets the key.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param ip the ip
	 * @param port the port
	 * @return the key
	 */
	public String getKey(String ip, int port)
	{
		String key = ip + ":" + port;
		return key;
	}

	/**
	 * Removes映射
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
	 * 添加映射.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午2:25:46
	 */
	public void put(ChannelContext<Ext, P, R> channelContext)
	{
		String key = getKey(channelContext);
		Lock lock = map.getLock().writeLock();
		DualHashBidiMap m = map.getObj();

		try
		{
			lock.lock();
			m.put(key, channelContext);
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
	 * @param ip
	 * @param port
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午12:07:35
	 *
	 */
	@SuppressWarnings("unchecked")
	public ChannelContext<Ext, P, R> find(String ip, int port)
	{
		String key = getKey(ip, port);
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
	public ClientNodes()
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
	 * @return the map
	 */
	public ObjWithReadWriteLock<DualHashBidiMap> getMap()
	{
		return map;
	}

}
