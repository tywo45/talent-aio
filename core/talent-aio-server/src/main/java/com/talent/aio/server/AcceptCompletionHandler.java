/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午1:31:04
 *
 * **************************************************************************
 */
package com.talent.aio.server;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.server.intf.ServerAioListener;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午1:31:04
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class AcceptCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<AsynchronousSocketChannel, AioServer<Ext, P, R>>
{

	private static Logger log = LoggerFactory.getLogger(AioServer.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public AcceptCompletionHandler()
	{

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param asynchronousSocketChannel
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:28:05
	 * 
	 */
	@Override
	public void completed(AsynchronousSocketChannel asynchronousSocketChannel, AioServer<Ext, P, R> aioServer)
	{
		try
		{
			ServerGroupContext<Ext, P, R> serverGroupContext = aioServer.getServerGroupContext();
			ServerGroupStat serverGroupStat = serverGroupContext.getServerGroupStat();
			serverGroupStat.getAccepted().incrementAndGet();

			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 32 * 1024);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

			ServerChannelContext<Ext, P, R> channelContext = new ServerChannelContext<>(serverGroupContext, asynchronousSocketChannel);
			ServerAioListener<Ext, P, R> serverAioListener = serverGroupContext.getServerAioListener();
			if (serverAioListener != null)
			{
				boolean f = serverAioListener.onAfterConnected(channelContext);
				if (!f)
				{
					log.warn("不允许连接:{}", channelContext);
					Aio.close(channelContext, "不允许连接");
					return;
				}
			}
			

			ReadCompletionHandler<Ext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
			ByteBuffer newByteBuffer = ByteBuffer.allocate(channelContext.getGroupContext().getReadBufferSize());
			asynchronousSocketChannel.read(newByteBuffer, newByteBuffer, readCompletionHandler);
		} catch (Exception e)
		{
			log.error("", e);
		} finally
		{
			AsynchronousServerSocketChannel serverSocketChannel = aioServer.getServerSocketChannel();
			serverSocketChannel.accept(aioServer, this);
		}
	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param aioServer
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:28:05
	 * 
	 */
	@Override
	public void failed(Throwable exc, AioServer<Ext, P, R> aioServer)
	{
		AsynchronousServerSocketChannel serverSocketChannel = aioServer.getServerSocketChannel();
		serverSocketChannel.accept(aioServer, this);

		String ip = aioServer.getServerGroupContext().getIp();
		String ipstr = StringUtils.isNotBlank(ip) ? ip : "0.0.0.0";
		ipstr += ":" + aioServer.getServerGroupContext().getPort();
		log.error("[" + ipstr + "]监听出现异常", exc);

	}

}
