package com.talent.aio.examples.im.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-utils
 *
 * @author: tanyaowu 
 * @创建时间: 2013年5月10日 下午3:13:37
 *
 * **************************************************************************
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2013年5月10日 下午3:13:37
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2013年5月10日 | tanyaowu | 新建类
 *
 */
public class Md5
{

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    
    
    public static void main(String[] args)
	{
//    	String input = "1234567中文fgfdg";
//    	System.out.println(com.talent.utils.Md5.getMD5(input));
//    	System.out.println(sign(input, "", "utf-8"));
    	System.out.println(Integer.valueOf("1001001",2).toString());
    	 
    	
	}

	public static String getMD5(String input)
	{
		return sign(input, "", "utf-8");
	}

}
