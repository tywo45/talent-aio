package com.talent.aio.client;

import java.nio.channels.AsynchronousSocketChannel;

import com.talent.aio.common.Node;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月28日 下午6:38:58
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月28日 | tanyaowu | 新建类
 *
 */
public class ConnectionCompletionVo<SessionContext, P extends Packet, R>
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月28日 下午6:38:58
	 * 
	 */
	public ConnectionCompletionVo()
	{
		
	}
	
	private ClientChannelContext<SessionContext, P, R> channelContext = null;
	
	private AioClient<SessionContext, P, R> aioClient = null;
	
	private boolean isReconnect = false;
	
	private AsynchronousSocketChannel asynchronousSocketChannel;
	
	private Node serverNode;
	
	private String bindIp;
	
	private Integer bindPort;

	

	/**
	 * @param channelContext
	 * @param aioClient
	 * @param isReconnect
	 * @param asynchronousSocketChannel
	 * @param serverNode
	 * @param bindIp
	 * @param bindPort
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月28日 下午6:51:30
	 * 
	 */
	public ConnectionCompletionVo(ClientChannelContext<SessionContext, P, R> channelContext, AioClient<SessionContext, P, R> aioClient, boolean isReconnect,
			AsynchronousSocketChannel asynchronousSocketChannel, Node serverNode, String bindIp, Integer bindPort)
	{
		super();
		this.channelContext = channelContext;
		this.aioClient = aioClient;
		this.isReconnect = isReconnect;
		this.asynchronousSocketChannel = asynchronousSocketChannel;
		this.serverNode = serverNode;
		this.bindIp = bindIp;
		this.bindPort = bindPort;
	}

	/**
	 * @return the serverNode
	 */
	public Node getServerNode()
	{
		return serverNode;
	}

	/**
	 * @param serverNode the serverNode to set
	 */
	public void setServerNode(Node serverNode)
	{
		this.serverNode = serverNode;
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

	/**
	 * @return the channelContext
	 */
	public ClientChannelContext<SessionContext, P, R> getChannelContext()
	{
		return channelContext;
	}

	/**
	 * @param channelContext the channelContext to set
	 */
	public void setChannelContext(ClientChannelContext<SessionContext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	/**
	 * @return the isReconnect
	 */
	public boolean isReconnect()
	{
		return isReconnect;
	}

	/**
	 * @param isReconnect the isReconnect to set
	 */
	public void setReconnect(boolean isReconnect)
	{
		this.isReconnect = isReconnect;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月28日 下午6:38:58
	 * 
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * @return the asynchronousSocketChannel
	 */
	public AsynchronousSocketChannel getAsynchronousSocketChannel()
	{
		return asynchronousSocketChannel;
	}

	/**
	 * @param asynchronousSocketChannel the asynchronousSocketChannel to set
	 */
	public void setAsynchronousSocketChannel(AsynchronousSocketChannel asynchronousSocketChannel)
	{
		this.asynchronousSocketChannel = asynchronousSocketChannel;
	}

	/**
	 * @return the aioClient
	 */
	public AioClient<SessionContext, P, R> getAioClient()
	{
		return aioClient;
	}

	/**
	 * @param aioClient the aioClient to set
	 */
	public void setAioClient(AioClient<SessionContext, P, R> aioClient)
	{
		this.aioClient = aioClient;
	}

}
