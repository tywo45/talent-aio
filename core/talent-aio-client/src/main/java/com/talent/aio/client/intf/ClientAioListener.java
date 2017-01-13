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

import com.talent.aio.common.ChannelContext;
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
public interface ClientAioListener <Ext, P extends Packet, R> extends AioListener<Ext, P, R> 
{

	/**
	 * 重连后触发的方法
	 * @param newChannelContext
	 * @param initChannelContext
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午3:04:17
	 *
	 */
	boolean onAfterReconnected(ChannelContext<Ext, P, R> newChannelContext, ChannelContext<Ext, P, R> initChannelContext);
}
