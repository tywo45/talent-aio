/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年1月13日 下午3:27:19
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月13日 下午3:27:19
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月13日 | tanyaowu | 新建类
 *
 */
public interface ChannelContextFilter<SessionContext, P extends Packet, R>
{

	/**
	 * 
	 * @param channelContext
	 * @return false: 排除此channelContext, true: 不排除
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月13日 下午3:28:54
	 *
	 */
	public boolean filter(ChannelContext<SessionContext, P, R> channelContext);

}
