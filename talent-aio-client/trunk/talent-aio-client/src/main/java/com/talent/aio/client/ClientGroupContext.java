/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月16日 上午10:06:33
 *
 * **************************************************************************
 */
package com.talent.aio.client;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ChannelContext.Stat;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.threadpool.DefaultThreadFactory;
import com.talent.aio.common.utils.SystemTimer;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerGroupContext.
 *
 * @author tanyaowu
 * @param <Ext> the generic type
 * @param <P> the generic type
 * @param <R> the generic type
 * @创建时间 2016年11月16日 上午10:06:33
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月16日 | tanyaowu | 新建类
 */
public class ClientGroupContext<Ext, P extends Packet, R> extends GroupContext<Ext, P, R>
{
	static Logger log = LoggerFactory.getLogger(ClientGroupContext.class);

	/** The ip. */
	private String ip;

	/** The port. */
	private int port;

	/** The group executor. */
	private ExecutorService groupExecutor = null;

	private ClientGroupStat clientGroupStat = new ClientGroupStat();

	/**
	 * Instantiates a new aio server config.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 * @param aioListener the send listener
	 */
	public ClientGroupContext(String ip, int port, AioClientHandler<Ext, P, R> aioHandler, AioListener<Ext, P, R> aioListener)
	{
		this(ip, port, aioHandler, aioListener, new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				DefaultThreadFactory.getInstance("t-aio-client-group")));
	}

	/**
	 * Instantiates a new server group context.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 * @param aioListener the send listener
	 * @param groupExecutor the group executor
	 */
	public ClientGroupContext(String ip, int port, AioClientHandler<Ext, P, R> aioHandler, AioListener<Ext, P, R> aioListener, ExecutorService groupExecutor)
	{
		super((StringUtils.isBlank(ip) ? "0.0.0.0" : ip) + ":" + port, aioHandler, aioListener);
		this.ip = ip;
		this.port = port;
		this.groupExecutor = groupExecutor;

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = ClientGroupContext.this.getConnections().getSet();
						ReadLock readLock = objWithReadWriteLock.getLock().readLock();
						try
						{
							readLock.lock();
							Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
							long currtime = SystemTimer.currentTimeMillis();
							for (ChannelContext<Ext, P, R> entry : set)
							{
								ChannelContext<Ext, P, R> channelContext = entry;
								Stat stat = channelContext.getStat();
								long timeLatestReceivedMsg = stat.getTimeLatestReceivedMsg();
								long timeLatestSentMsg = stat.getTimeLatestSentMsg();
								long compareTime = Math.max(timeLatestReceivedMsg, timeLatestSentMsg);
								long interval = (currtime - compareTime);
								if (interval >= heartbeatTimeout / 2)
								{
									P packet = aioHandler.heartbeatPacket();
									if (packet != null)
									{
										log.error("{}发送心跳包", channelContext.toString());
										Aio.send(channelContext, packet);
									}
								}
							}
							log.error("[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", id, set.size(), clientGroupStat.getClosed().get(),
									clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(), clientGroupStat.getHandledPacket().get(),
									clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());
						} catch (Throwable e)
						{
							log.error("", e);
						} finally
						{
							readLock.unlock();
							Thread.sleep(heartbeatTimeout / 4);
						}
					} catch (Throwable e)
					{
						log.error("", e);
					}
				}
			}
		}, "t-aio-timer-sendheartbeat-" + id).start();
	}

	//	/**
	//	 * Instantiates a new server group context.
	//	 *
	//	 * @param ip the ip
	//	 * @param port the port
	//	 * @param aioHandler the aio handler
	//	 * @param decodeExecutor the decode executor
	//	 * @param closeExecutor the close executor
	//	 * @param handlerExecutorHighPrior the handler executor high prior
	//	 * @param handlerExecutorNormPrior the handler executor norm prior
	//	 * @param sendExecutorHighPrior the send executor high prior
	//	 * @param sendExecutorNormPrior the send executor norm prior
	//	 */
	//	public ClientGroupContext(String ip, int port, AioHandler<Ext, P, R> aioHandler,
	//			SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor,SynThreadPoolExecutor<SynRunnableIntf> closeExecutor, SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior,
	//			SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior, SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior,
	//			SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior)
	//	{
	//		super(aioHandler, decodeExecutor, closeExecutor, handlerExecutorHighPrior, handlerExecutorNormPrior, sendExecutorHighPrior, sendExecutorNormPrior);
	//		this.ip = ip;
	//		this.port = port;
	//	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:06:33
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the ip to set
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Gets the group executor.
	 *
	 * @return the groupExecutor
	 */
	public ExecutorService getGroupExecutor()
	{
		return groupExecutor;
	}

	/**
	 * Sets the group executor.
	 *
	 * @param groupExecutor the groupExecutor to set
	 */
	public void setGroupExecutor(ExecutorService groupExecutor)
	{
		this.groupExecutor = groupExecutor;
	}

	/**
	 * @return the clientGroupStat
	 */
	public ClientGroupStat getClientGroupStat()
	{
		return clientGroupStat;
	}

	/**
	 * @param clientGroupStat the clientGroupStat to set
	 */
	public void setClientGroupStat(ClientGroupStat clientGroupStat)
	{
		this.clientGroupStat = clientGroupStat;
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getGroupStat()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午2:02:06
	 * 
	 */
	@Override
	public GroupStat getGroupStat()
	{
		return clientGroupStat;
	}

}
