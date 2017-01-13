/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午1:31:04
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.DecodeRunnable;
import com.talent.aio.common.utils.AioUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午1:31:04
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class ReadCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<Integer, ByteBuffer>
{

	private static Logger log = LoggerFactory.getLogger(ReadCompletionHandler.class);
	private ChannelContext<Ext, P, R> channelContext = null;

	//	private ByteBuffer byteBuffer = ByteBuffer.allocate(ChannelContext.READ_BUFFER_SIZE);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public ReadCompletionHandler(ChannelContext<Ext, P, R> channelContext)
	{
		this.setChannelContext(channelContext);
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param result
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void completed(Integer result, ByteBuffer byteBuffer)
	{
		GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
		if (result > 0)
		{
			channelContext.getStat().setTimeLatestReceivedMsg(SystemTimer.currentTimeMillis());
			byteBuffer.limit(byteBuffer.position());
			byteBuffer.position(0);
			//			byteBuffer.flip();
			//			ByteBuffer byteBuffer1 = ByteBuffer.allocate(byteBuffer.limit());
			//			byteBuffer1.put(byteBuffer);
			//			byteBuffer.clear();

			DecodeRunnable<Ext, P, R> decodeRunnable = channelContext.getDecodeRunnable();
			decodeRunnable.addMsg(byteBuffer);

			groupContext.getDecodeExecutor().execute(decodeRunnable);

		} else if (result == 0)
		{
			log.error("读到的数据长度为0");
		} else if (result < 0)
		{
			Aio.close(channelContext, null, "读数据时返回" + result);
		}

		if (AioUtils.checkBeforeIO(channelContext))
		{
			AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
			ByteBuffer newByteBuffer = ByteBuffer.allocate(channelContext.getGroupContext().getReadBufferSize());
			asynchronousSocketChannel.read(newByteBuffer, newByteBuffer, this);
		}

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void failed(Throwable exc, ByteBuffer byteBuffer)
	{
		Aio.close(channelContext, exc, "读数据时发生异常");

	}

	//	/**
	//	 * @return the byteBuffer
	//	 */
	//	public ByteBuffer getByteBuffer()
	//	{
	//		return byteBuffer;
	//	}
	//
	//	/**
	//	 * @param byteBuffer the byteBuffer to set
	//	 */
	//	public void setByteBuffer(ByteBuffer byteBuffer)
	//	{
	//		this.byteBuffer = byteBuffer;
	//	}

	/**
	 * @return the channelContext
	 */
	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	/**
	 * @param channelContext the channelContext to set
	 */
	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

}
