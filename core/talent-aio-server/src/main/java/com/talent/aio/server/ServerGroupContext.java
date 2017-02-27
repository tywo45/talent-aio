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
package com.talent.aio.server;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ChannelContext.Stat;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.ObjWithLock;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.threadpool.DefaultThreadFactory;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.server.intf.ServerAioHandler;
import com.talent.aio.server.intf.ServerAioListener;

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
public class ServerGroupContext<SessionContext, P extends Packet, R> extends GroupContext<SessionContext, P, R>
{
	static Logger log = LoggerFactory.getLogger(ServerGroupContext.class);

	/** The group executor. */
	private ThreadPoolExecutor groupExecutor = null;

	private AcceptCompletionHandler<SessionContext, P, R> acceptCompletionHandler = null;

	private ServerAioHandler<SessionContext, P, R> serverAioHandler = null;

	private ServerAioListener<SessionContext, P, R> serverAioListener = null;

	protected ServerGroupStat serverGroupStat = new ServerGroupStat();

	/** The accept executor. */
	//private ThreadPoolExecutor acceptExecutor = null;

	private Thread checkHeartbeatThread = null;
	
	

	/**
	 * 
	 * @param aioHandler
	 * @param aioListener
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月2日 下午1:40:29
	 *
	 */
	public ServerGroupContext(ServerAioHandler<SessionContext, P, R> aioHandler, ServerAioListener<SessionContext, P, R> aioListener)
	{
		this(aioHandler, aioListener, new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				DefaultThreadFactory.getInstance("t-aio-server-group")));
	}

	/**
	 * 
	 * @param serverAioHandler
	 * @param serverAioListener
	 * @param groupExecutor
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月2日 下午1:40:11
	 *
	 */
	public ServerGroupContext(ServerAioHandler<SessionContext, P, R> serverAioHandler, ServerAioListener<SessionContext, P, R> serverAioListener, ThreadPoolExecutor groupExecutor)
	{
		super();
		this.groupExecutor = groupExecutor;
		this.acceptCompletionHandler = new AcceptCompletionHandler<>();
		this.setServerAioHandler(serverAioHandler);
		this.setServerAioListener(serverAioListener);

		//this.acceptExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), DefaultThreadFactory.getInstance("t-aio-server-accept"));

		checkHeartbeatThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				long sleeptime = heartbeatTimeout / 2;
				while (!isStopped())
				{
					long start = SystemTimer.currentTimeMillis();
					ObjWithLock<Set<ChannelContext<SessionContext, P, R>>> objWithLock = ServerGroupContext.this.getConnections().getSetWithLock();
					ReadLock readLock = objWithLock.getLock().readLock();
					long start1 = 0;
					int count = 0;
					try
					{
						readLock.lock();
						start1 = SystemTimer.currentTimeMillis();
						Set<ChannelContext<SessionContext, P, R>> set = objWithLock.getObj();
						
						for (ChannelContext<SessionContext, P, R> entry : set)
						{
							count++;
							ChannelContext<SessionContext, P, R> channelContext = entry;
							Stat stat = channelContext.getStat();
							long timeLatestReceivedMsg = stat.getLatestTimeOfReceivedPacket();
							long timeLatestSentMsg = stat.getLatestTimeOfSentPacket();
							long compareTime = Math.max(timeLatestReceivedMsg, timeLatestSentMsg);
							long currtime = SystemTimer.currentTimeMillis();
							long interval = (currtime - compareTime);
							if (interval > heartbeatTimeout)
							{
								Aio.remove(channelContext, interval + "ms没有收发消息");
							}
						}

						if (log.isInfoEnabled())
						{
							log.info("[{}]:[{}]: curr:{}, accepted:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", SystemTimer.currentTimeMillis(), id,
									set.size(), serverGroupStat.getAccepted().get(), serverGroupStat.getClosed().get(), serverGroupStat.getReceivedPacket().get(),
									serverGroupStat.getReceivedBytes().get(), serverGroupStat.getHandledPacket().get(), serverGroupStat.getSentPacket().get(),
									serverGroupStat.getSentBytes().get());
						}
					} catch (Throwable e)
					{
						log.error("", e);
					} finally
					{
						try
						{
							if (log.isInfoEnabled())
							{
								long end = SystemTimer.currentTimeMillis();
								long iv1 = start1 - start;
								long iv = end - start1;
								log.info("检查心跳, 共{}个连接, 取锁耗时{}ms, 循环耗时{}ms, 心跳时间:{}", count, iv1, iv, heartbeatTimeout);
							}
							
							readLock.unlock();
							Thread.sleep(sleeptime);
						} catch (Exception e)
						{
							log.error("", e);
						}
					}
				}
			}
		}, "t-aio-timer-checkheartbeat-" + id);
		checkHeartbeatThread.setDaemon(true);
		checkHeartbeatThread.setPriority(Thread.MIN_PRIORITY);
		checkHeartbeatThread.start();
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
	//	public ServerGroupContext(String ip, int port, AioHandler<SessionContext, P, R> aioHandler, SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor,
	//			SynThreadPoolExecutor<SynRunnableIntf> closeExecutor, SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior,
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
	 * Gets the group executor.
	 *
	 * @return the groupExecutor
	 */
	public ThreadPoolExecutor getGroupExecutor()
	{
		return groupExecutor;
	}

	/**
	 * Sets the group executor.
	 *
	 * @param groupExecutor the groupExecutor to set
	 */
	public void setGroupExecutor(ThreadPoolExecutor groupExecutor)
	{
		this.groupExecutor = groupExecutor;
	}

	/**
	 * Gets the accept executor.
	 *
	 * @return the acceptExecutor
	 */
