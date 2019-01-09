package org.zhao.common.zmq.queue.achieve;

import java.util.Arrays;

import org.zhao.common.zmq.queue.Zqueue;

/**
 * 先进先出队列
 * @author zhao
 *
 * @param <E>
 */
public class FifoQueue<E> implements Zqueue<E> {

	private Object[] datas ;
	private int size = 0;
	private final int DEFAULT_SIZE = 10;
	
	public FifoQueue() {
		checkNull();
	}
	
	/**
	 * 顺序取  从下标0开始
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E pop() {
		if(datas == null) return null;
		if(size < 1) return null;
		Object model = null;
		synchronized (datas) {
			model = datas[0];
			System.arraycopy(datas, 1, datas, 0,
	                size - 1);
			datas[--size] = null;
		}
		
		return (E) model;
	}

	/**
	 * 顺序加入 从下标0开始
	 */
	@Override
	public boolean flush(E model) {
		synchronized (datas) {
			checkNull();
			checkDataSize();
			datas[size++] = model;
		}
		return true;
	}

	@Override
	public long size() {
		return size;
	}

	@Override
	public boolean clean() {
		datas = null;
		size = 0;
		return true;
	}

	/**
	 * 检查数据长度是否满足继续添加新元素
	 */
	private void checkDataSize() {
		if(datas.length == size) {
			int newSize = size + size / 2 ;
			datas = Arrays.copyOf(datas, newSize);
			return;
		}
	}
	
	/**
	 * 检查数据空
	 */
	private void checkNull() {
		if(datas == null) {
			datas = new Object[this.DEFAULT_SIZE];
			size = 0;
		}
	}
}
