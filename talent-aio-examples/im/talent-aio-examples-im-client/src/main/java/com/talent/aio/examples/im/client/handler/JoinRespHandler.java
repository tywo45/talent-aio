/**
 * 
 */
package com.talent.aio.examples.im.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.JoinRespBody;
import com.talent.aio.examples.im.common.json.Json;

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
public class JoinRespHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(JoinRespHandler.class);

	/**
	 * 
	 */
	public JoinRespHandler()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

//	@Override
//	public Map<String, Object> onReceived(ImReqPacket packet, ChannelContext<ImClientChannelContextExt> channelContext) throws Exception
//	{
//
//		ImClientChannelContextExt ext = channelContext.getExt();
//		String bodyStr = null;
//		if (packet.getBody() != null)
//		{
//			bodyStr = new String(packet.getBody(), Const.CHARSET_UTF8);
//		}
//		JoinRespBody body = Json.toBean(bodyStr, JoinRespBody.class);
//		if (Objects.equals(JoinGroupResultVo.Code.OK, body.getResult().getCode()))
//		{
//			String group = body.getGroup();
//			//			log.info("join group {}", group);
//			String xx = channelContext.getId() + "(" + ext.getLoginname() + ")" + "进入组:" + group;
//			JFrameMain.getInstance().getMsgTextArea().append(xx + System.lineSeparator());
//			//顺利进入组
//		} else
//		{
//			//被拒绝
//			//			log.error("refused to join in group {}", body.getGroup());
//			String xx = channelContext.getId() + "(" + ext.getLoginname() + ")" + "被拒绝进入组" + body.getGroup();
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
	 * @重写时间: 2016年12月6日 下午2:25:44
	 * 
	 */
	@Override
	public Object handler(ImPacket packet, com.talent.aio.common.ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{

		String bodyStr = null;
		if (packet.getBody() != null)
		{
			bodyStr = new String(packet.getBody(), ImPacket.CHARSET);
		}
		JoinRespBody body = Json.toBean(bodyStr, JoinRespBody.class);
		channelContext.getGroupContext().getGroups().put(body.getGroup(), channelContext);
		return null;
	}
}
