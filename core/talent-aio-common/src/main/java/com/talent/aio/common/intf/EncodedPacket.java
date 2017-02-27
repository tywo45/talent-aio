package com.talent.aio.common.intf;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月22日 下午1:14:31
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月22日 | tanyaowu | 新建类
 *
 */
public class EncodedPacket extends Packet
{

	private byte[] bytes;
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月22日 下午1:14:31
	 * 
	 */
	public EncodedPacket(byte[] bytes)
	{
		this.bytes = bytes;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月22日 下午1:14:31
	 * 
	 */
	public static void main(String[] args)
	{}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes()
	{
		return bytes;
	}

	/**
	 * @param bytes the bytes to set
	 */
	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

}
