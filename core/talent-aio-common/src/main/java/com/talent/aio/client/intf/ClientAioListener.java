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
package com.talent.aio.client.intf;

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
public interface ClientAioListener <SessionContext, P extends Packet, R> extends AioListener<SessionContext, P, R> 
{

	/**
	 * 重连后触发本方法
	 * @param channelContext
	 * @param isConnected true: 表示重连成功，false: 表示重连失败
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月3日 上午9:58:01
	 *
	 */
//	void onAfterReconnected(ChannelContext<SessionContext, P, R> channelContext, boolean isConnected) throws Exception;
	
//	/**
//	 * 连接失败后触发的方法
//	 * @param channelContext
//	 * @param isReconnect 是否是重连
//	 * @param throwable 有可能是null
//	 * @author: tanyaowu
//	 * @创建时间:　2017年2月2日 下午7:46:45
//	 *
//	 */
//	void onFailConnected(ChannelContext<SessionContext, P, R> channelContext, boolean isReconnect, java.lang.Throwable throwable);
}
