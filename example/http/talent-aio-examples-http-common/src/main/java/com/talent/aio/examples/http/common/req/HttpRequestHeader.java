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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String version;
	
	private String protocol;
	private String agent;
	private String host;
	private int port;
	private String encoding;
	private String language;
	private String accept;
	private String dnt;
	private String connection;
	private String cookie;

	private Map<String, String> headers = new HashMap<>();
	protected boolean header_complete;
	protected List<String> headerLines;
	protected StringBuilder currentHeaderLine;

	
	protected void parseFirstLine(String line) {
		int index1 = line.indexOf(' ');
		this.method = line.substring(0, index1);
		int index2 = line.indexOf(' ',index1+1);
		this.setRequestURL(line.substring(index1 + 1, index2));
		this.setVersion(line.substring(index2+1));
	}
	
	private void readHeader(ByteBuffer buffer) throws IOException
	{
		while (buffer.hasRemaining())
		{

			//			if (++headerLength > headerLimit)
			//			{
			//				throw new IOException("max http header length " + headerLimit);
			//			}

			byte b = buffer.get();

			if (b == '\n')
			{
				if (currentHeaderLine.length() == 0)
				{
					header_complete = true;
					break;
				} else
				{
					headerLines.add(currentHeaderLine.toString());
					currentHeaderLine.setLength(0);
				}
				continue;
			} else if (b == '\r')
			{
				continue;
			} else
			{
				currentHeaderLine.append((char) b);
			}
		}
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getRequestURL()
	{
		return requestURL;
	}

	public void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public String getAgent()
	{
		return agent;
	}

	public void setAgent(String agent)
	{
		this.agent = agent;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getAccept()
	{
		return accept;
	}

	public void setAccept(String accept)
	{
		this.accept = accept;
	}

	public String getDnt()
	{
		return dnt;
	}

	public void setDnt(String dnt)
	{
		this.dnt = dnt;
	}

	public String getConnection()
	{
		return connection;
	}

	public void setConnection(String connection)
	{
		this.connection = connection;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
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

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

}
