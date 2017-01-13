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
package com.talent.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AioServer<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(AioServer.class);

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

	private ServerGroupContext<Ext, P, R> serverGroupContext;

	private AsynchronousServerSocketChannel serverSocketChannel;

	/**
	 * 
	 * @param serverGroupContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月2日 下午5:53:06
	 *
	 */
	public AioServer(ServerGroupContext<Ext, P, R> serverGroupContext)
	{
		super();
		this.serverGroupContext = serverGroupContext;
	}

	/**
	 * @return the serverGroupContext
	 */
	public ServerGroupContext<Ext, P, R> getServerGroupContext()
	{
		return serverGroupContext;
	}

	/**
	 * @return the serverSocketChannel
	 */
	public AsynchronousServerSocketChannel getServerSocketChannel()
	{
		return serverSocketChannel;
	}

	/**
	 * @param serverGroupContext the serverGroupContext to set
	 */
	public void setServerGroupContext(ServerGroupContext<Ext, P, R> serverGroupContext)
	{
		this.serverGroupContext = serverGroupContext;
	}

	public void start() throws IOException
	{
		String ip = serverGroupContext.getIp();
		int port = serverGroupContext.getPort();
		ExecutorService groupExecutor = serverGroupContext.getGroupExecutor();

		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(groupExecutor);
		serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);

		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 64 * 1024);

		InetSocketAddress listenAddress = null;

		if (StringUtils.isBlank(ip))
		{
			listenAddress = new InetSocketAddress(port);
		} else
		{
			listenAddress = new InetSocketAddress(ip, port);
		}

		serverSocketChannel.bind(listenAddress, 0);

		AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler = serverGroupContext.getAcceptCompletionHandler();
		serverSocketChannel.accept(this, acceptCompletionHandler);

		String ipstr = StringUtils.isNotBlank(ip) ? ip : "0.0.0.0";
		System.out.println("start listening on " + ipstr + ":" + port);
	}

}
