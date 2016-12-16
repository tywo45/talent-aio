/**
 * 
 */
package com.talent.aio.examples.im.common.bs;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2013年8月10日 上午10:54:11
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2013年8月10日 | tanyaowu | 新建类
 */
@Getter
@Setter
@lombok.AllArgsConstructor
public class JoinNotifyRespBody extends com.talent.aio.examples.im.common.bs.BaseRespBody
{
	private String group;
	private Integer userid;
	private String nick;
	
	private Integer allcount;   //所有客户端数(包括注册和没注册的)
	private Integer usercount;   //注册用户数
	
	
	public JoinNotifyRespBody()
	{
	}

}
