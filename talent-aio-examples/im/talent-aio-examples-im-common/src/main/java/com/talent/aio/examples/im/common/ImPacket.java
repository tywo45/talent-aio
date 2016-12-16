/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:15:03
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:15:03
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class ImPacket extends Packet
{
	private static Logger log = LoggerFactory.getLogger(ImPacket.class);

	public static final int HEADER_LENGHT = 15;
	
	public static final String CHARSET = "utf-8";
	
	/**
	 * 协议版本号
	 */
	public final static byte VERSION = 1;
	
	public static final long MAX_LENGTH_OF_BODY = (long) (1024 * 1024 * 0.1); //只支持0.1M
	
	private byte[] body;

	public ImPacket(byte[] body, short command)
	{
		super();
		this.body = body;
		this.setCommand(command);
	}

	public ImPacket(short command)
	{
		super();
		this.setCommand(command);
	}

	public ImPacket()
	{

	}



	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	
	@JSONField(serialize = false)
	private int bodyLen;

	private short command;

	

	public int getBodyLen()
	{
		return bodyLen;
	}

	public short getCommand()
	{
		return command;
	}
	
//	public final static AtomicInteger seq = new AtomicInteger();
//
//	private Integer seqNo = null;
//
//	@Override
//	public String getSeqNo()
//	{
//		if (this.seqNo == null)
//		{
//			return null;
//		}
//		return String.valueOf(this.seqNo);
//	}
//
//	@Override
//	public void setSeqNo(String seqNo)
//	{
//		this.seqNo = seqNo;
//	}

	

	public void setBodyLen(int bodyLen)
	{
		this.bodyLen = bodyLen;
	}

	public void setCommand(short type)
	{
		this.command = type;
//		if (com.talent.im.common.command.Command.PRIORITY_HANDLER_COMMANDS.contains(type))
//		{
//			this.setPriority(PRIORITY_MAX);
//		}
	}

	/**
	 * @return the body
	 */
	public byte[] getBody()
	{
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body)
	{
		this.body = body;
	}




}
