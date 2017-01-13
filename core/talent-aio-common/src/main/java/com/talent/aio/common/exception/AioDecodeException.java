/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午9:13:11
 *
 * **************************************************************************
 */
package com.talent.aio.common.exception;

/**
 * 解码异常
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午9:13:11
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class AioDecodeException extends java.lang.Throwable
{

	/**
	 * @含义: 
	 * @类型: long
	 */
	private static final long serialVersionUID = 5231789012657669073L;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:13:11
	 * 
	 */
	public AioDecodeException()
	{
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:14:09
	 * 
	 */
	public AioDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);

	}

	/**
	 * @param message
	 * @param cause
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:14:09
	 * 
	 */
	public AioDecodeException(String message, Throwable cause)
	{
		super(message, cause);

	}

	/**
	 * @param message
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:14:09
	 * 
	 */
	public AioDecodeException(String message)
	{
		super(message);

	}

	/**
	 * @param cause
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:14:09
	 * 
	 */
	public AioDecodeException(Throwable cause)
	{
		super(cause);

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午9:13:11
	 * 
	 */
	public static void main(String[] args)
	{

	}

}
