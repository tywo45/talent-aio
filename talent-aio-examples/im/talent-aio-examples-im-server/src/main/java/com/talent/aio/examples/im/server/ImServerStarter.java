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
package com.talent.aio.examples.im.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImAioListener;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.server.AioServer;
import com.talent.aio.server.ServerGroupContext;

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
public class ImServerStarter
{
	private static Logger log = LoggerFactory.getLogger(ImServerStarter.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImServerStarter()
	{

	}

	static ServerGroupContext<Object, ImPacket, Object> serverGroupContext = null;

	static AioServer<Object, ImPacket, Object> aioServer = null;

	static AioHandler<Object, ImPacket, Object> aioHandler = null;

	static AioListener<Object, ImPacket, Object> aioListener = null;

	static String ip = null;

	static int port = 9321;

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public static void main(String[] args) throws IOException
	{
		aioHandler = new ImServerAioHandler();
		aioListener = new ImAioListener();
		serverGroupContext = new ServerGroupContext<>(ip, port, aioHandler, aioListener);
		aioServer = new AioServer<>(serverGroupContext);
		aioServer.start();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						try
						{
							log.error("[{}]: command stat:{}", SystemTimer.currentTimeMillis(), Json.toJson(CommandStat.commandAndCount));
						} catch (Exception e1)
						{
							// may be ConcurrentModificationException,  skip it
						}

						try
						{
							ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> objWithReadWriteLock = serverGroupContext.getConnections().getSet();
							ReadLock readLock = objWithReadWriteLock.getLock().readLock();
							try
							{
								readLock.lock();
								Set<ChannelContext<Object, ImPacket, Object>> set = objWithReadWriteLock.getObj();

								for (ChannelContext<Object, ImPacket, Object> entry : set)
								{

									//									ChannelContext<Object, ImPacket, Object> channelContext = entry;
									//									CloseRunnable<Object, ImPacket, Object> closeRunnable = channelContext.getCloseRunnable();
									//									DecodeRunnable<Object, ImPacket, Object> decodeRunnable = channelContext.getDecodeRunnable();
									//									HandlerRunnable<Object, ImPacket, Object> handlerRunnableHighPrior = channelContext.getHandlerRunnableHighPrior();
									//									HandlerRunnable<Object, ImPacket, Object> handlerRunnableNormPrior = channelContext.getHandlerRunnableNormPrior();
									//									SendRunnable<Object, ImPacket, Object> sendRunnableHighPrior = channelContext.getSendRunnableHighPrior();
									//									SendRunnable<Object, ImPacket, Object> sendRunnableNormPrior = channelContext.getSendRunnableNormPrior();

									//									StringBuilder sb = new StringBuilder();
									//									if (decodeRunnable.getMsgQueue().size() > 0)
									//									{
									//										sb.append("decodeRunnable queue:").append(decodeRunnable.getMsgQueue().size()).append(",isrunning:")
									//												.append(decodeRunnable.runningLock().getObj()).append(",isneedrun:").append(decodeRunnable.isNeededExecute()).append(" | ");
									//										
									//										if (decodeRunnable.isNeededExecute() && !decodeRunnable.runningLock().getObj())
									//										{
									////											decodeRunnable.getExecutor().execute(decodeRunnable);
									//										}
									//									}
									//									if (handlerRunnableHighPrior.getMsgQueue().size() > 0)
									//									{
									//										sb.append("handlerRunnableHighPrior queue:").append(handlerRunnableHighPrior.getMsgQueue().size()).append(",isrunning:")
									//												.append(handlerRunnableHighPrior.runningLock().getObj()).append(",isneedrun:").append(handlerRunnableHighPrior.isNeededExecute()).append(" | ");
									//										if (handlerRunnableHighPrior.isNeededExecute() && !handlerRunnableHighPrior.runningLock().getObj())
									//										{
									////											handlerRunnableHighPrior.getExecutor().execute(handlerRunnableHighPrior);
									//										}
									//									}
									//									if (handlerRunnableNormPrior.getMsgQueue().size() > 0)
									//									{
									//										sb.append("handlerRunnableNormPrior queue:").append(handlerRunnableNormPrior.getMsgQueue().size()).append(",isrunning:")
									//												.append(handlerRunnableNormPrior.runningLock().getObj()).append(",isneedrun:").append(handlerRunnableNormPrior.isNeededExecute()).append(" | ");
									//										if (handlerRunnableNormPrior.isNeededExecute() && !handlerRunnableNormPrior.runningLock().getObj())
									//										{
									////											handlerRunnableNormPrior.getExecutor().execute(handlerRunnableNormPrior);
									//										}
									//									}
									//									if (sendRunnableHighPrior.getMsgQueue().size() > 0)
									//									{
									//										sb.append("sendRunnableHighPrior queue:").append(sendRunnableHighPrior.getMsgQueue().size()).append(",isrunning:")
									//												.append(sendRunnableHighPrior.runningLock().getObj()).append(",isneedrun:").append(sendRunnableHighPrior.isNeededExecute()).append(" | ");
									//										if (sendRunnableHighPrior.isNeededExecute() && !sendRunnableHighPrior.runningLock().getObj())
									//										{
									////											sendRunnableHighPrior.getExecutor().execute(sendRunnableHighPrior);
									//										}
									//									}
									//									if (sendRunnableNormPrior.getMsgQueue().size() > 0)
									//									{
									//										sb.append("sendRunnableNormPrior queue:").append(sendRunnableNormPrior.getMsgQueue().size()).append(",isrunning:")
									//												.append(sendRunnableNormPrior.runningLock().getObj()).append(",isneedrun:").append(sendRunnableNormPrior.isNeededExecute()).append(" | ");
									//										if (sendRunnableNormPrior.isNeededExecute() && !sendRunnableNormPrior.runningLock().getObj())
									//										{
									////											sendRunnableNormPrior.getExecutor().execute(sendRunnableNormPrior);
									//										}
									//									}
									////									if (sb.length() > 0)
									////									{
									////										log.error(channelContext.toString() + " : " + sb.toString());
									////									}
								}
							} catch (Throwable e)
							{
								log.error("", e);
							} finally
							{
								readLock.unlock();
							}
						} catch (Throwable e)
						{
							log.error("", e);
						}

						Thread.sleep(5000);
					} catch (Throwable e)
					{
						log.error("", e);
					}
				}
			}
		}, "t-aio-server-monitor").start();
	}

}
