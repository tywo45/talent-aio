/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月1日 上午10:31:55
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client.ui.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.ImSessionContext;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月1日 上午10:31:55
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月1日 | tanyaowu | 新建类
 *
 */
public class ImListCellRenderer extends DefaultListCellRenderer
{
	private Color okColor;
	private Color warnColor;

	//#5cb85c OK
	//#f0ad4e warn
	public ImListCellRenderer(Color okColor, Color warnColor)
	{
		this.okColor = okColor;
		this.warnColor = warnColor;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		ClientChannelContext<ImSessionContext, ImPacket, Object> channelContext = (ClientChannelContext<ImSessionContext, ImPacket, Object>) value;
		super.getListCellRendererComponent(list, channelContext, index, isSelected, cellHasFocus);

		String text = channelContext.getServerNode() + "<--" + channelContext.getClientNode();
		setText(text);

		if (channelContext.isClosed())
		{
			//			Font font = new Font("宋体", Font.PLAIN, 36);
			setForeground(warnColor);
			//			setBackground(this.warnColor);
		} else
		{
			//			Font font = new Font("宋体", Font.PLAIN, 36);
			setForeground(okColor);
			//			setBackground(this.okColor);
		}

		return this;
	}
}
