/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月26日 下午9:36:54
 *
 * **************************************************************************
 */
package com.talent.aio.client;

import java.nio.channels.CompletionHandler;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;

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
public class ConnectionCompletionHandler<SessionContext, P extends Packet, R> implements CompletionHandler<Void, ChannelContext<SessionContext, P, R> >
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月26日 下午9:36:55
	 * 
	 */
	public ConnectionCompletionHandler()
	{
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub

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
	public void completed(Void result, ChannelContext<SessionContext, P, R> attachment)
	{
		
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
	public void failed(Throwable exc, ChannelContext<SessionContext, P, R> attachment)
	{
		
	}

}
