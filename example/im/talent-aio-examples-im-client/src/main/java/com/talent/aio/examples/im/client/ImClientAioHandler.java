/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:13:15
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.examples.im.client.handler.AuthRespHandler;
import com.talent.aio.examples.im.client.handler.ChatRespHandler;
import com.talent.aio.examples.im.client.handler.HandshakeRespHandler;
import com.talent.aio.examples.im.client.handler.ImAioHandlerIntf;
import com.talent.aio.examples.im.client.handler.JoinRespHandler;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;
import com.talent.aio.examples.im.common.packets.Command;
import com.talent.aio.examples.im.common.utils.GzipUtils;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:13:15
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class ImClientAioHandler implements ClientAioHandler<ImSessionContext, ImPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(ImClientAioHandler.class);

	private static Map<Command, ImAioHandlerIntf> handlerMap = new HashMap<>();
	static
	{
		handlerMap.put(Command.COMMAND_AUTH_RESP, new AuthRespHandler());
		handlerMap.put(Command.COMMAND_CHAT_RESP, new ChatRespHandler());
		handlerMap.put(Command.COMMAND_JOIN_GROUP_RESP, new JoinRespHandler());
		handlerMap.put(Command.COMMAND_HANDSHAKE_RESP, new HandshakeRespHandler());
	}

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public ImClientAioHandler()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#handler(com.talent.aio.common.intf.Packet)
	 * 
	 * @param packet
	 * @return
	 * @throws Exception 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public Object handler(ImPacket packet, ChannelContext<ImSessionContext, ImPacket, Object> channelContext) throws Exception
	{
		Command command = packet.getCommand();
		ImAioHandlerIntf handler = handlerMap.get(command);
		if (handler != null)
		{
			Object obj = handler.handler(packet, channelContext);
			CommandStat.getCount(command).handled.incrementAndGet();
			return obj;
		} else
		{
			CommandStat.getCount(command).handled.incrementAndGet();
			log.warn("找不到对应的命令码[{}]处理类", command);
			return null;
		}

	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#encode(com.talent.aio.common.intf.Packet)
	 * 
	 * @param packet
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public ByteBuffer encode(ImPacket packet, GroupContext<ImSessionContext, ImPacket, Object> groupContext, ChannelContext<ImSessionContext, ImPacket, Object> channelContext)
	{
		if (packet.getCommand() == Command.COMMAND_HEARTBEAT_REQ)
		{
			ByteBuffer buffer = ByteBuffer.allocate(1);
			buffer.put(ImPacket.HEARTBEAT_BYTE);
			return buffer;
		}
		if (packet.getCommand() == Command.COMMAND_HANDSHAKE_REQ)
		{
			ByteBuffer buffer = ByteBuffer.allocate(1);
			buffer.put(ImPacket.HANDSHAKE_BYTE);
			return buffer;
		}

		byte[] body = packet.getBody();
		int bodyLen = 0;
		boolean isCompress = false;
		boolean is4ByteLength = false;
		if (body != null)
		{
			bodyLen = body.length;

			if (bodyLen > 200)
			{
				try
				{
					byte[] gzipedbody = GzipUtils.gZip(body);
					if (gzipedbody.length < body.length)
					{
						log.error("压缩前:{}, 压缩后:{}", body.length, gzipedbody.length);
						body = gzipedbody;
						packet.setBody(gzipedbody);
						bodyLen = gzipedbody.length;
						isCompress = true;
					}
				} catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}
			}

			if (bodyLen > Short.MAX_VALUE)
			{
				is4ByteLength = true;
			}
		}

		int allLen = packet.calcHeaderLength(is4ByteLength) + bodyLen;

		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(groupContext.getByteOrder());

		byte firstbyte = ImPacket.encodeCompress(ImPacket.VERSION, isCompress);
		firstbyte = ImPacket.encodeHasSynSeq(firstbyte, packet.getSynSeq() > 0);
		firstbyte = ImPacket.encode4ByteLength(firstbyte, is4ByteLength);
		//		String bstr = Integer.toBinaryString(firstbyte);
		//		log.error("二进制:{}",bstr);

		buffer.put(firstbyte);
		buffer.put((byte)packet.getCommand().getNumber());// TODO 此处需要验证
		 

		//GzipUtils

		if (is4ByteLength)
		{
			buffer.putInt(bodyLen);
		} else
		{
			buffer.putShort((short) bodyLen);
		}

		if (packet.getSynSeq() != null && packet.getSynSeq() > 0)
		{
			buffer.putInt(packet.getSynSeq());
		}
		//		else
		//		{
		//			buffer.putInt(0);
		//		}
		//
		//		buffer.putInt(0);

		if (body != null)
		{
			buffer.put(body);
		}
		return buffer;
	}

	private static ImPacket handshakeRespPacket = new ImPacket(Command.COMMAND_HANDSHAKE_RESP);
	
	/** 
	 * @see com.talent.aio.common.intf.AioHandler#decode(java.nio.ByteBuffer)
	 * 
	 * @param buffer
	 * @return
	 * @throws AioDecodeException
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public ImPacket decode(ByteBuffer buffer, ChannelContext<ImSessionContext, ImPacket, Object> channelContext) throws AioDecodeException
	{
		ImSessionContext imSessionContext = channelContext.getSessionContext();
		byte firstbyte = buffer.get();
		
		if (!imSessionContext.isHandshaked())
		{
			if (ImPacket.HANDSHAKE_BYTE == firstbyte)
			{
				return handshakeRespPacket;
			} else
			{
				throw new AioDecodeException("还没握手");
			}
		}
		
		buffer.position(buffer.position() - 1);//位置复元
		
		
		int readableLength = buffer.limit() - buffer.position();

		int headerLength = ImPacket.LEAST_HEADER_LENGHT;
		ImPacket imPacket = null;
		firstbyte = buffer.get();
		@SuppressWarnings("unused")
		byte version = ImPacket.decodeVersion(firstbyte);
		boolean isCompress = ImPacket.decodeCompress(firstbyte);
		boolean hasSynSeq = ImPacket.decodeHasSynSeq(firstbyte);
		boolean is4ByteLength = ImPacket.decode4ByteLength(firstbyte);
		if (hasSynSeq)
		{
			headerLength += 4;
		}
		if (is4ByteLength)
		{
			headerLength += 2;
		}
		if (readableLength < headerLength)
		{
			return null;
		}
		Byte code = buffer.get();
		Command command = Command.valueOf(code);
		int bodyLength = 0;
		if (is4ByteLength)
		{
			bodyLength = buffer.getInt();
		} else
		{
			bodyLength = buffer.getShort();
		}

		if (bodyLength > ImPacket.MAX_LENGTH_OF_BODY || bodyLength < 0)
		{
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}

		int seq = 0;
		if (hasSynSeq)
		{
			seq = buffer.getInt();
		}

		//		@SuppressWarnings("unused")
		//		int reserve = buffer.getInt();//保留字段

		//		PacketMeta<ImPacket> packetMeta = new PacketMeta<>();
		int neededLength = headerLength + bodyLength;
		int test = readableLength - neededLength;
		if (test < 0) // 不够消息体长度(剩下的buffe组不了消息体)
		{
			//			packetMeta.setNeededLength(neededLength);
			return null;
		} else
		{
			imPacket = new ImPacket();
			imPacket.setCommand(command);

			if (seq != 0)
			{
				imPacket.setSynSeq(seq);
			}

			if (bodyLength > 0)
			{
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				if (isCompress)
				{
					try
					{
						byte[] unGzippedBytes = GzipUtils.unGZip(dst);
						imPacket.setBody(unGzippedBytes);
//						imPacket.setBodyLen(unGzippedBytes.length);
					} catch (IOException e)
					{
						throw new AioDecodeException(e);
					}
				} else
				{
					imPacket.setBody(dst);
//					imPacket.setBodyLen(dst.length);
				}
			}

			//			packetMeta.setPacket(imPacket);
			return imPacket;

		}

	}

	private static ImPacket heartbeatPacket = new ImPacket(Command.COMMAND_HEARTBEAT_REQ);

	/** 
	 * @see com.talent.aio.client.intf.ClientAioHandler#heartbeatPacket()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午2:18:16
	 * 
	 */
	@Override
	public ImPacket heartbeatPacket()
	{
		return heartbeatPacket;
	}

}
