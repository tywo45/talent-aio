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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ChannelContext.Stat;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.AioHandler;
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
public class ServerGroupContext<Ext, P extends Packet, R> extends GroupContext<Ext, P, R>
{
	static Logger log = LoggerFactory.getLogger(ServerGroupContext.class);
	/** The ip. */
	private String ip;

	/** The port. */
	private int port;

	/** The group executor. */
	private ThreadPoolExecutor groupExecutor = null;

	private AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler = null;

	protected ServerGroupStat serverGroupStat = new ServerGroupStat();

	/** The accept executor. */
	private ThreadPoolExecutor acceptExecutor = null;
	//	ThreadPoolExecutor(int corePoolSize,
	//            int maximumPoolSize,
	//            long keepAliveTime,
	//            TimeUnit unit,
	//            BlockingQueue<Runnable> workQueue,
	//            ThreadFactory threadFactory)

	/**
	 * Instantiates a new aio server config.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 */
	public ServerGroupContext(String ip, int port, AioHandler<Ext, P, R> aioHandler, AioListener<Ext, P, R> aioListener)
	{
		this(ip, port, aioHandler, aioListener, new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				DefaultThreadFactory.getInstance("t-aio-server-group")));
	}

	/**
	 * Instantiates a new server group context.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 * @param groupExecutor the group executor
	 */
	public ServerGroupContext(String ip, int port, AioHandler<Ext, P, R> aioHandler, AioListener<Ext, P, R> aioListener, ThreadPoolExecutor groupExecutor)
	{
		super((StringUtils.isBlank(ip) ? "0.0.0.0" : ip) + ":" + port, aioHandler, aioListener);
		this.ip = ip;
		this.port = port;
		this.groupExecutor = groupExecutor;
		this.acceptCompletionHandler = new AcceptCompletionHandler<>();

		this.acceptExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), DefaultThreadFactory.getInstance("t-aio-server-accept"));

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = ServerGroupContext.this.getConnections().getSet();
						ReadLock readLock = objWithReadWriteLock.getLock().readLock();
						try
						{
							readLock.lock();
							Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();

							for (ChannelContext<Ext, P, R> entry : set)
							{

								ChannelContext<Ext, P, R> channelContext = entry;
								Stat stat = channelContext.getStat();
								long timeLatestReceivedMsg = stat.getTimeLatestReceivedMsg();
								long timeLatestSentMsg = stat.getTimeLatestSentMsg();
								long compareTime = Math.max(timeLatestReceivedMsg, timeLatestSentMsg);
								long currtime = SystemTimer.currentTimeMillis();
								long interval = (currtime - compareTime);
								if (interval > heartbeatTimeout)
								{
									Aio.close(channelContext, "定时任务检查，" + channelContext.toString() + "隔了" + interval + "ms没有发送消息");
								}
							}
							log.error("[{}]:[{}]: curr:{}, accepted:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", SystemTimer.currentTimeMillis(), id, set.size(),
									serverGroupStat.getAccepted().get(), serverGroupStat.getClosed().get(), serverGroupStat.getReceivedPacket().get(),
									serverGroupStat.getReceivedBytes().get(), serverGroupStat.getHandledPacket().get(), serverGroupStat.getSentPacket().get(),
									serverGroupStat.getSentBytes().get());
						} catch (Throwable e)
						{
							log.error("", e);
						} finally
						{
							readLock.unlock();
							Thread.sleep(heartbeatTimeout / 2);
						}
					} catch (Throwable e)
					{
						log.error("", e);
					}
				}
			}
		}, "t-aio-timer-checkheartbeat-" + id).start();
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
	//	public ServerGroupContext(String ip, int port, AioHandler<Ext, P, R> aioHandler, SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor,
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
	public ThreadPoolExecutor getAcceptExecutor()
	{
		return acceptExecutor;
	}

	/**
	 * Sets the accept executor.
	 *
	 * @param acceptExecutor the acceptExecutor to set
	 */
	public void setAcceptExecutor(ThreadPoolExecutor acceptExecutor)
	{
		this.acceptExecutor = acceptExecutor;
	}

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

	/** 
	 * @see com.talent.aio.common.GroupContext#getGroupStat()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午1:59:07
	 * 
	 */
	@Override
	public GroupStat getGroupStat()
	{
		return serverGroupStat;
	}

	/**
	 * @return the acceptCompletionHandler
	 */
	public AcceptCompletionHandler<Ext, P, R> getAcceptCompletionHandler()
	{
		return acceptCompletionHandler;
	}

	/**
	 * @param acceptCompletionHandler the acceptCompletionHandler to set
	 */
	public void setAcceptCompletionHandler(AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler)
	{
		this.acceptCompletionHandler = acceptCompletionHandler;
	}
}
