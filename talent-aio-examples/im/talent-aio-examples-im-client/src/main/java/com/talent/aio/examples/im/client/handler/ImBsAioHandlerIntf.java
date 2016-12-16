/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 下午1:06:56
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client.handler;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.ImPacket;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 下午1:06:56
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public interface ImBsAioHandlerIntf
{
	/**
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 下午1:08:45
	 *
	 */
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext)  throws Exception;
}
