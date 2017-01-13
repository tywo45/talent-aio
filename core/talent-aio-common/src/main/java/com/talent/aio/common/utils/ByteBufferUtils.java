/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午3:11:09
 *
 * **************************************************************************
 */
package com.talent.aio.common.utils;

import java.nio.ByteBuffer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午3:11:09
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class ByteBufferUtils
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午3:11:09
	 * 
	 */
	public ByteBufferUtils()
	{

	}

	/**
	 * 
	 * @param byteBuffer1
	 * @param byteBuffer2
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午9:46:57
	 *
	 */
	public static ByteBuffer composite(ByteBuffer byteBuffer1, ByteBuffer byteBuffer2)
	{
		int capacity = (byteBuffer1.limit() - byteBuffer1.position()) + (byteBuffer2.limit() - byteBuffer2.position());
		ByteBuffer ret = ByteBuffer.allocate(capacity);
		//		byteBuffer1.position(0);
		//		byteBuffer2.position(0);

		ret.put(byteBuffer1);
		ret.put(byteBuffer2);

		ret.position(0);
		ret.limit(ret.capacity());
		return ret;
	}

	/**
	 * 
	 * @param src
	 * @param startindex 从0开始
	 * @param endindex 
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午9:47:01
	 *
	 */
	public static ByteBuffer copy(ByteBuffer src, int startindex, int endindex)
	{
		int size = endindex - startindex;
		ByteBuffer ret = ByteBuffer.allocate(size);
		src.position(startindex);
		for (int i = 0; i < size; i++)
		{
			ret.put(src.get());
		}
		return ret;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午3:11:09
	 * 
	 */
	public static void main(String[] args)
	{

	}

}
