/**
 * 
 */
package com.talent.aio.common.task;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractQueueRunnable;
import com.talent.aio.common.utils.ByteBufferUtils;

/**
 * 接收socket消息时，需要将消息组包。本类就是处理组包操作的。
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class DecodeRunnable<Ext, P extends Packet, R> extends AbstractQueueRunnable<ByteBuffer>
{
	private static final Logger log = LoggerFactory.getLogger(DecodeRunnable.class);

	private ChannelContext<Ext, P, R> channelContext = null;

	/**
	 * 上一次解码，剩下的数据
	 */
	private ByteBuffer lastByteBuffer = null;

	/**
	 * 
	 */
	public DecodeRunnable(ChannelContext<Ext, P, R> channelContext, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
	}

	/**
	 * 添加要解码的消息
	 * 
	 * @param datas
	 */
	public void addMsg(ByteBuffer datas)
	{
		getMsgQueue().add(datas);
	}

	/**
	 * 清空处理的队列消息
	 */
	public void clearMsgQueue()
	{
		getMsgQueue().clear();
		lastByteBuffer = null;
	}

	//	private int needLength = -1;

	@Override
	public void runTask()
	{
		ConcurrentLinkedQueue<ByteBuffer> queue = getMsgQueue();
		@SuppressWarnings("unused")
		int size = 0;
		ByteBuffer byteBuffer = null;
		label_1: while ((size = queue.size()) > 0)
		{
			byteBuffer = queue.poll();
			if (byteBuffer != null)
			{
				if (lastByteBuffer != null)
				{
					//					lastByteBuffer.position(0);
					//					lastByteBuffer.limit(lastByteBuffer.capacity());
					byteBuffer.position(0);
//					byteBuffer.limit(byteBuffer.capacity());
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
//				byteBuffer.limit(byteBuffer.capacity());

				label_2: while (true)
				{
					int initPosition = byteBuffer.position();
					P packet = channelContext.getGroupContext().getAioHandler().decode(byteBuffer, channelContext);

					if (packet == null)
					{
						// 数据不够，组不了包，
						byteBuffer.position(initPosition);
						//						byteBuffer.limit(byteBuffer.capacity());

						lastByteBuffer = byteBuffer;
						//						needLength = packetMeta.getNeededLength();
						continue label_1;
					} else //组包成功
					{

						int afterDecodePosition = byteBuffer.position();
						int len = afterDecodePosition - initPosition;
						AioListener<Ext, P, R> aioListener = channelContext.getGroupContext().getAioListener();
						if (aioListener != null)
						{
							aioListener.onAfterDecoded(channelContext, packet, len);
						}
						//						needLength = -1;
						submit(packet, len);
						channelContext.getGroupContext().getGroupStat().getReceivedPacket().incrementAndGet();
						channelContext.getGroupContext().getGroupStat().getReceivedBytes().addAndGet(len);

						if (byteBuffer.hasRemaining())//组包后，还剩有数据，留到下一次解码。
						{
							if (log.isDebugEnabled())
							{
								log.debug("组包后，还剩一点数据:{}", byteBuffer.limit() - byteBuffer.position());
							}

							//							lastByteBuffer = ByteBufferUtils.copy(byteBuffer, len, byteBuffer.capacity());

							continue label_2;
						} else
						{
							lastByteBuffer = null;
							log.debug("组包后，数据刚好用完");
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
		Aio.handler(channelContext, packet);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	//	public static SynThreadPoolExecutor<SynRunnableIntf> getThreadExecutor()
	//	{
	//		return handlerPoolExecutor1;
	//	}

	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
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
