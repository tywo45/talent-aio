/**
 * 
 */
package com.talent.aio.examples.im.common.vo;

/**
 * 
 * @filename:	 com.talent.im.server.vo.JoinGroupResultVo
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年3月23日 下午1:41:39
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
* 	<tr><td>2013年3月23日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class JoinGroupResultVo
{

	public static interface Code
	{
		/**
		 * 允许进入
		 */
		public static final int OK = 1;
		/**
		 * 组不存在
		 */
		public static final int NOT_EXIST = 2;

		/**
		 * 组满
		 */
		public static final int GROUP_FULL = 3;
		/**
		 * 在黑名单中
		 */
		public static final int IN_BACKLIST = 4;
		/**
		 * 被踢
		 */
		public static final int TAKEOUTED = 5;
		/**
		 * 不允许进入，原因为其它
		 */
		public static final int OTHER = 99;
	}

	private Integer code;

	private String msg;

	/**
	 * 
	 */
	public JoinGroupResultVo()
	{

	}

	public JoinGroupResultVo(Integer code, String msg)
	{
		super();
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
