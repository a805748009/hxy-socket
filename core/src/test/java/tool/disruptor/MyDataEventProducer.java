//package tool.disruptor;
//
//
//import com.lmax.disruptor.RingBuffer;
//
//public class MyDataEventProducer {
//
//    private final RingBuffer<MyDataEvent> ringBuffer; // 敲黑板！ 很重要的知识点
//
//    public MyDataEventProducer(RingBuffer<MyDataEvent> ringBuffer) {
//        this.ringBuffer = ringBuffer;
//    }
//
//    /**
//     * 发布事件，每调用一次就发布一次事件
//     * 它的参数会通过事件传递给消费者
//     * @param byteBuffer 用 byteBuffer传参 是考虑到 Disruptor 是消息框架，而ByteBuffer又是读取时信道 (SocketChannel)最常用的缓冲区
//     */
//    public void publishData(long byteBuffer){
//        // RingBuffer 是一个圆环，.next() 方法是获取下一个索引值
//        long sequence = ringBuffer.next();
//        try {
//            // 通过索引值获取其对象
//            MyDataEvent myDataEvent = ringBuffer.get(sequence);
//            // 给数据单元赋值
//            myDataEvent.setValue(byteBuffer); // byteBuffer 的一个方法，文章中有链接
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 发布事件，其实就是发布索引 ，发布方法必须放在finally 中，避免出现阻塞情况。
//            ringBuffer.publish(sequence);
//        }
//    }
//
//}
