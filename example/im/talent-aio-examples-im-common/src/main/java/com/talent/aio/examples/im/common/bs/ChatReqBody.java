/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @filename:	 com.talent.aio.examples.common.im.bs.ChatReqBody
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年3月24日 下午2:41:26
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
* 	<tr><td>2013年3月24日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ChatReqBody  extends BaseReqBody
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ChatReqBody.class);

	/**
	 * 
	 */
	public ChatReqBody()
	{

	}

	

	private Integer type; //聊天类型
	private String text; //聊天内容
	private String group; //哪个组
	private Integer toId; //目标用户id，
	private String toNick; //目标用户nick，

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * 
	 * @param type 私聊还是公聊。从com.talent.im.common.Const.ChatType中取值
	 * @param text
	 * @param group
	 * @param toId
	 * @param toNick
	 * @author: tanyaowu
	 * @创建时间:　2013年8月10日 上午11:27:45
	 */
	public ChatReqBody(Integer type, String text, String group, Integer toId, String toNick)
	{
		super();
		this.type = type;
		this.text = text;
		this.group = group;
		this.toId = toId;
		this.toNick = toNick;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public Integer getToId()
	{
		return toId;
	}

	public void setToId(Integer toId)
	{
		this.toId = toId;
	}

	public String getToNick()
	{
		return toNick;
	}

	public void setToNick(String toNick)
	{
		this.toNick = toNick;
	}

//	public String getGroup()
//	{
//		return group;
//	}
//
//	public void setGroup(String group)
//	{
//		this.group = group;
//	}

	
}
