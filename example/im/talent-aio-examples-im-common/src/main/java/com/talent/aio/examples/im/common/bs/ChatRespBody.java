/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import com.talent.aio.examples.im.common.json.Json;

/**
 * 
 * @filename:	 com.talent.im.server.packet.request.JoinRespBody
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年4月3日 上午11:21:32
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年4月3日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ChatRespBody extends BaseRespBody
{

	private Integer type; //聊天类型
	private String text; //聊天内容

	private Integer fromId; //发送聊天消息的用户id
	private String fromNick; //发送聊天消息的用户nick

	private Integer toId; //目标用户id
	private String toNick; //目标用户nick
	private String group; //目标group

	/**
	 * 
	 * @param type 私聊还是公聊。从com.talent.im.common.Const.ChatType中取值
	 * @param text 聊天内容
	 * @param fromId
	 * @param fromNick
	 * @param toId
	 * @param toNick
	 * @param group
	 */
	public ChatRespBody(Integer type, String text, Integer fromId, String fromNick, Integer toId, String toNick, String group)
	{
		super();
		this.type = type;
		this.text = text;
		this.fromId = fromId;
		this.fromNick = fromNick;
		this.toId = toId;
		this.toNick = toNick;
		this.group = group;
	}

	public ChatRespBody()
	{
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

	public Integer getFromId()
	{
		return fromId;
	}

	public void setFromId(Integer fromId)
	{
		this.fromId = fromId;
	}

	public String getFromNick()
	{
		return fromNick;
	}

	public void setFromNick(String fromNick)
	{
		this.fromNick = fromNick;
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

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}
	
	public static void main(String[] args){
		ChatRespBody chatRespBody = new ChatRespBody();
		chatRespBody.setFromId(222);
		chatRespBody.setText("哈哈哈");
		chatRespBody.setFromNick("张三");
		chatRespBody.setGroup("5623555");
		System.out.println(Json.toJson(Json.toJson(chatRespBody)));
	}

}
