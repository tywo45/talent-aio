/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月1日 下午1:40:12
 *
 * **************************************************************************
 */
package com.talent.aio.common.maintain;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月1日 下午1:40:12
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月1日 | tanyaowu | 新建类
 *
 */
public class MaintainUtils
{
	private static Logger log = LoggerFactory.getLogger(MaintainUtils.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月1日 下午1:40:12
	 * 
	 */
	public MaintainUtils()
	{

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月1日 下午1:40:12
	 * 
	 */
	public static void main(String[] args)
	{

	}

	public static <SessionContext, P extends Packet, R> void addToMaintain(ChannelContext<SessionContext, P, R> channelContext)
	{
		GroupContext<SessionContext, P, R> groupContext = channelContext.getGroupContext();
		groupContext.getConnections().add(channelContext);
	}

	/**
	 * 彻底删除，不再维护
	 * @param channelContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月2日 上午9:27:00
	 *
	 */
	public static <SessionContext, P extends Packet, R> void removeFromMaintain(ChannelContext<SessionContext, P, R> channelContext)
	{
		GroupContext<SessionContext, P, R> groupContext = channelContext.getGroupContext();

		try
		{
			groupContext.getConnections().remove(channelContext);
		} catch (Exception e1)
		{
			log.error(e1.toString(), e1);
		}
		try
		{
			groupContext.getConnecteds().remove(channelContext);
		} catch (Exception e1)
		{
			log.error(e1.toString(), e1);
		}
		try
		{
			groupContext.getCloseds().remove(channelContext);
		} catch (Exception e1)
		{
			log.error(e1.toString(), e1);
		}
		
		if (StringUtils.isNotBlank(channelContext.getUserid()))
		{
			try
			{
				Aio.unbindUser(channelContext);
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
		}

		try
		{
			Aio.unbindGroup(channelContext);
		} catch (Throwable e)
		{
			log.error(e.toString(), e);
		}
	}

}
