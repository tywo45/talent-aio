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
package com.talent.aio.common;

import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.ReconnConf;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.ClientNodes;
import com.talent.aio.common.maintain.Closeds;
import com.talent.aio.common.maintain.Connecteds;
import com.talent.aio.common.maintain.Connections;
import com.talent.aio.common.maintain.Groups;
import com.talent.aio.common.maintain.Syns;
import com.talent.aio.common.maintain.Users;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.threadpool.DefaultThreadFactory;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月16日 上午10:06:33
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月16日 | tanyaowu | 新建类
 *
 */
public abstract class GroupContext<SessionContext, P extends Packet, R>
{
	static Logger log = LoggerFactory.getLogger(GroupContext.class);

	public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 1;

	/**
	 * 默认的心跳超时时间(单位: 毫秒)
	 */
	public static final long DEFAULT_HEARTBEAT_TIMEOUT = 1000 * 120;

	/** 
	 * 默认的接收数据的buffer size
	 */
	public static final int READ_BUFFER_SIZE = Integer.getInteger("talent.aio.default.read.buffer.size", 2048);

//	public static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 4;

	public static final long KEEP_ALIVE_TIME = 9000000L;
	

	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

	/**
	 * 心跳超时时间(单位: 毫秒)
	 */
	protected long heartbeatTimeout = DEFAULT_HEARTBEAT_TIMEOUT;

	/**
	 * 接收数据的buffer size
	 */
	protected int readBufferSize = READ_BUFFER_SIZE;
	
	protected ReconnConf<SessionContext, P, R> reconnConf;//重连配置

	//	private AioHandler<SessionContext, P, R> aioHandler;
	//
	//	private AioListener<SessionContext, P, R> aioListener;
	/**
	 * 解码线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor = null;

//	/**
//	 * 关闭连接的线程池
//	 */
//	private SynThreadPoolExecutor<SynRunnableIntf> closeExecutor = null;

//	/**
//	 * 高优先级的业务处理线程池
//	 */
//	private SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior = null;
	/**
	 * 低优先级的业务处理线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior = null;

//	/**
//	 * 高优先级的消息发送线程池
//	 */
//	private SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior = null;
	/**
	 * 低优先级的消息发送线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior = null;
	
	

	private ThreadPoolExecutor closePoolExecutor = null;
	
	

	protected ClientNodes<SessionContext, P, R> clientNodes = new ClientNodes<>();
	protected Connections<SessionContext, P, R> connections = new Connections<>();
	protected Connecteds<SessionContext, P, R> connecteds = new Connecteds<>();
	protected Closeds<SessionContext, P, R> closeds = new Closeds<>();
	
	protected Groups<SessionContext, P, R> groups = new Groups<>();
	protected Users<SessionContext, P, R> users = new Users<>();
	protected Syns<SessionContext, P, R> syns = new Syns<>();
	
	/**
	 * packet编码成bytebuffer时，是否与ChannelContext相关，false: packet编码与ChannelContext无关
	 */
	private boolean isEncodeCareWithChannelContext  = true;

	protected String id;
	
	private boolean isStopped = false;

	
	
	private final static AtomicInteger ID_ATOMIC = new AtomicInteger();


