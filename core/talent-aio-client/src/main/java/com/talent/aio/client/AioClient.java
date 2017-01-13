package com.talent.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ChannelContext.Stat;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月2日 下午6:02:56
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月2日 | tanyaowu | 新建类
 *
 */
public class AioClient<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(AioClient.class);

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:35:17
	 * 
	 */
	public static void main(String[] args)
	{
	}

	private AsynchronousChannelGroup channelGroup;

	private ClientGroupContext<Ext, P, R> clientGroupContext;

	/**
	 * @param ip 可以为空
	 * @param port 
	 * @param aioDecoder
	 * @param aioEncoder
	 * @param aioHandler
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月15日 下午1:09:28
	 * 
	 */
	public AioClient(final ClientGroupContext<Ext, P, R> clientGroupContext) throws IOException
	{
		super();
		this.clientGroupContext = clientGroupContext;
		ExecutorService groupExecutor = clientGroupContext.getGroupExecutor();
		this.channelGroup = AsynchronousChannelGroup.withThreadPool(groupExecutor);

		startHeartbeatTask();
		
		startReconnTask();
	}

	//	/**
	//	 * 
	//	 * @param bindIp
	//	 * @param bindPort
	//	 * @param autoReconnect
	//	 * @return
	//	 * @throws Exception
	//	 *
	//	 * @author: tanyaowu
	//	 * @创建时间:　2016年12月19日 下午5:49:05
	//	 *
	//	 */
	//	public ClientChannelContext<Ext, P, R> connect(String bindIp, Integer bindPort, boolean autoReconnect) throws Exception
	//	{
	//		return connect(bindIp, bindPort, autoReconnect, null);
	//	}

	/**
	 * 
	 * @param bindIp 绑定本地ip
	 * @param bindPort 绑定本地port
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月19日 下午5:49:00
	 *
	 */
	public ClientChannelContext<Ext, P, R> connect(String bindIp, Integer bindPort) throws Exception
	{
		String ip = clientGroupContext.getIp();
		int port = clientGroupContext.getPort();

		AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open(channelGroup);
		asynchronousSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

		InetSocketAddress bind = null;
		if (bindPort != null && bindPort > 0)
		{
			if (StringUtils.isNotBlank(bindIp))
			{
				bind = new InetSocketAddress(bindIp, bindPort);
			} else
			{
				bind = new InetSocketAddress(bindPort);
			}
		}

		if (bind != null)
		{
			asynchronousSocketChannel.bind(bind);
		}

		Future<Void> future = asynchronousSocketChannel.connect(new InetSocketAddress(ip, port));
		try
		{
			future.get(5, TimeUnit.SECONDS);
			log.info("connected to {}:{}", ip, port);

			//			if (channelContext == null)
			//			{
			ClientChannelContext<Ext, P, R> channelContext = new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
			channelContext.setBindIp(bindIp);
			channelContext.setBindPort(bindPort);
			//			} else
			//			{
			//				channelContext.setAsynchronousSocketChannel(asynchronousSocketChannel);
			//			}

			ClientAioListener<Ext, P, R> clientAioListener = clientGroupContext.getClientAioListener();
			if (clientAioListener != null)
			{
				boolean f = clientAioListener.onAfterConnected(channelContext);
				if (!f)
				{
					log.warn("不允许连接:{}", channelContext);
					Aio.close(channelContext, "不允许连接");
					return null;
				}
			}

			ReadCompletionHandler<Ext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
			ByteBuffer byteBuffer = ByteBuffer.allocate(channelContext.getGroupContext().getReadBufferSize());
			asynchronousSocketChannel.read(byteBuffer, byteBuffer, readCompletionHandler);
			return channelContext;
		} catch (InterruptedException | ExecutionException | TimeoutException e)
		{
			throw e;
		}
	}

	/**
	 * @return the channelGroup
	 */
	public AsynchronousChannelGroup getChannelGroup()
	{
		return channelGroup;
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<Ext, P, R> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * 重连
	 * @param channelContext
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月19日 下午5:43:35
	 *
	 */
	public ClientChannelContext<Ext, P, R> reconnect(ClientChannelContext<Ext, P, R> channelContext) throws Exception
	{
		ClientChannelContext<Ext, P, R> newChannelContext = connect(channelContext.getBindIp(), channelContext.getBindPort());
		ClientGroupContext<Ext, P, R> clientGroupContext = (ClientGroupContext<Ext, P, R>) channelContext.getGroupContext();
		ClientAioListener<Ext, P, R> clientAioListener = clientGroupContext.getClientAioListener();
		if (clientAioListener != null)
		{
			clientAioListener.onAfterReconnected(newChannelContext, channelContext);
		}
		return newChannelContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<Ext, P, R> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}

	/**
	 * 定时任务：发心跳，重连(待实现)
	 * @author: tanyaowu
	 * @创建时间:　2017年1月2日 下午6:01:06
	 *
	 */
	private void startHeartbeatTask()
	{
		final ClientGroupStat clientGroupStat = clientGroupContext.getClientGroupStat();
		final ClientAioHandler<Ext, P, R> aioHandler = (ClientAioHandler<Ext, P, R>) clientGroupContext.getClientAioHandler();
		final long heartbeatTimeout = clientGroupContext.getHeartbeatTimeout();
		final String id = clientGroupContext.getId();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					ReadLock readLock = null;
					try
					{
						ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = clientGroupContext.getConnections().getSet();
						readLock = objWithReadWriteLock.getLock().readLock();
						readLock.lock();
						Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
						long currtime = SystemTimer.currentTimeMillis();
						for (ChannelContext<Ext, P, R> entry : set)
						{
							ClientChannelContext<Ext, P, R> channelContext = (ClientChannelContext<Ext, P, R>) entry;

							//							if (channelContext.isClosed()) //已经关闭了
							//							{
							//								if (channelContext.isAutoReconnect())
							//								{
							//									AioClient.this.reconnect(channelContext);
							//								}
							//							} else
							//							{
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
									log.info("{}发送心跳包", channelContext.toString());
									Aio.send(channelContext, packet);
								}
							}
							//							}
						}
						if (log.isInfoEnabled())
						{
							log.info("[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", id, set.size(), clientGroupStat.getClosed().get(),
									clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(), clientGroupStat.getHandledPacket().get(),
									clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());
						}

					} catch (Throwable e)
					{
						log.error("", e);
					} finally
					{
						try
						{
							if (readLock != null)
							{
								readLock.unlock();
							}
							Thread.sleep(heartbeatTimeout / 4);
						} catch (Exception e)
						{
							log.error(e.toString(), e);
						} finally
						{

						}
					}
				}
			}
		}, "t-aio-timer-heartbeat" + id).start();
	}

	/**
	 * 启动重连任务
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午5:48:17
	 *
	 */
	private void startReconnTask()
	{
		final ReconnConf<Ext, P, R> reconnConf = clientGroupContext.getReconnConf();
		if (reconnConf == null || reconnConf.getInterval() <= 0)
		{
			return;
		}

		final String id = clientGroupContext.getId();
		Thread thread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				LinkedBlockingQueue<ChannelContext<Ext, P, R>> queue = reconnConf.getQueue();
				while (true)
				{
					ClientChannelContext<Ext, P, R> channelContext = null;
					try
					{
						channelContext = (ClientChannelContext<Ext, P, R>) queue.take();
					} catch (InterruptedException e1)
					{
						log.error(e1.toString(), e1);
					}
					if (channelContext == null)
					{
						continue;
					}

					try
					{

						ClientChannelContext<Ext, P, R> newChannelContext = null;
						try
						{
							long currtime = SystemTimer.currentTimeMillis();
							long closetime = channelContext.getStat().getTimeClosed();
							long sleeptime = reconnConf.getInterval() - (currtime - closetime);
							if (sleeptime > 0)
							{
								Thread.sleep(sleeptime);
							}

							newChannelContext = reconnect(channelContext);
						} catch (java.lang.Throwable e)
						{
							log.error(e.toString(), e);
						}
						if (newChannelContext == null)
						{
							channelContext.setReConnCount(channelContext.getReConnCount() + 1);
							ReconnConf<Ext, P, R> reconnConf = channelContext.getGroupContext().getReconnConf();

//							if (reconnConf != null && reconnConf.getInterval() > 0)
//							{
								if (reconnConf.getRetryCount() <= 0 || reconnConf.getRetryCount() >= channelContext.getReConnCount())
								{
									queue.put(channelContext);
								}
//							}
							channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());
							continue;
						}

						channelContext.setReConnCount(0);
						ClientAioListener<Ext, P, R> clientAioListener = clientGroupContext.getClientAioListener();
						if (clientAioListener != null)
						{
							clientAioListener.onAfterReconnected(newChannelContext, channelContext);
						}

					} catch (java.lang.Throwable e)
					{
						log.error(e.toString(), e);
					}
				}
			}
		});
		thread.setName("t-aio-timer-reconnect" + id);
		thread.setDaemon(true);
		thread.start();

	}
}
