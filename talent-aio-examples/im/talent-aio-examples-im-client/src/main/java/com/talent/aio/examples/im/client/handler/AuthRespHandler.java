/**
 * 
 */
package com.talent.aio.examples.im.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.Const;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.JoinReqBody;
import com.talent.aio.examples.im.common.json.Json;

/**
 * 
 * @filename:	 com.talent.im.client.handler.bs.AuthorizationRespHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年7月1日 下午1:31:50
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年7月1日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */

public class AuthRespHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(AuthRespHandler.class);

	/**
	 * 
	 */
	public AuthRespHandler()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see com.talent.aio.examples.im.client.handler.ImBsAioHandlerIntf#handler(com.talent.aio.examples.im.common.ImPacket, com.talent.aio.common.ChannelContext)
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 * @throws Exception
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午2:23:24
	 * 
	 */
	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		JoinReqBody joinReqBody = new JoinReqBody(com.talent.aio.examples.im.client.ImClientStarter.groupid);
		ImPacket respPacket = new ImPacket();
		respPacket.setCommand(Command.JOIN_GROUP_REQ);
		respPacket.setBody(Json.toJson(joinReqBody).getBytes(ImPacket.CHARSET));
		Aio.send(channelContext, respPacket);
		return null;
	}
}
