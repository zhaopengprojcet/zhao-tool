package org.zhao.common.zmq.queue;

/**
 * 队列实现接口
 * @author zhao
 *
 */
public interface Zqueue<E> {

	/**
	 * 出队列 并返回出值
	 * @return
	 */
	E pop();
	
	/**
	 * 入队列
	 * @param model
	 * @return
	 */
	boolean flush(E model);
	
	/**
	 * 队列长度
	 * @return
	 */
	long size();
	
	/**
	 * 清空队列
	 * @return
	 */
	boolean clean();
}
