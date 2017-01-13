/**
 * 
 */
package com.talent.aio.common.task;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.Syns;
import com.talent.aio.common.threadpool.AbstractQueueRunnable;

/**
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class HandlerRunnable<Ext, P extends Packet, R> extends AbstractQueueRunnable<P>
{
	private static final Logger log = LoggerFactory.getLogger(HandlerRunnable.class);

	//	private DecodeRunnable decodeRunnable = null;

	// private String msgType = null;

	private ChannelContext<Ext, P, R> channelContext = null;

	public HandlerRunnable(ChannelContext<Ext, P, R> channelContext, Executor executor)
	{
		super(executor);
		this.setChannelContext(channelContext);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	//	@Override
	//	public void runTask()
	//	{
	//		List<Packet> packets = null;
	//		ConcurrentLinkedQueue<List<Packet>> queue = getMsgQueue();
	//		while (queue.size() > 0)
	//		{
	//			if (waitingRunCount() > 0)
	//			{
	//				break;
	//			}
	//
	//			packets = queue.poll();
	//
	//			// packet = msgQueue.poll();
	//			// if (packet == null)
	//			// {
	//			// continue;
	//			// }
	//			try
	//			{
	//				doPackets(packets, decodeRunnable.getChannelContext());
	//
	//			} catch (Exception e)
	//			{
	//				log.error(packets.toString());
	//				throw new RuntimeException(e.toString(), e);
	//			}
	//
	//			if (log.isDebugEnabled())
	//			{
	//				log.debug("total processed[" + StatVo.getAllProcessedPacketCount().get() + "];" + "["
	//						+ decodeRunnable.getChannelContext().getId() + "] processed["
	//						+ decodeRunnable.getProcessedPacketCount().get() + "],waiting["
	//						+ decodeRunnable.getMsgQueue().size() + "]；"
	//						+ channelContext.getAioHandler().getClass().getName() + " processed["
	//						+ this.getProcessedPacketCount().get() + "],waiting[" + queue.size() + "]");
	//			}
	//		}
	//	}

	private AtomicLong synFailCount = new AtomicLong();
	
	private int doPacket(P packet)
	{
		int ret = 0;
		if (packet != null)
		{
			try
			{
				GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();

				Integer synSeq = packet.getSynSeq();
				if (synSeq != null && synSeq > 0)
				{
					Syns<Ext, P, R> syns = channelContext.getGroupContext().getSyns();
					P initPacket = syns.remove(synSeq);
					if (initPacket != null)
					{
						synchronized (initPacket)
						{
							syns.put(synSeq, packet);
							initPacket.notify();
						}
						groupContext.getGroupStat().getHandledPacket().incrementAndGet();
					} else
					{
						log.error("[{}]同步消息失败, synSeq is {}, 但是同步集合中没有对应key值", synFailCount.incrementAndGet(), synSeq);
					}
				} else
				{
					groupContext.getAioHandler().handler(packet, channelContext);
					groupContext.getGroupStat().getHandledPacket().incrementAndGet();
				}
				ret++;
			} catch (Exception e)
			{
				log.error(e.toString(), e);
				return ret;
			} finally
			{

			}
		}
		return ret;
	}

	/**
	 * 添加要处理的消息
	 * 
	 * @param packet
	 */
	public void addMsg(P packet)
	{
		//log.error("handler queue size:" + getMsgQueue().size());
		getMsgQueue().add(packet);
	}

	/**
	 * 清空处理的队列消息
	 */
	public void clearMsgQueue()
	{
		getMsgQueue().clear();
	}

	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	//	@Override
	//	public void runBatch(List<P> packetss)
	//	{
	//		for (P packet : packetss)
	//		{
	//			try
	//			{
	//				doPacket(packet);
	//			} catch (Exception e)
	//			{
	//				log.error(packet.toString());
	//				throw new RuntimeException(e.toString(), e);
	//			}
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
	 * @重写时间: 2016年12月5日 下午3:02:49
	 * 
	 */
	@Override
	public void runTask()
	{
		ConcurrentLinkedQueue<P> queue = getMsgQueue();
		P packet = null;
		while ((packet = queue.poll()) != null)
		{
			doPacket(packet);
		}

		//		ConcurrentLinkedQueue<P> queue = getMsgQueue();
		//		P packet = null;
		//		while (true)
		//		{
		//			packet = queue.poll();
		//			if (packet != null)
		//			{
		//				doPacket(packet);
		//			} else
		//			{
		//				break;
		//			}
		//		}

	}

	// public String getMsgType()
	// {
	// return msgType;
	// }
	//
	// public void setMsgType(String msgType)
	// {
	// this.msgType = msgType;
	// }

}
