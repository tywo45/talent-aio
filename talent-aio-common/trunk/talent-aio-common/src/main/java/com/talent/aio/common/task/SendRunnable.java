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

//	public void sendPackets(List<P> packets) throws IOException
//	{
//		if (packets == null || packets.size() == 0)
//		{
//			log.error("Packets is null，please check synchronize");
//			return;
//		}
//
//		for (P packet : packets)
//		{
//			
//			sendPacket(packet);
//		}
//
//	}

	public void sendPacket(P packet)
	{
		if (packet == null)
		{
			log.error("Packet is null，please check synchronize");
			return;
		}
		
		if (!AioUtils.checkBeforeIO(channelContext))
		{
			return;
		}
		GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
//		GroupStat groupStat = groupContext.getGroupStat();
//		log.error("发送数据:{}", packet);
		ByteBuffer byteBuffer = groupContext.getAioHandler().encode(packet, channelContext);
//		int sentSize = byteBuffer.capacity();
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
		
//		writeCompletionHandler.setPacket(packet);
		asynchronousSocketChannel.write(byteBuffer, packet, writeCompletionHandler);
		
//		Future<Integer> future = asynchronousSocketChannel.write(byteBuffer);
//		Integer result = null;
//		try
//		{
//			result = future.get();
//		} catch (InterruptedException e)
//		{
//			log.error(e.toString(), e);
//		} catch (ExecutionException e)
//		{
//			log.error(e.toString(), e);
//		}
//
//		if (result != null)
//		{
//			if (result > 0)
//			{
//				SendListener<Ext, P, R> aioListener = groupContext.getSendListener();
//				groupStat.getSentPacket().incrementAndGet();
//				groupStat.getSentBytes().addAndGet(result);
//				channelContext.getStat().setTimeLatestSentMsg(SystemTimer.currentTimeMillis());
//				if (aioListener != null)
//				{
//					aioListener.onAfterSent(channelContext, packet, result);
//				}
//			} else if (result == 0)
//			{
//				log.error("发送长度为{}", result);
//				Aio.close(channelContext, "写数据返回:" + result);
//			} else if (result < 0)
//			{
//				log.error("发送长度为{}", result);
//				Aio.close(channelContext, "写数据返回:" + result);
//			}
//		} else
//		{
//			Aio.close(channelContext, "写不了数据");
//		}
	}

	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

//	@Override
//	public void runBatch(List<P> t)
//	{
//		try
//		{
//			sendPackets(t);
//		} catch (IOException e)
//		{
//			Aio.close(channelContext, e, "发送消息包发生IO异常");
//		}
//	}

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
		P packet = null;
		while ((packet = queue.poll()) != null)
		{
			sendPacket(packet);
		}
		
		
		
//		ConcurrentLinkedQueue<P> queue = getMsgQueue();
//		P packet = null;
//		while (true)
//		{
//			packet = queue.poll();
//			if (packet != null)
//			{
//				sendPacket(packet);
//			} else
//			{
//				break;
//			}
//		}
	}
}
