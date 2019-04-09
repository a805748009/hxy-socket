//package nafos.core.task;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//
///**
// * 任务处理队列.
// */
//public abstract class TaskQueue implements Runnable{
//	protected final ExecutorService threadPool;
//	/** 任务处理队列 */
//	protected final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
//
//
//
//	public TaskQueue(ExecutorService threadPool) {
//		this.threadPool = threadPool;
//        threadPool.execute(this);
//	}
//
//	/**
//	 * 往任务队列里提交一个任务。
//	 *
//	 * @param object 任务
//	 */
//	public void submit(Object object) {
//			queue.add(object);
//	}
//
//
//
//	@Override
//	public void run(){
//        for(;;){
//            try {
//                this.complete(queue.take());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 完成一个任务后续处理
//     */
//    public abstract void complete(Object object);
//}