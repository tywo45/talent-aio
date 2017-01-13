package com.talent.aio.common.intf;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午9:12:18
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class Packet
{
//	/**
//	 * 低优先级
//	 */
//	public static final byte PRIORITY_LOW = 1;
//
//	/**
//	 * 高优先级
//	 */
//	public static final byte PRIORITY_HIGH = 2;

//	/**
//	 * 消息处理的发送的优先级
//	 */
//	private byte priority = 1;

	/**
	 * 同步发送时，需要的同步序列号
	 */
	private Integer synSeq = null;
//
//	/**
//	 * @return the priority
//	 */
//	public byte getPriority()
//	{
//		return priority;
//	}
//
//	/**
//	 * @param priority the priority to set
//	 */
//	public void setPriority(byte priority)
//	{
//		this.priority = priority;
//	}

	/**
	 * @return the synSeq
	 */
	public Integer getSynSeq()
	{
		return synSeq;
	}

	/**
	 * @param synSeq the synSeq to set
	 */
	public void setSynSeq(Integer synSeq)
	{
		this.synSeq = synSeq;
	}

}
