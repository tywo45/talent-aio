/**
 * 
 */
package com.talent.aio.common.threadpool.intf;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * The Interface SynRunnableIntf.
 *
 * @filename:  com.talent.threadpool.intf.SynRunnableIntf
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2012-5-18 下午2:55:50
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2012-5-18</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public interface SynRunnableIntf extends Runnable
{

	public ReadWriteLock runningLock();

	public boolean isNeededExecute();

	public boolean isCanceled();

	public void setCanceled(boolean isCanceled);

	/**
	 * Run task.
	 */
	public void runTask();

}
