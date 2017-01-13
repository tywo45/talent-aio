
package com.talent.aio.common;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.CloseRunnable;
import com.talent.aio.common.task.DecodeRunnable;
import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.utils.SystemTimer;

/**
 * The Class ChannelContext.
 *
 * @author tanyaowu
 * @创建时间 2016年11月14日 下午5:40:25
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月14日 | tanyaowu | 新建类
 */
public abstract class ChannelContext<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(ChannelContext.class);

	private static java.util.concurrent.atomic.AtomicLong idAtomicLong = new AtomicLong();

	//	private java.util.concurrent.Semaphore sendSemaphore = new Semaphore(1);

	private GroupContext<Ext, P, R> groupContext = null;

	private DecodeRunnable<Ext, P, R> decodeRunnable = null;

	private CloseRunnable<Ext, P, R> closeRunnable = null;
//	private HandlerRunnable<Ext, P, R> handlerRunnableHighPrior = null;
	private HandlerRunnable<Ext, P, R> handlerRunnableNormPrior = null;

//	private SendRunnable<Ext, P, R> sendRunnableHighPrior = null;
	private SendRunnable<Ext, P, R> sendRunnableNormPrior = null;

	private ReadCompletionHandler<Ext, P, R> readCompletionHandler = new ReadCompletionHandler<>(this);
	private WriteCompletionHandler<Ext, P, R> writeCompletionHandler = new WriteCompletionHandler<>(this);
	
	private int reConnCount = 0;//重连次数，连接成功后，此值会被重置0

	//	private WriteCompletionHandler<Ext, P, R> writeCompletionHandler = new WriteCompletionHandler<>();

	private String userid;

	private boolean isClosed = false;

	private Stat stat = new Stat();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月14日 下午5:40:25
	 */
	public static void main(String[] args)
	{
	}

	/** The asynchronous socket channel. */
	private AsynchronousSocketChannel asynchronousSocketChannel;

	private Ext ext;

	private long id = idAtomicLong.incrementAndGet();

	private Node clientNode;

	/**
	 * @param groupContext
	 * @param asynchronousSocketChannel
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 下午1:13:56
	 * 
	 */
	public ChannelContext(GroupContext<Ext, P, R> groupContext, AsynchronousSocketChannel asynchronousSocketChannel)
	{
		super();
		this.setAsynchronousSocketChannel(asynchronousSocketChannel);
		this.setGroupContext(groupContext);
	}

	/**
	 * 
	 * @param asynchronousSocketChannel
	 * @return
	 * @throws IOException
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午12:21:41
	 *
	 */
	public abstract Node getClientNode(AsynchronousSocketChannel asynchronousSocketChannel) throws IOException;

	@Override
	public String toString()
	{
		return this.getClientNode().toString();
	}

	/**
	 * @return the asynchronousSocketChannel
	 */
	public AsynchronousSocketChannel getAsynchronousSocketChannel()
	{
		return asynchronousSocketChannel;
	}

	/**
	 * @return the ext
	 */
	public Ext getExt()
	{
		return ext;
	}

	/**
	 * @return the id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * @return the remoteNode
	 */
	public Node getClientNode()
	{
		return clientNode;
	}

	/**
	 * @param asynchronousSocketChannel the asynchronousSocketChannel to set
	 */
	public void setAsynchronousSocketChannel(AsynchronousSocketChannel asynchronousSocketChannel)
	{
		this.asynchronousSocketChannel = asynchronousSocketChannel;

		if (asynchronousSocketChannel != null)
		{
			try
			{
				clientNode = getClientNode(asynchronousSocketChannel);
			} catch (IOException e)
			{
				log.error(e.toString(), e);
			}
		} else
		{
			clientNode = null;
		}
	}

	/**
	 * @param ext the ext to set
	 */
	public void setExt(Ext ext)
	{
		this.ext = ext;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @param remoteNode the remoteNode to set
	 */
	public void setClientNode(Node clientNode)
	{
		this.clientNode = clientNode;
	}

	/**
	 * @return the groupContext
	 */
	public GroupContext<Ext, P, R> getGroupContext()
	{
		return groupContext;
	}

	/**
	 * @param groupContext the groupContext to set
	 */
	public void setGroupContext(GroupContext<Ext, P, R> groupContext)
	{
		this.groupContext = groupContext;

		if (groupContext != null)
		{
			decodeRunnable = new DecodeRunnable<>(this, groupContext.getDecodeExecutor());
			closeRunnable = new CloseRunnable<>(this, null, null, groupContext.getCloseExecutor());

//			handlerRunnableHighPrior = new HandlerRunnable<>(this, groupContext.getHandlerExecutorHighPrior());
			handlerRunnableNormPrior = new HandlerRunnable<>(this, groupContext.getHandlerExecutorNormPrior());

//			sendRunnableHighPrior = new SendRunnable<>(this, groupContext.getSendExecutorHighPrior());
			sendRunnableNormPrior = new SendRunnable<>(this, groupContext.getSendExecutorNormPrior());

			groupContext.getConnections().add(this);
			groupContext.getClientNodes().put(this);
		}
	}

	/**
	 * @return the readCompletionHandler
	 */
	public ReadCompletionHandler<Ext, P, R> getReadCompletionHandler()
	{
		return readCompletionHandler;
	}

	/**
	 * @param readCompletionHandler the readCompletionHandler to set
	 */
	public void setReadCompletionHandler(ReadCompletionHandler<Ext, P, R> readCompletionHandler)
	{
		this.readCompletionHandler = readCompletionHandler;
	}

	/**
	 * @return the decodeRunnable
	 */
	public DecodeRunnable<Ext, P, R> getDecodeRunnable()
	{
		return decodeRunnable;
	}

	/**
	 * @param decodeRunnable the decodeRunnable to set
	 */
	public void setDecodeRunnable(DecodeRunnable<Ext, P, R> decodeRunnable)
	{
		this.decodeRunnable = decodeRunnable;
	}

//	/**
//	 * @return the handlerRunnableHighPrior
//	 */
//	public HandlerRunnable<Ext, P, R> getHandlerRunnableHighPrior()
//	{
//		return handlerRunnableHighPrior;
//	}

//	/**
//	 * @param handlerRunnableHighPrior the handlerRunnableHighPrior to set
//	 */
//	public void setHandlerRunnableHighPrior(HandlerRunnable<Ext, P, R> handlerRunnableHighPrior)
//	{
//		this.handlerRunnableHighPrior = handlerRunnableHighPrior;
//	}

	/**
	 * @return the handlerRunnableNormPrior
	 */
	public HandlerRunnable<Ext, P, R> getHandlerRunnableNormPrior()
	{
		return handlerRunnableNormPrior;
	}

	/**
	 * @param handlerRunnableNormPrior the handlerRunnableNormPrior to set
	 */
	public void setHandlerRunnableNormPrior(HandlerRunnable<Ext, P, R> handlerRunnableNormPrior)
	{
		this.handlerRunnableNormPrior = handlerRunnableNormPrior;
	}

//	/**
//	 * @return the sendRunnableHighPrior
//	 */
//	public SendRunnable<Ext, P, R> getSendRunnableHighPrior()
//	{
//		return sendRunnableHighPrior;
//	}
//
//	/**
//	 * @param sendRunnableHighPrior the sendRunnableHighPrior to set
//	 */
//	public void setSendRunnableHighPrior(SendRunnable<Ext, P, R> sendRunnableHighPrior)
//	{
//		this.sendRunnableHighPrior = sendRunnableHighPrior;
//	}

	/**
	 * @return the sendRunnableNormPrior
	 */
	public SendRunnable<Ext, P, R> getSendRunnableNormPrior()
	{
		return sendRunnableNormPrior;
	}

	/**
	 * @param sendRunnableNormPrior the sendRunnableNormPrior to set
	 */
	public void setSendRunnableNormPrior(SendRunnable<Ext, P, R> sendRunnableNormPrior)
	{
		this.sendRunnableNormPrior = sendRunnableNormPrior;
	}

	//	/**
	//	 * @return the writeCompletionHandler
	//	 */
	//	public WriteCompletionHandler<Ext, P, R> getWriteCompletionHandler()
	//	{
	//		return writeCompletionHandler;
	//	}
	//
	//	/**
	//	 * @param writeCompletionHandler the writeCompletionHandler to set
	//	 */
	//	public void setWriteCompletionHandler(WriteCompletionHandler<Ext, P, R> writeCompletionHandler)
	//	{
	//		this.writeCompletionHandler = writeCompletionHandler;
	//	}

	/**
	 * @return the userid
	 */
	public String getUserid()
	{
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	/**
	 * @return the isClosed
	 */
	public boolean isClosed()
	{
		return isClosed;
	}

	/**
	 * @param isClosed the isClosed to set
	 */
	public void setClosed(boolean isClosed)
	{
		this.isClosed = isClosed;
	}

	/**
	 * @return the closeRunnable
	 */
	public CloseRunnable<Ext, P, R> getCloseRunnable()
	{
		return closeRunnable;
	}

	/**
	 * @param closeRunnable the closeRunnable to set
	 */
	public void setCloseRunnable(CloseRunnable<Ext, P, R> closeRunnable)
	{
		this.closeRunnable = closeRunnable;
	}

	/**
	 * @return the stat
	 */
	public Stat getStat()
	{
		return stat;
	}

	/**
	 * @param stat the stat to set
	 */
	public void setStat(Stat stat)
	{
		this.stat = stat;
	}

	//	/**
	//	 * @return the sendSemaphore
	//	 */
	//	public java.util.concurrent.Semaphore getSendSemaphore()
	//	{
	//		return sendSemaphore;
	//	}

	/**
	 * @return the writeCompletionHandler
	 */
	public WriteCompletionHandler<Ext, P, R> getWriteCompletionHandler()
	{
		return writeCompletionHandler;
	}

	/**
	 * @param writeCompletionHandler the writeCompletionHandler to set
	 */
	public void setWriteCompletionHandler(WriteCompletionHandler<Ext, P, R> writeCompletionHandler)
	{
		this.writeCompletionHandler = writeCompletionHandler;
	}

	/**
	 * @return the reConnCount
	 */
	public int getReConnCount()
	{
		return reConnCount;
	}

	/**
	 * @param reConnCount the reConnCount to set
	 */
	public void setReConnCount(int reConnCount)
	{
		this.reConnCount = reConnCount;
	}

	public static class Stat
	{
		/**
		 * 最近一次收消息的时间
		 */
		private long timeLatestReceivedMsg = SystemTimer.currentTimeMillis();

		/**
		 * 最近一次发消息的时间
		 */
		private long timeLatestSentMsg = SystemTimer.currentTimeMillis();
		
		/**
		 * 连接关闭的时间
		 */
		private long timeClosed = SystemTimer.currentTimeMillis();

		/**
		 * 本连接已发送的字节数
		 */
		private AtomicLong countSentByte = new AtomicLong();

		/**
		 * 本连接已发送的packet数
		 */
		private AtomicLong countSentPacket = new AtomicLong();

		/**
		 * 本连接已处理的字节数
		 */
		private AtomicLong countHandledByte = new AtomicLong();

		/**
		 * 本连接已处理的packet数
		 */
		private AtomicLong countHandledPacket = new AtomicLong();

		/**
		 * 本连接已接收的字节数
		 */
		private AtomicLong countReceivedByte = new AtomicLong();

		/**
		 * 本连接已接收的packet数
		 */
		private AtomicLong countReceivedPacket = new AtomicLong();

		/**
		 * @return the timeLatestReceivedMsg
		 */
		public long getTimeLatestReceivedMsg()
		{
			return timeLatestReceivedMsg;
		}

		/**
		 * @param timeLatestReceivedMsg the timeLatestReceivedMsg to set
		 */
		public void setTimeLatestReceivedMsg(long timeLatestReceivedMsg)
		{
			this.timeLatestReceivedMsg = timeLatestReceivedMsg;
		}

		/**
		 * @return the timeLatestSentMsg
		 */
		public long getTimeLatestSentMsg()
		{
			return timeLatestSentMsg;
		}

		/**
		 * @param timeLatestSentMsg the timeLatestSentMsg to set
		 */
		public void setTimeLatestSentMsg(long timeLatestSentMsg)
		{
			this.timeLatestSentMsg = timeLatestSentMsg;
		}

		/**
		 * @return the countSentByte
		 */
		public AtomicLong getCountSentByte()
		{
			return countSentByte;
		}

		/**
		 * @param countSentByte the countSentByte to set
		 */
		public void setCountSentByte(AtomicLong countSentByte)
		{
			this.countSentByte = countSentByte;
		}

		/**
		 * @return the countSentPacket
		 */
		public AtomicLong getCountSentPacket()
		{
			return countSentPacket;
		}

		/**
		 * @param countSentPacket the countSentPacket to set
		 */
		public void setCountSentPacket(AtomicLong countSentPacket)
		{
			this.countSentPacket = countSentPacket;
		}

		/**
		 * @return the countHandledByte
		 */
		public AtomicLong getCountHandledByte()
		{
			return countHandledByte;
		}

		/**
		 * @param countHandledByte the countHandledByte to set
		 */
		public void setCountHandledByte(AtomicLong countHandledByte)
		{
			this.countHandledByte = countHandledByte;
		}

		/**
		 * @return the countHandledPacket
		 */
		public AtomicLong getCountHandledPacket()
		{
			return countHandledPacket;
		}

		/**
		 * @param countHandledPacket the countHandledPacket to set
		 */
		public void setCountHandledPacket(AtomicLong countHandledPacket)
		{
			this.countHandledPacket = countHandledPacket;
		}

		/**
		 * @return the countReceivedByte
		 */
		public AtomicLong getCountReceivedByte()
		{
			return countReceivedByte;
		}

		/**
		 * @param countReceivedByte the countReceivedByte to set
		 */
		public void setCountReceivedByte(AtomicLong countReceivedByte)
		{
			this.countReceivedByte = countReceivedByte;
		}

		/**
		 * @return the countReceivedPacket
		 */
		public AtomicLong getCountReceivedPacket()
		{
			return countReceivedPacket;
		}

		/**
		 * @param countReceivedPacket the countReceivedPacket to set
		 */
		public void setCountReceivedPacket(AtomicLong countReceivedPacket)
		{
			this.countReceivedPacket = countReceivedPacket;
		}

		/**
		 * @return the timeClosed
		 */
		public long getTimeClosed()
		{
			return timeClosed;
		}

		/**
		 * @param timeClosed the timeClosed to set
		 */
		public void setTimeClosed(long timeClosed)
		{
			this.timeClosed = timeClosed;
		}

	}

}
