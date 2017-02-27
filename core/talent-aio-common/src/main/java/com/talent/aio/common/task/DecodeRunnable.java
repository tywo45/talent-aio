/**
 * 
 */
package com.talent.aio.common.task;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractQueueRunnable;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;
import com.talent.aio.common.utils.AioUtils;
import com.talent.aio.common.utils.ByteBufferUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 解码
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class DecodeRunnable<SessionContext, P extends Packet, R> extends AbstractQueueRunnable<ByteBuffer>
{
	private static final Logger log = LoggerFactory.getLogger(DecodeRunnable.class);

	private ChannelContext<SessionContext, P, R> channelContext = null;

	/**
	 * 上一次解码，剩下的数据
	 */
	private ByteBuffer lastByteBuffer = null;

	/**
	 * 
	 */
	public DecodeRunnable(ChannelContext<SessionContext, P, R> channelContext, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
	}

	/**
	 * 清空处理的队列消息
	 */
	public void clearMsgQueue()
	{
		msgQueue.clear();
		lastByteBuffer = null;
	}

	@Override
	public void runTask()
	{
		@SuppressWarnings("unused")
		int size = 0;
		ByteBuffer byteBuffer = null;
		label_1: while ((size = msgQueue.size()) > 0)
		{
			byteBuffer = msgQueue.poll();
			//log.error(byteBuffer.toString());
			if (byteBuffer != null)
			{
				if (lastByteBuffer != null)
				{
					byteBuffer.position(0);
					byteBuffer = ByteBufferUtils.composite(lastByteBuffer, byteBuffer);
					lastByteBuffer = null;
				}
			} else
			{
				break label_1;
			}

			try
			{
				byteBuffer.position(0);
				label_2: while (true)
				{
					int initPosition = byteBuffer.position();
					P packet = channelContext.getGroupContext().getAioHandler().decode(byteBuffer, channelContext);

					if (packet == null)// 数据不够，组不了包
					{
						if (log.isDebugEnabled())
						{
							log.debug("{},数据不够，组不了包", channelContext.toString());
						}
//						log.error("{},数据不够，组不了包", channelContext.toString());
						byteBuffer.position(initPosition);
						lastByteBuffer = byteBuffer;
						continue label_1;
					} else //组包成功
					{
						channelContext.getStat().setLatestTimeOfReceivedPacket(SystemTimer.currentTimeMillis());
						
						int afterDecodePosition = byteBuffer.position();
						int len = afterDecodePosition - initPosition;
						
						channelContext.getGroupContext().getGroupStat().getReceivedPacket().incrementAndGet();
						channelContext.getGroupContext().getGroupStat().getReceivedBytes().addAndGet(len);
						
						submit(packet, len);
						
						AioListener<SessionContext, P, R> aioListener = channelContext.getGroupContext().getAioListener();
						try
						{
							log.info("{} 收到:{}", channelContext, packet.logstr());
							aioListener.onAfterReceived(channelContext, packet, len);
						} catch (Exception e)
						{
							log.error(e.toString(), e);
						}
						

						if (byteBuffer.hasRemaining())//组包后，还剩有数据
						{
							if (log.isDebugEnabled())
							{
								log.debug("{},组包后，还剩有数据:{}", channelContext, byteBuffer.limit() - byteBuffer.position());
							}
							continue label_2;
						} else//组包后，数据刚好用完
						{
							lastByteBuffer = null;
							log.debug("{},组包后，数据刚好用完", channelContext);
							continue label_1;
						}
					}
				}
			} catch (AioDecodeException e)
			{
				log.error(channelContext.toString(), e);
				Aio.close(channelContext, e, "解码异常:" + e.getMessage());
				break label_1;
			} finally
			{

			}
		}
	}

	/**
	 * 
	 * @param packets
	 * @param byteCount
	 */
	private void submit(P packet, int byteCount)
	{
		handler(channelContext, packet);
	}

	/**
	 * Handler.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param packet the packet
	 */
	public static <SessionContext, P extends Packet, R> void handler(ChannelContext<SessionContext, P, R> channelContext, P packet)
	{
		if (channelContext.isClosed() || channelContext.isRemoved())
		{
			log.error("{} 已经关闭", channelContext);
			return;
		}
		HandlerRunnable<SessionContext, P, R> handlerRunnable = AioUtils.selectHandlerRunnable(channelContext, packet);
		handlerRunnable.addMsg(packet);
		SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor = AioUtils.selectHandlerExecutor(channelContext, packet);
		synThreadPoolExecutor.execute(handlerRunnable);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	public ChannelContext<SessionContext, P, R> getChannelContext()
	{
		return channelContext;
	}

	public void setChannelContext(ChannelContext<SessionContext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()).append(":");
		builder.append(channelContext.toString());
		return builder.toString();
	}

}
