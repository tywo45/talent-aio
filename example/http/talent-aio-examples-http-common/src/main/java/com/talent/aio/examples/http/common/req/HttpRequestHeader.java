/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-http-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年1月13日 下午5:20:13
 *
 * **************************************************************************
 */
package com.talent.aio.examples.http.common.req;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月13日 下午5:20:13
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月13日 | tanyaowu | 新建类
 *
 */
public class HttpRequestHeader
{

	public interface HeaderKey
	{
		public static final String ACCEPT = "Accept";
		public static final String ACCEPT_LANGUAGE = "Accept-Language";
		public static final String USER_AGENT = "User-Agent";
		public static final String ACCEPT_ENCODING = "Accept-Encoding";
		public static final String HOST = "Host";
		public static final String DNT = "DNT";
		public static final String CONNECTION = "Connection";
		public static final String COOKIE = "Cookie";
	}
	
	 
    private String method;  
    private String requestURL;  
    private String protocol;  
    private String agent;  
    private String host;  
    private int port;  
    private String encoding;  
    private String language;  
    private String  accept;  
    private String dnt;  
    private String connection;  
    private String cookie;  
      
      
    public void parseHead(ByteBuffer byteBuffer){  
//    	byteBuffer.getChar()
//        String s = null;  
//        try {  
//            s = br.readLine();//第一行  
//            String[] firstLine = s.split(" ");  
//            if(firstLine.length == 3){  
//                this.method = firstLine[0].trim();  
//                this.requestURL = firstLine[1].trim();  
//                this.protocol = firstLine[2].trim();  
//            }  
//            //继续读剩下的头信息  
//            s = br.readLine();  
//            while (s !=null) {  
//                String[] split = s.split(":");  
//                  
//                switch(split[0].trim()){  
//                case HeaderKey.ACCEPT:{  
//                    this.accept = split[1].trim();  
//                }  
//                case HeaderKey.ACCEPT_LANGUAGE :{  
//                    this.language = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.USER_AGENT :{  
//                    this.agent = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.ACCEPT_ENCODING :{  
//                    this.encoding = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.HOST :{  
//                    this.host = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.DNT :{  
//                    this.dnt = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.CONNECTION :{  
//                    this.connection = split[1].trim();  
//                    break;  
//                }  
//                case HeaderKey.COOKIE :{  
//                    this.cookie = split[1].trim();  
//                    break;  
//                }  
//                }  
//                s = br.readLine();  
//            }  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        }  
    }  
      
    public String getMethod() {  
        return method;  
    }  
    public void setMethod(String method) {  
        this.method = method;  
    }  
    public String getRequestURL() {  
        return requestURL;  
    }  
    public void setRequestURL(String requestURL) {  
        this.requestURL = requestURL;  
    }  
    public String getProtocol() {  
        return protocol;  
    }  
    public void setProtocol(String protocol) {  
        this.protocol = protocol;  
    }  
  
    public String getAgent() {  
        return agent;  
    }  
  
    public void setAgent(String agent) {  
        this.agent = agent;  
    }  
  
    public String getHost() {  
        return host;  
    }  
  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    public int getPort() {  
        return port;  
    }  
  
    public void setPort(int port) {  
        this.port = port;  
    }  
  
    public String getEncoding() {  
        return encoding;  
    }  
  
    public void setEncoding(String encoding) {  
        this.encoding = encoding;  
    }  
  
    public String getLanguage() {  
        return language;  
    }  
  
    public void setLanguage(String language) {  
        this.language = language;  
    }  
  
    public String getAccept() {  
        return accept;  
    }  
  
    public void setAccept(String accept) {  
        this.accept = accept;  
    }  
  
    public String getDnt() {  
        return dnt;  
    }  
  
    public void setDnt(String dnt) {  
        this.dnt = dnt;  
    }  
  
    public String getConnection() {  
        return connection;  
    }  
  
    public void setConnection(String connection) {  
        this.connection = connection;  
    }  
  
    public String getCookie() {  
        return cookie;  
    }  
  
    public void setCookie(String cookie) {  
        this.cookie = cookie;  
    }  

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月13日 下午5:20:13
	 * 
	 */
	public HttpRequestHeader()
	{

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月13日 下午5:20:13
	 * 
	 */
	public static void main(String[] args)
	{

	}

}