//	public ThreadPoolExecutor getAcceptExecutor()
//	{
//		return acceptExecutor;
//	}
//
//	/**
//	 * Sets the accept executor.
//	 *
//	 * @param acceptExecutor the acceptExecutor to set
//	 */
//	public void setAcceptExecutor(ThreadPoolExecutor acceptExecutor)
//	{
//		this.acceptExecutor = acceptExecutor;
//	}

	//	/**
	//	 * @return the serverGroupStat
	//	 */
	//	public ServerGroupStat getServerGroupStat()
	//	{
	//		return serverGroupStat;
	//	}

	//	/**
	//	 * @param serverGroupStat the serverGroupStat to set
	//	 */
	//	public void setServerGroupStat(ServerGroupStat serverGroupStat)
	//	{
	//		this.serverGroupStat = serverGroupStat;
	//	}

	public ServerGroupStat getServerGroupStat()
	{
		return serverGroupStat;
	}

	/**
	 * @return the acceptCompletionHandler
	 */
	public AcceptCompletionHandler<SessionContext, P, R> getAcceptCompletionHandler()
	{
		return acceptCompletionHandler;
	}

	/**
	 * @param acceptCompletionHandler the acceptCompletionHandler to set
	 */
	public void setAcceptCompletionHandler(AcceptCompletionHandler<SessionContext, P, R> acceptCompletionHandler)
	{
		this.acceptCompletionHandler = acceptCompletionHandler;
	}

	/**
	 * @return the serverAioHandler
	 */
	public ServerAioHandler<SessionContext, P, R> getServerAioHandler()
	{
		return serverAioHandler;
	}

	/**
	 * @param serverAioHandler the serverAioHandler to set
	 */
	public void setServerAioHandler(ServerAioHandler<SessionContext, P, R> serverAioHandler)
	{
		this.serverAioHandler = serverAioHandler;
	}

	/**
	 * @return the serverAioListener
	 */
	public ServerAioListener<SessionContext, P, R> getServerAioListener()
	{
		return serverAioListener;
	}

	/**
	 * @param serverAioListener the serverAioListener to set
	 */
	public void setServerAioListener(ServerAioListener<SessionContext, P, R> serverAioListener)
	{
		this.serverAioListener = serverAioListener;
		
		if (this.serverAioListener == null)
		{
			this.serverAioListener = new DefaultServerAioListener<SessionContext, P, R>();
		}
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getAioHandler()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:34:37
	 * 
	 */
	@Override
	public AioHandler<SessionContext, P, R> getAioHandler()
	{
		return this.getServerAioHandler();
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getGroupStat()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:34:37
	 * 
	 */
	@Override
	public GroupStat getGroupStat()
	{
		return this.getServerGroupStat();
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getAioListener()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:34:37
	 * 
	 */
	@Override
	public AioListener<SessionContext, P, R> getAioListener()
	{
		return getServerAioListener();
	}
}
