package com.talent.aio.examples.im.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;
import com.talent.aio.examples.im.common.packets.Command;
import com.talent.aio.examples.im.common.packets.JoinGroupResult;
import com.talent.aio.examples.im.common.packets.JoinReqBody;
import com.talent.aio.examples.im.common.packets.JoinRespBody;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月8日 下午3:30:31
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月8日 | tanyaowu | 新建类
 *
 */
public class JoinReqHandler implements ImAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(JoinReqHandler.class);

	@Override
	public Object handler(ImPacket packet, ChannelContext<ImSessionContext, ImPacket, Object> channelContext) throws Exception
	{
		if (packet.getBody() == null)
		{
			throw new Exception("body is null");
		}

		JoinReqBody reqBody = JoinReqBody.parseFrom(packet.getBody());
		
		String group = reqBody.getGroup();
		if (StringUtils.isBlank(group))
		{
			log.error("group is null,{}", channelContext);
			Aio.close(channelContext, "group is null when join group");
			return null;
		}

		Aio.bindGroup(channelContext, group);

		
		JoinGroupResult joinGroupResult = JoinGroupResult.JOIN_GROUP_RESULT_OK;
		JoinRespBody joinRespBody = JoinRespBody.newBuilder().setTime(SystemTimer.currentTimeMillis()).setResult(joinGroupResult).setGroup(group).build();
		byte[] body = joinRespBody.toByteArray();
		
		ImPacket respPacket = new ImPacket();
		respPacket.setCommand(Command.COMMAND_JOIN_GROUP_RESP);
		respPacket.setBody(body);
		
		Aio.send(channelContext, respPacket);
	
		return null;
	}
}
