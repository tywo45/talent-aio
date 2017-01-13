/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月6日 下午12:17:30
 *
 * **************************************************************************
 */
package com.talent.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.Node;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月6日 下午12:17:30
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月6日 | tanyaowu | 新建类
 *
 */
public class ClientChannelContext<Ext, P extends Packet, R> extends ChannelContext<Ext, P, R>
{
	
	
	
	private String bindIp;
	
	private Integer bindPort;

	/**
	 * @param groupContext
	 * @param asynchronousSocketChannel
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午12:17:59
	 * 
	 */
	public ClientChannelContext(GroupContext<Ext, P, R> groupContext, AsynchronousSocketChannel asynchronousSocketChannel)
	{
		super(groupContext, asynchronousSocketChannel);
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午12:17:30
	 * 
	 */
	public static void main(String[] args)
	{}

	/** 
	 * @see com.talent.aio.common.ChannelContext#getClientNode(java.nio.channels.AsynchronousSocketChannel)
	 * 
	 * @param asynchronousSocketChannel
	 * @return
	 * @throws IOException 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午12:18:08
	 * 
	 */
	@Override
	public Node getClientNode(AsynchronousSocketChannel asynchronousSocketChannel) throws IOException
	{
		InetSocketAddress inetSocketAddress = (InetSocketAddress) asynchronousSocketChannel.getLocalAddress();
		Node clientNode = new Node(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
		return clientNode;
	}

	/**
	 * @return the bindIp
	 */
	public String getBindIp()
	{
		return bindIp;
	}

	/**
	 * @param bindIp the bindIp to set
	 */
	public void setBindIp(String bindIp)
	{
		this.bindIp = bindIp;
	}

	/**
	 * @return the bindPort
	 */
	public Integer getBindPort()
	{
		return bindPort;
	}

	/**
	 * @param bindPort the bindPort to set
	 */
	public void setBindPort(Integer bindPort)
	{
		this.bindPort = bindPort;
	}

}
