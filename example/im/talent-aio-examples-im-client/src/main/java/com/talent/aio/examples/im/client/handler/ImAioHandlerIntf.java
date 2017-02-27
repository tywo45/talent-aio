package com.talent.aio.examples.im.client.handler;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;

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
public interface ImAioHandlerIntf
{
	/**
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月22日 下午2:02:30
	 *
	 */
	public Object handler(ImPacket packet, ChannelContext<ImSessionContext, ImPacket, Object> channelContext)  throws Exception;
}
