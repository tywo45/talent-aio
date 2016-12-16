/**
 * 
 */
package com.talent.aio.examples.im.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @filename:	 com.talent.im.server.Command
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年3月20日 下午4:44:50
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年3月20日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class Command
{

	public static final String IM_HANDLER_BEAN_PREFIX_SERVER = "im-server-";
	public static final String IM_HANDLER_BEAN_PREFIX_CLIENT = "im-client-";

	public static final Map<Short, String> COMMAND_DESC = new HashMap<>();

	public static final short START_INDEXOF_INNERCOMMAND = 20000;

//	private static final int STEP = 1000;

	/**
	 * 鉴权请求
	 */
	public static final short AUTH_REQ = 1;
	/**
	 * 鉴权响应
	 */
	public static final short AUTH_RESP = 1001;

	
	/**
	 * 请求进入房间
	 */
	public static final short JOIN_GROUP_REQ = 2;
	/**
	 * 进入房间后，服务器发给该客户端的响应的响应
	 */
	public static final short JOIN_GROUP_RESP = 1002;
	/**
	 * 进入房间通知，成员进入房间后，要向其它成员发送通知
	 */
	public static final short JOIN_GROUP_NOTIFY_RESP = 1003;

	/**
	 * 聊天请求
	 */
	public static final short CHAT_REQ = 4;
	/**
	 * 聊天响应
	 */
	public static final short CHAT_RESP = 1004;
	
	
	/**
	 * 开播请求
	 */
	public static final short STARTSHOW_REQ = 5;
	/**
	 * 开播响应
	 */
	public static final short STARTSHOW_RESP = 1005;
	
	/**
	 * 停播请求
	 */
	public static final short ENDSHOW_REQ = 6;
	/**
	 * 停播通知
	 */
	public static final short ENDSHOW_NOTIFY_RESP = 1006;

	
	/**
	 * 心跳请求
	 */
	public static final short HEARTBEAT_REQ = 9999;
	

	public static final Set<Short> PRIORITY_HANDLER_COMMANDS = new HashSet<>();

	static
	{
		PRIORITY_HANDLER_COMMANDS.add(AUTH_REQ);
		PRIORITY_HANDLER_COMMANDS.add(AUTH_RESP);
		
		PRIORITY_HANDLER_COMMANDS.add(JOIN_GROUP_REQ);
		PRIORITY_HANDLER_COMMANDS.add(JOIN_GROUP_RESP);
		
		PRIORITY_HANDLER_COMMANDS.add(STARTSHOW_REQ);
		PRIORITY_HANDLER_COMMANDS.add(STARTSHOW_RESP);

		COMMAND_DESC.put(AUTH_REQ, "鉴权");
		COMMAND_DESC.put(AUTH_RESP, "鉴权响应");

		COMMAND_DESC.put(JOIN_GROUP_REQ, "进入房间");
		COMMAND_DESC.put(JOIN_GROUP_RESP, "进入房间响应");

		COMMAND_DESC.put(HEARTBEAT_REQ, "心跳");
		COMMAND_DESC.put(CHAT_REQ, "聊天");
		COMMAND_DESC.put(CHAT_RESP, "聊天响应");

	}

	/**
	 * 
	 */
	public Command()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}
}
