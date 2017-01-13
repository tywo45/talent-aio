/**
 * 
 */
package com.talent.aio.examples.im.client.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.client.ui.JFrameMain;
import com.talent.aio.examples.im.common.ImPacket;

/**
 * 
 * @filename:	 com.talent.im.client.handler.bs.ChatRespHandler
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
public class ChatRespHandler implements ImBsAioHandlerIntf
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ChatRespHandler.class);

	/**
	 * 
	 */
	public ChatRespHandler()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	public static final java.util.concurrent.atomic.AtomicInteger count = new AtomicInteger();

	//	@Override
	//	public Map<String, Object> onReceived(ImReqPacket packet, ChannelContext<ImClientChannelContextExt> channelContext) throws Exception
	//	{
	//		String bodyStr = null;
	//		if (packet.getBody() != null)
	//		{
	//			bodyStr = new String(packet.getBody(), Const.CHARSET_UTF8);
	//		}
	//		
	//		ChatRespBody body = Json.toBean(bodyStr, ChatRespBody.class);
	//		int c = count.incrementAndGet();
	//		String xx = null;
	//		long time = SystemTimer.currentTimeMillis();
	//
	//		if (c == 1 || c == 0 || c % 10000 == 0)
	//		{
	//			if (StringUtils.isBlank(body.getToNick()))
	//			{
	//				xx = "[" + c + "]" + "[" + time + "]";// + "[" + channelContext.getId() + "]" + body.getFromNick() + " 说 : " + body.getBody();
	//			} else
	//			{
	//				xx = "[" + c + "]" + "[" + time + "]";// + "[" + channelContext.getId() + "]" + body.getFromNick() + " 对 " + body.getToNick() + " 说 : " + body.getBody();
	//			}
	//
	//			JFrameMain.getInstance().getMsgTextArea().append(xx + System.lineSeparator());
	//		}
	//
	//		return null;
	//	}

	/** 
	 * @see com.talent.aio.examples.im.client.handler.ImBsAioHandlerIntf#handler(com.talent.aio.examples.im.common.ImPacket, com.talent.aio.common.ChannelContext)
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 * @throws Exception
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午2:24:29
	 * 
	 */
	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		//		String bodyStr = null;
		//		if (packet.getBody() != null)
		//		{
		//			bodyStr = new String(packet.getBody(), ImPacket.CHARSET);
		//		}

		//		ChatRespBody body = Json.toBean(bodyStr, ChatRespBody.class);
		int packetCount = count.incrementAndGet();

		String xx = null;
		long time = SystemTimer.currentTimeMillis();

		JFrameMain frameMain = JFrameMain.getInstance();
		if (packetCount == 1 || packetCount == 0 || packetCount % 100000 == 0)
		{
			long sendStartTime = frameMain.getSendStartTime();
			long in = time - sendStartTime;
			if (in <= 0)
			{
				in = 1;
			}

			long initReceivedBytes = frameMain.getStartRecievedBytes();
			long initSentBytes = frameMain.getStartSentBytes();

			long nowReceivedBytes = channelContext.getGroupContext().getGroupStat().getReceivedBytes().get();
			long nowSentBytes = channelContext.getGroupContext().getGroupStat().getSentBytes().get();

			long receivedBytes = nowReceivedBytes - initReceivedBytes;
			long sentBytes = nowSentBytes - initSentBytes;

			double perPacket = Math.ceil(((double) packetCount / (double) in) * (double) 1000);
			double perReceivedBytes = Math.ceil(((double) receivedBytes / (double) in));
			double perSentBytes = Math.ceil(((double) sentBytes / (double) in));

			xx = "<<--------------------\r\n已接收" + packetCount + "条消息，共" + (receivedBytes / (1024 * 1024)) + "M，一共耗时" + in + "毫秒\r\n平均每秒接收" + perPacket + "条消息，数据大小为"
					+ perReceivedBytes + "KB(" + Math.ceil((perReceivedBytes / 1024)) + "M)，每条消息大小平均为" + Math.ceil((perReceivedBytes * 1024 / perPacket)) + "B\r\n平均每秒发送"
					+ perSentBytes + "KB数据\r\n-------------------->>";

			//			if (StringUtils.isBlank(body.getToNick()))
			//			{
			//				xx = "[" + c + "]" + "[" + time + "]";// + "[" + channelContext.getId() + "]" + body.getFromNick() + " 说 : " + body.getBody();
			//			} else
			//			{
			//				xx = "[" + c + "]" + "[" + time + "]";// + "[" + channelContext.getId() + "]" + body.getFromNick() + " 对 " + body.getToNick() + " 说 : " + body.getBody();
			//			}

			frameMain.getMsgTextArea().append(xx + System.lineSeparator());
		}
		return null;
	}
}
