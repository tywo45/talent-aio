/**
 * 
 */
package com.talent.aio.examples.im.common;

import java.nio.ByteBuffer;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 
 * 
 * @filename:	 com.talent.im.common.packet.ImByteReqPacket
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年3月30日 上午10:29:49
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
* 	<tr><td>2013年3月30日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ImBytePacket extends ImPacket
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6251241648032484262L;

	@JSONField(serialize = false)
	private ByteBuffer bytes = null;

	public ByteBuffer getBytes()
	{
		return bytes;
	}

	public ImBytePacket(ByteBuffer bytes)
	{
		this.bytes = bytes;
	}

	public void setBytes(ByteBuffer bytes)
	{
		this.bytes = bytes;
	}

	public ByteBuffer toBytes()
	{
		return bytes;
	}
}
