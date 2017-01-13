package com.talent.aio.examples.im.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.JoinReqBody;
import com.talent.aio.examples.im.common.bs.JoinRespBody;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.vo.JoinGroupResultVo;

/**
 * 手机端进入房间
 * 
 * @filename:	 com.talent.im.server.handler.bs.TcpJoinHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年4月3日 下午5:19:16
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年4月3日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class JoinHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(JoinHandler.class);

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{

		String bodyStr = null;
		if (packet.getBody() != null)
		{
			bodyStr = new String(packet.getBody(), ImPacket.CHARSET);
		}

		if (log.isInfoEnabled())
		{
			log.info("{}收到join包{}", channelContext.toString(), bodyStr);
		}
		
		JoinReqBody joinReqBody = Json.toBean(bodyStr, JoinReqBody.class);
		String group = joinReqBody.getGroup();
		if (StringUtils.isBlank(group))
		{
			log.error("group is null,{}", channelContext);
			Aio.close(channelContext, "group is null when join group");
			return null;
		}

//		channelContext.getGroupContext().getGroups().bind(groupid, channelContext);
		com.talent.aio.common.Aio.bindGroup(channelContext, group);
		
		JoinGroupResultVo joinGroupResultVo = new JoinGroupResultVo();
		joinGroupResultVo.setCode(com.talent.aio.examples.im.common.vo.JoinGroupResultVo.Code.OK);
		JoinRespBody joinRespBody = new JoinRespBody(group, joinGroupResultVo);
		
		ImPacket respPacket = new ImPacket();
		respPacket.setCommand(Command.JOIN_GROUP_RESP);
		respPacket.setBody(Json.toJson(joinRespBody).getBytes(ImPacket.CHARSET));
		
		Aio.send(channelContext, respPacket);
	
		return null;
	}
}
