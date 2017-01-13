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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.threadpool.DefaultThreadFactory;

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

	private ClientAioHandler<Ext, P, R> clientAioHandler = null;

	private ClientAioListener<Ext, P, R> clientAioListener = null;

	private ClientGroupStat clientGroupStat = new ClientGroupStat();

	/**
	 * 不重连
	 * @param ip
	 * @param port
	 * @param aioHandler
	 * @param aioListener
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午5:01:50
	 *
	 */
	public ClientGroupContext(String ip, int port, ClientAioHandler<Ext, P, R> aioHandler, ClientAioListener<Ext, P, R> aioListener)
	{
		this(ip, port, aioHandler, aioListener, null, new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				DefaultThreadFactory.getInstance("t-aio-client-group")));
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param aioHandler
	 * @param aioListener
	 * @param reconnConf 不需要重连就为空
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午4:30:53
	 *
	 */
	public ClientGroupContext(String ip, int port, ClientAioHandler<Ext, P, R> aioHandler, ClientAioListener<Ext, P, R> aioListener, ReconnConf<Ext, P, R> reconnConf)
	{
		this(ip, port, aioHandler, aioListener, reconnConf, new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				DefaultThreadFactory.getInstance("t-aio-client-group")));
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param aioHandler
	 * @param aioListener
	 * @param reconnConf 不需要重连就为空
	 * @param groupExecutor
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午4:29:48
	 *
	 */
	public ClientGroupContext(String ip, int port, ClientAioHandler<Ext, P, R> aioHandler, ClientAioListener<Ext, P, R> aioListener, ReconnConf<Ext, P, R> reconnConf,
			ExecutorService groupExecutor)
	{
		super((StringUtils.isBlank(ip) ? "0.0.0.0" : ip) + ":" + port);

		this.setClientAioHandler(aioHandler);
		this.setClientAioListener(aioListener);

		this.reconnConf = reconnConf;
		this.ip = ip;
		this.port = port;
		this.groupExecutor = groupExecutor;
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
	 * @param clientGroupStat the clientGroupStat to set
	 */
	public void setClientGroupStat(ClientGroupStat clientGroupStat)
	{
		this.clientGroupStat = clientGroupStat;
	}

	public ClientGroupStat getClientGroupStat()
	{
		return clientGroupStat;
	}

	/**
	 * @return the clientAioHandler
	 */
	public ClientAioHandler<Ext, P, R> getClientAioHandler()
	{
		return clientAioHandler;
	}

	/**
	 * @param clientAioHandler the clientAioHandler to set
	 */
	public void setClientAioHandler(ClientAioHandler<Ext, P, R> clientAioHandler)
	{
		this.clientAioHandler = clientAioHandler;
	}

	/**
	 * @return the clientAioListener
	 */
	public ClientAioListener<Ext, P, R> getClientAioListener()
	{
		return clientAioListener;
	}

	/**
	 * @param clientAioListener the clientAioListener to set
	 */
	public void setClientAioListener(ClientAioListener<Ext, P, R> clientAioListener)
	{
		this.clientAioListener = clientAioListener;
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getAioHandler()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:33:46
	 * 
	 */
	@Override
	public AioHandler<Ext, P, R> getAioHandler()
	{
		return this.getClientAioHandler();
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getGroupStat()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:33:46
	 * 
	 */
	@Override
	public GroupStat getGroupStat()
	{
		return this.getClientGroupStat();
	}

	/** 
	 * @see com.talent.aio.common.GroupContext#getAioListener()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:33:46
	 * 
	 */
	@Override
	public AioListener<Ext, P, R> getAioListener()
	{
		return this.getClientAioListener();
	}

	/**
	 * @param reconnConf the reconnConf to set
	 */
	public void setReconnConf(ReconnConf reconnConf)
	{
		this.reconnConf = reconnConf;
	}

}
