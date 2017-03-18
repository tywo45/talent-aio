package com.talent.aio.client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Node;
import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月26日 下午9:36:54
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月26日 | tanyaowu | 新建类
 *
 */
public class ConnectionCompletionHandler<SessionContext, P extends Packet, R> implements CompletionHandler<Void, ConnectionCompletionVo<SessionContext, P, R> >
{
	private static Logger log = LoggerFactory.getLogger(ConnectionCompletionHandler.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月26日 下午9:36:55
	 * 
	 */
	public ConnectionCompletionHandler()
	{
		
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月26日 下午9:36:55
	 * 
	 */
	public static void main(String[] args)
	{
		
	}

	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param result
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月26日 下午9:39:18
	 * 
	 */
	@Override
	public void completed(Void result, ConnectionCompletionVo<SessionContext, P, R> attachment)
	{
		synchronized (attachment)
		{
			try
			{
				boolean isReconnect = attachment.isReconnect();
				ClientChannelContext<SessionContext, P, R> channelContext = attachment.getChannelContext();
				AsynchronousSocketChannel asynchronousSocketChannel = attachment.getAsynchronousSocketChannel();
				AioClient<SessionContext, P, R> aioClient = attachment.getAioClient();
				ClientGroupContext<SessionContext, P, R> clientGroupContext = aioClient.getClientGroupContext();
				Node serverNode = attachment.getServerNode();
				String bindIp = attachment.getBindIp();
				Integer bindPort = attachment.getBindPort();
				ClientAioListener<SessionContext, P, R> clientAioListener = clientGroupContext.getClientAioListener();

				if (isReconnect)
				{
					channelContext.setAsynchronousSocketChannel(asynchronousSocketChannel);
					channelContext.getDecodeRunnable().setCanceled(false);
					channelContext.getHandlerRunnableNormPrior().setCanceled(false);
					//		channelContext.getHandlerRunnableHighPrior().setCanceled(false);
					channelContext.getSendRunnableNormPrior().setCanceled(false);
					//		channelContext.getSendRunnableHighPrior().setCanceled(false);

					clientGroupContext.getCloseds().remove(channelContext);
				} else
				{
					channelContext = new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
					channelContext.setServerNode(serverNode);
					channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());
				}

				channelContext.setBindIp(bindIp);
				channelContext.setBindPort(bindPort);

				channelContext.setReconnCount(0);
				channelContext.setClosed(false);
				
				attachment.setChannelContext(channelContext);

				clientGroupContext.getConnecteds().add(channelContext);

				ReadCompletionHandler<SessionContext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
				ByteBuffer readByteBuffer = readCompletionHandler.getReadByteBuffer();//ByteBuffer.allocateDirect(channelContext.getGroupContext().getReadBufferSize());
				readByteBuffer.position(0);
				readByteBuffer.limit(readByteBuffer.capacity());
				asynchronousSocketChannel.read(readByteBuffer, readByteBuffer, readCompletionHandler);

				log.info("connected to {}", serverNode);
				
				try
				{
					clientAioListener.onAfterConnected(channelContext, !channelContext.isClosed(), isReconnect);
				} catch (Exception e1)
				{
					log.error(e1.toString(), e1);
				}
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			}
			
			attachment.notify();
		}
		
		
	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月26日 下午9:39:18
	 * 
	 */
	@Override
	public void failed(Throwable e, ConnectionCompletionVo<SessionContext, P, R> attachment)
	{
		synchronized (attachment)
		{
			ClientChannelContext<SessionContext, P, R> channelContext = null;
			ClientGroupContext<SessionContext, P, R> clientGroupContext = null;
			ClientAioListener<SessionContext, P, R> clientAioListener = null;
			try
			{
				log.error(e.toString(), e);

				boolean isReconnect = attachment.isReconnect();
				channelContext = attachment.getChannelContext();
				AsynchronousSocketChannel asynchronousSocketChannel = attachment.getAsynchronousSocketChannel();
				AioClient<SessionContext, P, R> aioClient = attachment.getAioClient();
				clientGroupContext = aioClient.getClientGroupContext();
				Node serverNode = attachment.getServerNode();
				clientAioListener = clientGroupContext.getClientAioListener();

				if (channelContext == null)
				{
					channelContext = new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
					channelContext.setServerNode(serverNode);
					channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());
				}

				if (!isReconnect) //不是重连，则是第一次连接，需要把channelContext加到closeds行列
				{
					clientGroupContext.getCloseds().add(channelContext);
				}
				
				attachment.setChannelContext(channelContext);
				
				try
				{
					clientAioListener.onAfterConnected(channelContext, !channelContext.isClosed(), isReconnect);
				} catch (Exception e1)
				{
					log.error(e1.toString(), e1);
				}
			} catch (Exception e1)
			{
				log.error(e1.toString(), e1);
			} finally
			{
				ReconnConf.put(channelContext);
				attachment.notify();
			}
		}
		

	}

}
