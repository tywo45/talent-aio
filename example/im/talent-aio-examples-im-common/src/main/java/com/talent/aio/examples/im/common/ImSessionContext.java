/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月21日 上午10:27:53
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月21日 上午10:27:53
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月21日 | tanyaowu | 新建类
 *
 */
public class ImSessionContext
{

	/**
	 * 是否已经握过手
	 */
	private boolean isHandshaked = false;
	
	/**
	 * 是否是走了websocket协议
	 */
	private boolean isWebsocket = false;
	
	
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月21日 上午10:27:54
	 * 
	 */
	public ImSessionContext()
	{
		
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月21日 上午10:27:54
	 * 
	 */
	public static void main(String[] args)
	{
		
	}



	/**
	 * @return the isWebsocket
	 */
	public boolean isWebsocket()
	{
		return isWebsocket;
	}

	/**
	 * @param isWebsocket the isWebsocket to set
	 */
	public void setWebsocket(boolean isWebsocket)
	{
		this.isWebsocket = isWebsocket;
	}

	/**
	 * @return the isHandshaked
	 */
	public boolean isHandshaked()
	{
		return isHandshaked;
	}

	/**
	 * @param isHandshaked the isHandshaked to set
	 */
	public void setHandshaked(boolean isHandshaked)
	{
		this.isHandshaked = isHandshaked;
	}

}
