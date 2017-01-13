/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 下午1:31:57
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 下午1:31:57
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public interface Const
{
	String authkey = "fdsfeofa";
	
	public static interface ChatType
	{
		/**
		 * 公聊
		 */
		public static Integer pub = 1;
		/**
		 * 私聊
		 */
		public static Integer pri = 2;
	}
}
