/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import com.talent.aio.examples.im.common.json.Json;

/**
 * 
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
public class AuthReqBody extends BaseReqBody
{
	/**
	 * device id(手机设备id)
	 */
	private String deviceId;

	/**
	 * token
	 */
	private String token;

	/**
	 * clienttype客户端类型1-pc 2-android 3-ios
	 */
	private byte deviceType;

	/**
	 * 渠道号
	 */
	private String cid;
	

	/**
	 * app版本号
	 */
	private String appVersion;

	/**
	 * 手机型号信息，譬如华为某型号
	 */
	private String deviceInfo;

	private Long seq = null;

	private String sign = null;

	/**
	 * 
	 */
	public AuthReqBody()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		AuthReqBody authReqBody = Json.toBean("{\"ddd\":2, \"deviceId\":\"ddddddddddd\"}", AuthReqBody.class);
		
		System.out.println(authReqBody);
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public byte getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(byte deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getCid()
	{
		return cid;
	}

	public void setCid(String cid)
	{
		this.cid = cid;
	}

	public String getAppVersion()
	{
		return appVersion;
	}

	public void setAppVersion(String appVersion)
	{
		this.appVersion = appVersion;
	}

	public String getDeviceInfo()
	{
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo)
	{
		this.deviceInfo = deviceInfo;
	}

	public Long getSeq()
	{
		return seq;
	}

	public void setSeq(Long seq)
	{
		this.seq = seq;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}
}
