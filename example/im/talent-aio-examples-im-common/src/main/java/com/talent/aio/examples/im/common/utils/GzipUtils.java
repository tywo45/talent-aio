/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年1月31日 上午11:22:48
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月31日 上午11:22:48
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月31日 | tanyaowu | 新建类
 *
 */
public class GzipUtils
{
	private static Logger log = LoggerFactory.getLogger(GzipUtils.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月31日 上午11:22:48
	 * 
	 */
	private GzipUtils()
	{

	}

	/***
	 * 解压GZip
	 * 
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	public static byte[] unGZip(byte[] data) throws IOException
	{
		byte[] ret = null;
		ByteArrayInputStream bis = null;
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
		try
		{
			bis = new ByteArrayInputStream(data);
			gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1)
			{
				baos.write(buf, 0, num);
			}
			ret = baos.toByteArray();
			baos.flush();
		} finally
		{
			IOUtils.closeQuietly(baos);
			IOUtils.closeQuietly(gzip);
			IOUtils.closeQuietly(bis);
		}
		return ret;
	}

	public static byte[] gZip(byte[] data) throws IOException
	{
		byte[] ret = null;
		ByteArrayOutputStream bos = null;
		GZIPOutputStream gzip = null;
		try
		{
			bos = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(bos);
			gzip.write(data);
			gzip.finish();
			ret = bos.toByteArray();
		} finally
		{
			IOUtils.closeQuietly(gzip);
			IOUtils.closeQuietly(bos);
		}
		return ret;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月31日 上午11:22:48
	 * 
	 */
	public static void main(String[] args)
	{

	}

}
