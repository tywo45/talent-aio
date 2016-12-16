/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:35:17
 *
 * **************************************************************************
 */
package com.talent.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午11:35:17
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class AioClient<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(AioClient.class);

	private ClientGroupContext<Ext, P, R> clientGroupContext;
	private AsynchronousChannelGroup channelGroup;

	/**
	 * @return the channelGroup
	 */
	public AsynchronousChannelGroup getChannelGroup()
	{
		return channelGroup;
	}

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
	public AioClient(ClientGroupContext<Ext, P, R> clientGroupContext) throws IOException
	{
		super();
		this.clientGroupContext = clientGroupContext;
		ExecutorService groupExecutor = clientGroupContext.getGroupExecutor();
		this.channelGroup = AsynchronousChannelGroup.withThreadPool(groupExecutor);
	}

	public ClientChannelContext<Ext, P, R> connect() throws Exception
	{
		String ip = clientGroupContext.getIp();
		int port = clientGroupContext.getPort();

		AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open(channelGroup);
		asynchronousSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		Future<Void> future = asynchronousSocketChannel.connect(new InetSocketAddress(ip, port));
		try
		{
			future.get(5, TimeUnit.SECONDS);
			log.info("connected to {}:{}", ip, port);
			ClientChannelContext<Ext, P, R> channelContext = new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
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
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:35:17
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<Ext, P, R> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<Ext, P, R> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}
}
