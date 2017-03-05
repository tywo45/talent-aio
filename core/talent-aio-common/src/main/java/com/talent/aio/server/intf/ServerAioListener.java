/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月20日 上午10:14:41
 *
 * **************************************************************************
 */
package com.talent.aio.server.intf;

import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月20日 上午10:14:41
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月20日 | tanyaowu | 新建类
 *
 */
public interface ServerAioListener <SessionContext, P extends Packet, R> extends AioListener<SessionContext, P, R> 
{

	/**
	 * 建立连接后触发的方法
	 * @param asynchronousSocketChannel
	 * @param aioServer
	 * @return false: 表示拒绝这个连接, true: 表示接受这个连接
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月20日 上午10:10:56
	 *
	 */
//	void onAfterAccepted(AsynchronousSocketChannel asynchronousSocketChannel, AioServer<SessionContext, P, R> aioServer);
}
