package com.talent.aio.examples.im.client;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.ReconnConf;
import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月17日 下午5:59:24
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 *
 */
public class ImClientStarter
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ImClientStarter.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImClientStarter() throws IOException
	{
		aioClientHandler = new ImClientAioHandler();
		aioListener = new ImClientAioListener();
		clientGroupContext = new ClientGroupContext<>(aioClientHandler, aioListener, reconnConf);
//		clientGroupContext.setReadBufferSize(2048);
//		clientGroupContext.setByteOrder(ByteOrder.BIG_ENDIAN);
		clientGroupContext.setEncodeCareWithChannelContext(true);
		aioClient = new AioClient<>(clientGroupContext);
	}


	private AioClient<ImSessionContext, ImPacket, Object> aioClient;

	private ClientGroupContext<ImSessionContext, ImPacket, Object> clientGroupContext = null;

	private ClientAioHandler<ImSessionContext, ImPacket, Object> aioClientHandler = null;

	private ClientAioListener<ImSessionContext, ImPacket, Object> aioListener = null;
	
	private static ReconnConf<ImSessionContext, ImPacket, Object> reconnConf = new ReconnConf<ImSessionContext, ImPacket, Object>(5000L);

	//--------------

	public static String SERVER_IP = "118.178.88.70"; //服务器的IP地址

	public static int SERVER_PORT = 9321; //服务器的PORT

	public static AtomicLong SEQ = new AtomicLong();

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		com.talent.aio.examples.im.client.ui.JFrameMain.main(args);
	}

	/**
	 * @return the aioClient
	 */
	public AioClient<ImSessionContext, ImPacket, Object> getAioClient()
	{
		return aioClient;
	}

	/**
	 * @param aioClient the aioClient to set
	 */
	public void setAioClient(AioClient<ImSessionContext, ImPacket, Object> aioClient)
	{
		this.aioClient = aioClient;
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<ImSessionContext, ImPacket, Object> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<ImSessionContext, ImPacket, Object> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}

	/**
	 * @return the aioClientHandler
	 */
	public ClientAioHandler<ImSessionContext, ImPacket, Object> getAioClientHandler()
	{
		return aioClientHandler;
	}

	/**
	 * @param aioClientHandler the aioClientHandler to set
	 */
	public void setAioClientHandler(ClientAioHandler<ImSessionContext, ImPacket, Object> aioClientHandler)
	{
		this.aioClientHandler = aioClientHandler;
	}

	/**
	 * @return the aioListener
	 */
	public AioListener<ImSessionContext, ImPacket, Object> getAioListener()
	{
		return aioListener;
	}

	/**
	 * @param aioListener the aioListener to set
	 */
	public void setAioListener(ClientAioListener<ImSessionContext, ImPacket, Object> aioListener)
	{
		this.aioListener = aioListener;
	}

}
