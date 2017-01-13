/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:24:17
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.Objects;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午11:25:07
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class Node implements Comparable<Node>
{
	private String ip;
	private int port;

	public Node(String ip, int port)
	{
		super();

		this.setIp(ip);
		this.setPort(port);
	}

	@Override
	public int hashCode()
	{
		return (ip + ":" + port).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		Node other = (Node) obj;
		return ip.equals(other.getIp()) && port == other.getPort();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(ip).append(":").append(port);
		return builder.toString();
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException
	{

	}

	@Override
	public int compareTo(Node other)
	{
		if (other == null)
		{
			return -1;
		}
		//		RemoteNode other = (RemoteNode) obj;

		if (Objects.equals(ip, other.getIp()) && Objects.equals(port, other.getPort()))
		{
			return 0;
		} else
		{
			return this.toString().compareTo(other.toString());
		}
	}

}
