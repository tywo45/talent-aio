/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import com.talent.aio.examples.im.common.vo.JoinGroupResultVo;

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
public class JoinRespBody extends BaseRespBody
{
	public JoinRespBody()
	{
	}
	

	public JoinRespBody(String group, JoinGroupResultVo result)
	{
		this.group = group;
		this.result = result;
	}
	


	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public JoinGroupResultVo getResult()
	{
		return result;
	}


	public void setResult(JoinGroupResultVo result)
	{
		this.result = result;
	}

	private JoinGroupResultVo result;
	private String group;

}
