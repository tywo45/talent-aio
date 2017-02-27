package com.talent.aio.examples.im.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;


public class CloseReqHandler implements ImAioHandlerIntf
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(CloseReqHandler.class);

	@Override
	public Object handler(ImPacket packet, ChannelContext<ImSessionContext, ImPacket, Object> channelContext) throws Exception
	{
		Aio.remove(channelContext, "收到关闭请求");
		return null;
	}
}
