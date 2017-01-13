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
public interface AioListener<Ext, P extends Packet, R>
{
	/**
	 * 连接建立后触发的方法
	 * @param channelContext
	 * @return true: 表示这个连接可以连接，false: 表示这个连接需要立即关闭
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午3:28:24
	 *
	 */
	boolean onAfterConnected(ChannelContext<Ext, P, R> channelContext);

	/**
	 * 消息包发送出去后
	 * @param channelContext
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:14:03
	 *
	 */
	void onBeforeSent(ChannelContext<Ext, P, R> channelContext, P packet);

	/**
	 * 解码成功后
	 * @param channelContext
	 * @param packet
	 * @param sentSize
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月15日 上午10:22:49
	 *
	 */
	void onAfterDecoded(ChannelContext<Ext, P, R> channelContext, P packet, int packetSize);

	/**
	 * 连接关闭前触发本方法，业务层作一些释放业务资源等操作，记录日志等.
	 *
	 * @param channelContext the channelcontext
	 * @param throwable the throwable 有可能为空
	 * @param remark the remark 有可能为空
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午9:17:55
	 */
	void onBeforeClose(ChannelContext<Ext, P, R> channelContext, Throwable throwable, String remark);
}
