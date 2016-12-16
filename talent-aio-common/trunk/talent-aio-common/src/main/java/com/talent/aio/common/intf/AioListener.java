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
public interface AioListener <Ext, P extends Packet, R>
{
	/**
	 * 消息包发送出去后
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:14:03
	 *
	 */
	void onAfterSent(ChannelContext<Ext, P, R> channelContext, P packet, int packetSize);
	
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
}
