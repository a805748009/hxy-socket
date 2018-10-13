package nafos.core.task;

import nafos.core.entry.AsyncTaskMode;
import nafos.core.util.ObjectUtil;
import nafos.core.util.SpringApplicationContextHolder;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

/**
 * 任务处理队列.
 */
public class TaskQueue {
	protected final ExecutorService threadPool;
	/** 任务处理队列 */
	protected LinkedList<Object> queue;

	public TaskQueue(ExecutorService threadPool) {
		this.threadPool = threadPool;
		this.queue = new LinkedList<>();
	}

	/**
	 * 往任务队列里提交一个任务。
	 * 
	 * @param task 任务
	 */
	public void submit(Runnable task) {
		synchronized (this) {
			queue.add(task);
			// 只有一个任务，那就是刚刚加的，直接开始执行...
			if (queue.size() == 1) {
				threadPool.execute(task);
			}
		}
	}



	/**
	 * 完成一个任务后续处理
	 */
	public void complete() {
		synchronized (this) {
			// 移除已经完成的任务。
			queue.removeFirst();

			// 完成一个任务后，如果还有任务，则继续执行。
			if (!queue.isEmpty()) {
				this.threadPool.submit((Runnable) queue.getFirst());
			}
		}
	}
}