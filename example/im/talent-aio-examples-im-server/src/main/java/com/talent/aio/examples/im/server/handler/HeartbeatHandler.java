package com.talent.aio.examples.im.server.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.ImPacket;


public class HeartbeatHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);

	@PostConstruct
	public void init()
	{
	}

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
			log.info("{}收到心跳包", channelContext.toString());
		}
		
		return null;
	}

	/**
	 * 发送消息到手机客户端
	 * @param datas
	 */
	//	public void sendMsgToMobile(Map<String, Object> datas, Integer staruserid)
	//	{
	////		ChatRequestBody chatRequestBody = ChatRequestBody.wrapFromMap(datas);
	//
	//		if (staruserid == null || staruserid == -1)
	//		{
	//			log.warn("staruserid is {}", staruserid);
	//			return;
	//		}
	//
	//		Map<Integer, Collection<ChannelContext<ImServerChannelContextExt>>> map = TcpJoinHandler.staruseridAndChannelContext;
	//		Collection<ChannelContext<ImServerChannelContextExt>> set = map.get(staruserid);
	//
	//		if (set == null || set.size() == 0)
	//		{
	//			log.info("房间没有人在用手机聊天");
	//			return;
	//		}
	//
	//		ChatRequestPacket packet = new ChatRequestPacket();
	//		packet.setCommand(CommandExt.CHAT_RESP);
	//		packet.setBody(Json.toJson(datas));
	//		ClientApi.sendServerPacketToChannelContexts(packet, set);
	//		//
	//		//		ByteOnlyPacket byteOnlyPacket = new ByteOnlyPacket(packet.toBytes());
	//		//
	//		//		for (ChannelContext channelContext : set)
	//		//		{
	//		//			try
	//		//			{
	//		//				Nio.getInstance().asySend(byteOnlyPacket, channelContext);
	//		//			} catch (Exception e)
	//		//			{
	//		//				log.error(channelContext.getId() + e.getMessage());
	//		//			}
	//		//		}
	//
	//	}
}
