/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午10:19:00
 *
 * **************************************************************************
 */
package com.talent.aio.common.intf;

import java.nio.ByteBuffer;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;


/**
 * The Interface AioHandler.
 *
 * @param <Ext> the generic type
 * @param <P> the generic type
 * @param <R> the generic type
 */
public interface AioHandler <Ext, P extends Packet, R>
{
	
	/**
	 * Handler.
	 *
	 * @param packet the packet
	 * @return the r
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:38:52
	 */
	R handler(P packet, ChannelContext<Ext, P, R> channelContext) throws Exception;
	
	/**
	 * Encode.
	 *
	 * @param packet the packet
	 * @return the byte buffer
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:38:52
	 */
	ByteBuffer encode(P packet, ChannelContext<Ext, P, R> channelContext);
	
	/**
	 * 根据ByteBuffer解码成业务需要的Packet对象.
	 *
	 * @param buffer the buffer
	 * @return the t
	 * @throws AioDecodeException the aio decode exception
	 */
	P decode(ByteBuffer buffer, ChannelContext<Ext, P, R> channelContext) throws AioDecodeException;
	
	/**
	 * 连接关闭时触发本方法，业务层作一些释放资源等操作，记录日志等.
	 *
	 * @param channelContext the channelcontext
	 * @param throwable the throwable 有可能为空
	 * @param remark the remark 有可能为空
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午9:17:55
	 */
	void onClose(ChannelContext<Ext, P, R> channelContext, Throwable throwable, String remark);
}
