/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月17日 下午5:59:24
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.AioClientHandler;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.ClientGroupStat;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.Const.ChatType;
import com.talent.aio.examples.im.common.ImAioListener;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.AuthReqBody;
import com.talent.aio.examples.im.common.bs.ChatReqBody;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.utils.Md5;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月17日 下午5:59:24
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 *
 */
public class ImClientStarter
{
	private static Logger log = LoggerFactory.getLogger(ImClientStarter.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImClientStarter()
	{

	}

	static String ip = "127.0.0.1"; //服务器的IP地址

	static int clientCount = 16000; //与服务器建立多少个TCP长连接
	
	/**
	 * 每秒向组发送多少条消息(如果是测试服务器可以建立多少个TCP长连接，此值设为0，因为客户端会定时发心跳)
	 * 当此值设为10，clientCount设为10000时，本客户端每10秒就会发送一条群消息，但是服务器收到这10条消息后会分发10*10000条消息到本客户端。如果有很多个客户端，消息量还要成倍增加
	 */
	static int sendCountPerSecond = 0; //

	static int port = 9321; //服务器的PORT

	static AtomicLong SEQ = new AtomicLong();

	public static String groupid = "89889_1"; //消息群组id

	static AioClient<Object, ImPacket, Object> aioClient;

	static ClientGroupContext<Object, ImPacket, Object> clientGroupContext = null;

	static AioClientHandler<Object, ImPacket, Object> aioClientHandler = null;

	static AioListener<Object, ImPacket, Object> aioListener = null;

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		aioClientHandler = new ImAioClientHandler();
		aioListener = new ImAioListener();
		clientGroupContext = new ClientGroupContext<>(ip, port, aioClientHandler, aioListener);
		aioClient = new AioClient<>(clientGroupContext);

		for (int i = 0; i < clientCount; i++)
		{
			ClientChannelContext<Object, ImPacket, Object> clientChannelContext = aioClient.connect();
			String did = "did-" + i;
			String token = "token-" + i;
			String info = "info-" + i;
			Long seq = SEQ.incrementAndGet();
			ImPacket packet = createAuthPacket(did, token, info, seq);
			Aio.send(clientChannelContext, packet);
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(10000);

						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> objWithReadWriteLock = clientGroupContext.getGroups().clients(groupid);
						if (objWithReadWriteLock != null)
						{
							ReadLock readLock = objWithReadWriteLock.getLock().readLock();
							try
							{
								readLock.lock();
								Set<ChannelContext<Object, ImPacket, Object>> set = objWithReadWriteLock.getObj();
								int i = 0;
								//log.error("send msg to group {}", groupid);
								label_2: for (ChannelContext<Object, ImPacket, Object> entry : set)
								{
									if (i >= sendCountPerSecond)
									{
										break label_2;
									}
									String msg = "hello-" + i++;
									ChatReqBody chatReqBody = new ChatReqBody(ChatType.pub, msg, groupid, null, null);
									ImPacket imReqPacket = new ImPacket();
									imReqPacket.setCommand(Command.CHAT_REQ);
									imReqPacket.setBody(Json.toJson(chatReqBody).getBytes(ImPacket.CHARSET));
									Aio.send(entry, imReqPacket);//.sendToGroup(clientGroupContext, groupid, imReqPacket);

								}
							} catch (Throwable e)
							{
								log.error("", e);
							} finally
							{
								readLock.unlock();
							}
						}
						String id = clientGroupContext.getId();
						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> connections = clientGroupContext.getConnections().getSet();
						Set<ChannelContext<Object, ImPacket, Object>> set = connections.getObj();
						ClientGroupStat clientGroupStat = clientGroupContext.getClientGroupStat();
						log.error("[{}]:[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", SystemTimer.currentTimeMillis(), id, set.size(),
								clientGroupStat.getClosed().get(), clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(),
								clientGroupStat.getHandledPacket().get(), clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());

						try
						{
							log.error("[{}]: command stat:{}", SystemTimer.currentTimeMillis(), Json.toJson(CommandStat.commandAndCount));
						} catch (Exception e1)
						{
							// may be ConcurrentModificationException,  skip it
						}

					} catch (Throwable e)
					{
						log.error("", e);
					}
				}
			}
		}, "t-send-chatmsg-").start();

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
			log.error(e.toString(), e);
			throw new RuntimeException(e);
		}
		authReqBody.setSign(sign);

		imReqPacket.setBody(Json.toJson(authReqBody).getBytes(ImPacket.CHARSET));
		return imReqPacket;
	}

}
