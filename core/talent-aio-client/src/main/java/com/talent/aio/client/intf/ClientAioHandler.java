/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月6日 下午1:43:46
 *
 * **************************************************************************
 */
package com.talent.aio.client.intf;

import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月6日 下午1:43:46
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月6日 | tanyaowu | 新建类
 *
 */
public interface ClientAioHandler <Ext, P extends Packet, R> extends AioHandler<Ext, P, R>
{
	/**
	 * 创建心跳包
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午1:45:03
	 *
	 */
	P heartbeatPacket();
}
