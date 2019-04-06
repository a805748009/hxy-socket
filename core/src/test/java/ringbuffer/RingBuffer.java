package ringbuffer;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import nafos.bootStrap.handle.http.NsRespone;

import java.util.Arrays;

public class RingBuffer<T> {

    private final static int DEFAULT_SIZE  = 1024;
    private NsRespone[] buffer;
    private int head = 0;
    private int tail = 0;
    private int bufferSize;

    public RingBuffer(){
        new RingBuffer(DEFAULT_SIZE);
    }

    public RingBuffer(int initSize){
        this.bufferSize = initSize;
        this.buffer = new NsRespone[bufferSize];
        for (int i = 0; i < initSize; i++) {
            this.put(new NsRespone(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
        }
    }

    private Boolean empty() {
        return head == tail;
    }

    private Boolean full() {
        return (tail + 1) % bufferSize == head;
    }

    public void clear(){
        Arrays.fill(buffer,null);
        this.head = 0;
        this.tail = 0;
    }

    public Boolean put(NsRespone v) {
        if (full()) {
            return false;
        }
        buffer[tail] = v;
        tail = (tail + 1) % bufferSize;
        return true;
    }

    public NsRespone get() {
        if (empty()) {
            return null;
        }
        NsRespone result = buffer[head];
        head = (head + 1) % bufferSize;
        return result;
    }

    public NsRespone[] getAll() {
        if (empty()) {
            return new NsRespone[0];
        }
        int copyTail = tail;
        int cnt = head < copyTail ? copyTail - head : bufferSize - head + copyTail;
        NsRespone[] result = new NsRespone[cnt];
        if (head < copyTail) {
            for (int i = head; i < copyTail; i++) {
                result[i - head] = buffer[i];
            }
        } else {
            for (int i = head; i < bufferSize; i++) {
                result[i - head] = buffer[i];
            }
            for (int i = 0; i < copyTail; i++) {
                result[bufferSize - head + i] = buffer[i];
            }
        }
        head = copyTail;
        return result;
    }

}

