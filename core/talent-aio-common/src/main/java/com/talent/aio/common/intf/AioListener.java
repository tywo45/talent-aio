/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月8日 上午11:25:42
 *
 * **************************************************************************
 */
package com.talent.aio.common.intf;

import com.talent.aio.common.ChannelContext;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月8日 上午11:25:42
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月8日 | tanyaowu | 新建类
 *
 */
public interface AioListener<SessionContext, P extends Packet, R>
{
	/**
	 * 建链后触发本方法，注：建链不一定成功，需要关注参数isConnected
	 * @param channelContext
	 * @param isConnected 是否连接成功,true:表示连接成功，false:表示连接失败
	 * @param isReconnect 是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午3:28:24
	 *
	 */
	void onAfterConnected(ChannelContext<SessionContext, P, R> channelContext, boolean isConnected, boolean isReconnect) throws Exception;

	/**
	 * 消息包发送之后触发本方法
	 * @param channelContext
	 * @param packet
	 * @param isSentSuccess true:发送成功，false:发送失败
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月4日 下午9:18:38
	 *
	 */
	void onAfterSent(ChannelContext<SessionContext, P, R> channelContext, P packet, boolean isSentSuccess) throws Exception;

	/**
	 * 解码成功后触发本方法
	 * @param channelContext
	 * @param packet
	 * @param sentSize
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月15日 上午10:22:49
	 *
	 */
	void onAfterReceived(ChannelContext<SessionContext, P, R> channelContext, P packet, int packetSize) throws Exception;

//	/**
//	 * 连接关闭前触发本方法
//	 *
//	 * @param channelContext the channelcontext
//	 * @param throwable the throwable 有可能为空
//	 * @param remark the remark 有可能为空
//	 * @param isRemove
//	 * @author: tanyaowu
//	 * @创建时间:　2016年11月17日 上午9:17:55
//	 */
//	void onBeforeClose(ChannelContext<SessionContext, P, R> channelContext, Throwable throwable, String remark, boolean isRemove);
	
	/**
	 * 连接关闭前后触发本方法
	 *
	 * @param channelContext the channelcontext
	 * @param throwable the throwable 有可能为空
	 * @param remark the remark 有可能为空
	 * @param isRemove 是否是删除
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午9:17:55
	 */
	void onAfterClose(ChannelContext<SessionContext, P, R> channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception;
}
