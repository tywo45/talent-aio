/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-client
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月16日 下午5:52:06
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.maintain.ClientNodes;
import com.talent.aio.examples.im.client.ui.JFrameMain;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.AuthReqBody;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.utils.Md5;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月16日 下午5:52:06
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月16日 | tanyaowu | 新建类
 *
 */
public class ImClientAioListener implements ClientAioListener<Object, ImPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(ImClientAioListener.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public ImClientAioListener()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/** 
	 * @see com.talent.aio.examples.im.common.ImAioListener#onBeforeClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月16日 下午5:52:24
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onBeforeClose(ChannelContext<Object, ImPacket, Object> channelContext, Throwable throwable, String remark)
	{
		log.info("即将关闭连接:{}", channelContext);

		JFrameMain jFrameMain = JFrameMain.getInstance();
		synchronized (jFrameMain)
		{
			try
			{
				jFrameMain.getListModel().removeElement(ClientNodes.getKey(channelContext));
				jFrameMain.getClients().setModel(jFrameMain.getListModel());
			} catch (Exception e)
			{

			}

//			jFrameMain.updateClientCount();
		}

	}

	/** 
	 * @see com.talent.aio.client.intf.ClientAioListener#onAfterReconnected(com.talent.aio.common.ChannelContext)
	 * 
	 * @param channelContext
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午10:17:53
	 * 
	 */
	@Override
	public boolean onAfterReconnected(ChannelContext<Object, ImPacket, Object> newChannelContext, ChannelContext<Object, ImPacket, Object> initChannelContext)
	{
		return true;
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterConnected(com.talent.aio.common.ChannelContext)
	 * 
	 * @param channelContext
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:41:27
	 * 
	 */
	@Override
	public boolean onAfterConnected(ChannelContext<Object, ImPacket, Object> channelContext)
	{
		String did = "did";
		String token = "token";
		String info = "info";
		Long seq = 1L;
		ImPacket respPacket;
		try
		{
			respPacket = createAuthPacket(did, token, info, seq);
			Aio.send(channelContext, respPacket);
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		}
		
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * 构建鉴权包
	 * @return
	 * @throws Exception 
	 */
	public static ImPacket createAuthPacket(String did, String token, String info, Long seq) throws Exception
	{
		ImPacket imReqPacket = new ImPacket();
		imReqPacket.setCommand(Command.AUTH_REQ);

		AuthReqBody authReqBody = new AuthReqBody();
		authReqBody.setDeviceId(did);
		authReqBody.setSeq(seq);
		authReqBody.setDeviceType((byte) 2);
		authReqBody.setDeviceInfo(info);
		authReqBody.setToken(token);

		did = did == null ? "" : did;
		token = token == null ? "" : token;
		info = info == null ? "" : info;
		seq = seq == null ? 0 : seq;
		
		String data = token + did + info + seq + "fdsfeofa";
		String sign = null;
		try
		{
			sign = Md5.getMD5(data);//DesUtils.encrypt(data, ImClientConfig.getInstance().getString("im.auth.private.key", "fdsfeofa"));
		} catch (Exception e)
		{
			log.error(e.getLocalizedMessage(), e);
			throw new RuntimeException(e);
		}
		authReqBody.setSign(sign);

		imReqPacket.setBody(Json.toJson(authReqBody).getBytes(ImPacket.CHARSET));
		return imReqPacket;
	}
	
	

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onBeforeSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:41:27
	 * 
	 */
	@Override
	public void onBeforeSent(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet)
	{
		CommandStat.getCount(packet.getCommand()).sent.incrementAndGet();

	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterDecoded(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:41:27
	 * 
	 */
	@Override
	public void onAfterDecoded(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int packetSize)
	{
		CommandStat.getCount(packet.getCommand()).received.incrementAndGet();

	}

}
