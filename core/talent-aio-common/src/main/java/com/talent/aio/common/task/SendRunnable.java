package com.talent.aio.common.task;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.WriteCompletionHandler;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractQueueRunnable;
import com.talent.aio.common.utils.AioUtils;

/**
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class SendRunnable<Ext, P extends Packet, R> extends AbstractQueueRunnable<P>
{

	private static final Logger log = LoggerFactory.getLogger(SendRunnable.class);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}

	private ChannelContext<Ext, P, R> channelContext = null;

	/**
	 * 
	 * @param socketChannelId
	 */
	public SendRunnable(ChannelContext<Ext, P, R> channelContext, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
	}

	/**
	 * 添加要处理的消息
	 * 
	 * @param packet
	 */
	public void addMsg(P packet)
	{
		getMsgQueue().add(packet);

	}

	/**
	 * 清空消息队列
	 */
	public void clearMsgQueue()
	{
		getMsgQueue().clear();
	}

	public void sendPacket(P packet)
	{
		GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
		ByteBuffer byteBuffer = groupContext.getAioHandler().encode(packet, channelContext);
		int packetCount = 1;
		sendByteBuffer(byteBuffer, packetCount);
	}

	public void sendByteBuffer(ByteBuffer byteBuffer, Integer packetCount)
	{
		if (byteBuffer == null)
		{
			log.error("byteBuffer is null");
			return;
		}

		if (!AioUtils.checkBeforeIO(channelContext))
		{
			return;
		}

		byteBuffer.flip();
		AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
		WriteCompletionHandler<Ext, P, R> writeCompletionHandler = channelContext.getWriteCompletionHandler();
		try
		{
			writeCompletionHandler.getWriteSemaphore().acquire();
		} catch (InterruptedException e)
		{
			log.error(e.toString(), e);
		}
		asynchronousSocketChannel.write(byteBuffer, packetCount, writeCompletionHandler);
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

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#runTask()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月5日 下午2:57:33
	 * 
	 */
	@Override
	public void runTask()
	{
		ConcurrentLinkedQueue<P> queue = getMsgQueue();
		int queueSize = queue.size();
		if (queueSize == 0)
		{
			return;
		}
		if (queueSize >= 2000)
		{
			queueSize = 1000;
		}

		P packet = null;
		GroupContext<Ext, P, R> groupContext = this.channelContext.getGroupContext();
		AioListener<Ext, P, R> aioListener = groupContext.getAioListener();
		AioHandler<Ext, P, R> aioHandler = groupContext.getAioHandler();

		if (queueSize > 1)
		{

			ByteBuffer[] byteBuffers = new ByteBuffer[queueSize];
			int allBytebufferCapacity = 0;

			int packetCount = 0;
			for (int i = 0; i < queueSize; i++)
			{
				if ((packet = queue.poll()) != null)
				{

					ByteBuffer byteBuffer = aioHandler.encode(packet, channelContext);
					allBytebufferCapacity += byteBuffer.limit();
					packetCount++;
					byteBuffers[i] = byteBuffer;

					if (aioListener != null)
					{
						try
						{
							aioListener.onBeforeSent(channelContext, packet);
						} catch (Exception e)
						{
							log.error(e.toString(), e);
						}
					}
				} else
				{
					break;
				}
			}

			ByteBuffer allByteBuffer = ByteBuffer.allocate(allBytebufferCapacity);
			for (ByteBuffer byteBuffer : byteBuffers)
			{
				if (byteBuffer != null)
				{
					byteBuffer.flip();
					allByteBuffer.put(byteBuffer);
				}
			}

			this.sendByteBuffer(allByteBuffer, packetCount);

		} else
		{
			if ((packet = queue.poll()) != null)
			{
				if (aioListener != null)
				{
					try
					{
						aioListener.onBeforeSent(channelContext, packet);
					} catch (Exception e)
					{
						log.error(e.toString(), e);
					}
				}
				sendPacket(packet);
			}
		}

		if (queue.size() > 0)
		{
			runTask();
		}
	}
}
