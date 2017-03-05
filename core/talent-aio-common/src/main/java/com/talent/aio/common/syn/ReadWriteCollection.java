/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月7日 下午1:27:02
 *
 * **************************************************************************
 */
package com.talent.aio.common.syn;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.talent.aio.common.ObjWithLock;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月7日 下午1:27:02
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月7日 | tanyaowu | 新建类
 *
 */
public class ReadWriteCollection<E> implements Collection<E>
{

	private ObjWithLock<Collection<E>> objWithLock = null;
	private Collection<E> collection = null;
	private ReentrantReadWriteLock readWriteLock = null;
	private WriteLock writeLock = null;
	private ReadLock readLock = null;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月7日 下午1:27:02
	 * 
	 */
	public ReadWriteCollection(Collection<E> collection)
	{
		if (collection == null)
		{
			throw new RuntimeException("collection 不允许为空");
		}
		this.collection = collection;
		this.objWithLock = new ObjWithLock<>(this.collection);
		this.readWriteLock = this.objWithLock.getLock();
		this.writeLock = this.readWriteLock.writeLock();
		this.readLock = this.readWriteLock.readLock();
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月7日 下午1:27:02
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.util.Collection#size()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/** 
	 * @see java.util.Collection#isEmpty()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#contains(java.lang.Object)
	 * 
	 * @param o
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean contains(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#iterator()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public Iterator<E> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * @see java.util.Collection#toArray()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public Object[] toArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 * 
	 * @param a
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * @see java.util.Collection#add(java.lang.Object)
	 * 
	 * @param e
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean add(E e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#remove(java.lang.Object)
	 * 
	 * @param o
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean remove(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 * 
	 * @param c
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 * 
	 * @param c
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 * 
	 * @param c
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 * 
	 * @param c
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * @see java.util.Collection#clear()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月8日 上午10:22:54
	 * 
	 */
	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		
	}

}