	/**
	 * @param serverIp
	 * @param serverPort
	 * @param aioHandler
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:21:58
	 * 
	 */
	public GroupContext()
	{
		super();
		this.id = ID_ATOMIC.incrementAndGet() + "";
		
//		LinkedBlockingQueue<Runnable> poolQueueHighPrior = new LinkedBlockingQueue<Runnable>();
//		SynThreadPoolExecutor<SynRunnableIntf> executorHighPrior = new SynThreadPoolExecutor<SynRunnableIntf>(CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_TIME, poolQueueHighPrior,
//				DefaultThreadFactory.getInstance("t-aio-high-prior", Thread.MAX_PRIORITY), "t-aio-high-prior");
//		executorHighPrior.prestartAllCoreThreads();

		LinkedBlockingQueue<Runnable> poolQueueNormPrior = new LinkedBlockingQueue<Runnable>();
		SynThreadPoolExecutor<SynRunnableIntf> executorNormPrior = new SynThreadPoolExecutor<SynRunnableIntf>(CORE_POOL_SIZE, CORE_POOL_SIZE, KEEP_ALIVE_TIME, poolQueueNormPrior,
				DefaultThreadFactory.getInstance("t-aio-norm-prior", Thread.NORM_PRIORITY), "t-aio-norm-prior");
		executorNormPrior.prestartAllCoreThreads();

		decodeExecutor = executorNormPrior;
//		closeExecutor = executorNormPrior;//executorHighPrior;
//		handlerExecutorHighPrior = executorNormPrior;//executorHighPrior;
		handlerExecutorNormPrior = executorNormPrior;
//		sendExecutorHighPrior = executorNormPrior;//executorHighPrior;
		sendExecutorNormPrior = executorNormPrior;
		
		LinkedBlockingQueue<Runnable> closeQueue = new LinkedBlockingQueue<Runnable>();
		closePoolExecutor = new ThreadPoolExecutor(0, CORE_POOL_SIZE, 9, TimeUnit.SECONDS, closeQueue,
				DefaultThreadFactory.getInstance("t-aio-close", Thread.NORM_PRIORITY));

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:06:33
	 * 
	 */
	public static void main(String[] args)
	{

	}

	//	/**
	//	 * @return the aioHandler
	//	 */
	//	public AioHandler<SessionContext, P, R> getAioHandler()
	//	{
	//		return aioHandler;
	//	}
	//
	//	/**
	//	 * @param aioHandler the aioHandler to set
	//	 */
	//	public void setAioHandler(AioHandler<SessionContext, P, R> aioHandler)
	//	{
	//		this.aioHandler = aioHandler;
	//	}

	/**
	 * @return the decodeExecutor
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getDecodeExecutor()
	{
		return decodeExecutor;
	}

	/**
	 * @param decodeExecutor the decodeExecutor to set
	 */
	public void setDecodeExecutor(SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor)
	{
		this.decodeExecutor = decodeExecutor;
	}

//	/**
//	 * @return the handlerExecutorHighPrior
//	 */
//	public SynThreadPoolExecutor<SynRunnableIntf> getHandlerExecutorHighPrior()
//	{
//		return handlerExecutorHighPrior;
//	}
//
//	/**
//	 * @param handlerExecutorHighPrior the handlerExecutorHighPrior to set
//	 */
//	public void setHandlerExecutorHighPrior(SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior)
//	{
//		this.handlerExecutorHighPrior = handlerExecutorHighPrior;
//	}

	/**
	 * @return the handlerExecutorNormPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getHandlerExecutorNormPrior()
	{
		return handlerExecutorNormPrior;
	}

	/**
	 * @param handlerExecutorNormPrior the handlerExecutorNormPrior to set
	 */
	public void setHandlerExecutorNormPrior(SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior)
	{
		this.handlerExecutorNormPrior = handlerExecutorNormPrior;
	}

//	/**
//	 * @return the sendExecutorHighPrior
//	 */
//	public SynThreadPoolExecutor<SynRunnableIntf> getSendExecutorHighPrior()
//	{
//		return sendExecutorHighPrior;
//	}
//
//	/**
//	 * @param sendExecutorHighPrior the sendExecutorHighPrior to set
//	 */
//	public void setSendExecutorHighPrior(SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior)
//	{
//		this.sendExecutorHighPrior = sendExecutorHighPrior;
//	}

	/**
	 * @return the sendExecutorNormPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getSendExecutorNormPrior()
	{
		return sendExecutorNormPrior;
	}

	/**
	 * @param sendExecutorNormPrior the sendExecutorNormPrior to set
	 */
	public void setSendExecutorNormPrior(SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior)
	{
		this.sendExecutorNormPrior = sendExecutorNormPrior;
	}

	/**
	 * @return the byteOrder
	 */
	public ByteOrder getByteOrder()
	{
		return byteOrder;
	}

	/**
	 * @param byteOrder the byteOrder to set
	 */
	public void setByteOrder(ByteOrder byteOrder)
	{
		this.byteOrder = byteOrder;
	}

	/**
	 * @return the ipPortMaps
	 */
	public ClientNodes<SessionContext, P, R> getClientNodes()
	{
		return clientNodes;
	}

	/**
	 * @param remotes the ipPortMaps to set
	 */
	public void setClientNodes(ClientNodes<SessionContext, P, R> clientNodes)
	{
		this.clientNodes = clientNodes;
	}

	/**
	 * @return the groups
	 */
	public Groups<SessionContext, P, R> getGroups()
	{
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Groups<SessionContext, P, R> groups)
	{
		this.groups = groups;
	}

	/**
	 * @return the users
	 */
	public Users<SessionContext, P, R> getUsers()
	{
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Users<SessionContext, P, R> users)
	{
		this.users = users;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the heartbeatTimeout
	 */
	public long getHeartbeatTimeout()
	{
		return heartbeatTimeout;
	}

	/**
	 * @param heartbeatTimeout the heartbeatTimeout to set
	 */
	public void setHeartbeatTimeout(long heartbeatTimeout)
	{
		this.heartbeatTimeout = heartbeatTimeout;
	}

//	/**
//	 * @return the closeExecutor
//	 */
//	public SynThreadPoolExecutor<SynRunnableIntf> getCloseExecutor()
//	{
//		return closeExecutor;
//	}
//
//	/**
//	 * @param closeExecutor the closeExecutor to set
//	 */
//	public void setCloseExecutor(SynThreadPoolExecutor<SynRunnableIntf> closeExecutor)
//	{
//		this.closeExecutor = closeExecutor;
//	}

	/**
	 * @return the connections
	 */
	public Connections<SessionContext, P, R> getConnections()
	{
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	public void setConnections(Connections<SessionContext, P, R> connections)
	{
		this.connections = connections;
	}

	//	/**
	//	 * @return the aioListener
	//	 */
	//	public AioListener<SessionContext, P, R> getAioListener()
	//	{
	//		return aioListener;
	//	}
	//
	//	/**
	//	 * @param aioListener the aioListener to set
	//	 */
	//	public void setSendListener(AioListener<SessionContext, P, R> aioListener)
	//	{
	//		this.aioListener = aioListener;
	//	}

	/**
	 * @return the readBufferSize
	 */
	public int getReadBufferSize()
	{
		return readBufferSize;
	}

	/**
	 * @param readBufferSize the readBufferSize to set
	 */
	public void setReadBufferSize(int readBufferSize)
	{
		this.readBufferSize = readBufferSize;
	}

	/**
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月20日 上午11:32:02
	 * 
	 */
	public abstract AioHandler<SessionContext, P, R> getAioHandler();

	/**
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月20日 上午11:33:02
	 * 
	 */
	public abstract GroupStat getGroupStat();

	/**
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月20日 上午11:33:28
	 * 
	 */
	public abstract AioListener<SessionContext, P, R> getAioListener();

	/**
	 * @return the reconnConf
	 */
	public ReconnConf<SessionContext, P, R> getReconnConf()
	{
		return reconnConf;
	}

	/**
	 * @return the syns
	 */
	public Syns<SessionContext, P, R> getSyns()
	{
		return syns;
	}

	/**
	 * @param syns the syns to set
	 */
	public void setSyns(Syns<SessionContext, P, R> syns)
	{
		this.syns = syns;
	}

	

	/**
	 * @return the connecteds
	 */
	public Connecteds<SessionContext, P, R> getConnecteds()
	{
		return connecteds;
	}

	/**
	 * @param connecteds the connecteds to set
	 */
	public void setConnecteds(Connecteds<SessionContext, P, R> connecteds)
	{
		this.connecteds = connecteds;
	}

	/**
	 * @return the closeds
	 */
	public Closeds<SessionContext, P, R> getCloseds()
	{
		return closeds;
	}

	/**
	 * @param closeds the closeds to set
	 */
	public void setCloseds(Closeds<SessionContext, P, R> closeds)
	{
		this.closeds = closeds;
	}

	/**
	 * @return the isEncodeCareWithChannelContext
	 */
	public boolean isEncodeCareWithChannelContext()
	{
		return isEncodeCareWithChannelContext;
	}

	/**
	 * @param isEncodeCareWithChannelContext the isEncodeCareWithChannelContext to set
	 */
	public void setEncodeCareWithChannelContext(boolean isEncodeCareWithChannelContext)
	{
		this.isEncodeCareWithChannelContext = isEncodeCareWithChannelContext;
	}

	/**
	 * @return the isStop
	 */
	public boolean isStopped()
	{
		return isStopped;
	}

	/**
	 * @param isStop the isStop to set
	 */
	public void setStopped(boolean isStopped)
	{
		this.isStopped = isStopped;
	}

	/**
	 * @return the closePoolExecutor
	 */
	public ThreadPoolExecutor getClosePoolExecutor()
	{
		return closePoolExecutor;
	}

	/**
	 * @param closePoolExecutor the closePoolExecutor to set
	 */
	public void setClosePoolExecutor(ThreadPoolExecutor closePoolExecutor)
	{
		this.closePoolExecutor = closePoolExecutor;
	}
}
