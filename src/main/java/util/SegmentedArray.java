package util;

import core.*;
import interfaces.ThrowingBiConsumer;
import interfaces.ThrowingBiFunction;
import interfaces.ThrowingConsumer;
import interfaces.ThrowingFunction;

import java.lang.reflect.Array;

public class SegmentedArray<T> {
    private T[] array;
    private SegmentResource[] segments;
    private int partSize;
    public SegmentedArray(Class<T> cls, int length, int partSize) {
        this.partSize=partSize;
        array= (T[]) Array.newInstance(cls,length);
        segments=new SegmentResource[(array.length-1)/partSize+1];
        for(int i=0;i<segments.length;i++) {
            segments[i]=new SegmentResource();
        }
    }
    public SegmentResource getLock(int index) {
        return segments[(index-1)/partSize];
    }
    public SegmentResource[] getLocks() {
        return segments;
    }
    public T[] get() {
        return array;
    }
    public T get(int index) {
        return array[index];
    }
    public int getPartitionSize() {
        return partSize;
    }
    public MultiValue batchForEach(ThrowingConsumer<T> action) {
        ReturnValue[] returns=new ReturnValue[segments.length];
        for(int i=0;i<segments.length;i++) {

            int finalI = i;
            returns[i]=
            LambdaTask.startTask((t) -> {
                for(int j = finalI *partSize;j<Math.min((finalI+1)*partSize,array.length);j++) {
                    action.accept(array[j]);
                }
                return null;
            });
        }
        return new MultiValue(returns,ReturnValue.class);
    }
    public MultiValue batchForEach(ThrowingBiConsumer<T,Integer> action) {
        ReturnValue[] returns=new ReturnValue[segments.length];
        for(int i=0;i<segments.length;i++) {

            int finalI = i;
            returns[i]=
                    LambdaTask.startTask((t) -> {
                        for(int j = finalI *partSize;j<Math.min((finalI+1)*partSize,array.length);j++) {
                            action.accept(array[j],j);
                        }
                        return null;
                    });
        }
        return new MultiValue(returns,ReturnValue.class);
    }

    public MultiValue batchForEach(ThrowingFunction<T,T> action) {
        ReturnValue[] returns=new ReturnValue[segments.length];
        for(int i=0;i<segments.length;i++) {

            int finalI = i;
            returns[i]=
                    LambdaTask.startTask((t) -> {
                        for(int j = finalI *partSize;j<Math.min((finalI+1)*partSize,array.length);j++) {
                            array[j]=action.apply(array[j]);
                        }
                        return null;
                    });
        }
        return new MultiValue(returns,ReturnValue.class);
    }

    public MultiValue batchForEach(ThrowingBiFunction<T,Integer,T> action) {
        ReturnValue[] returns=new ReturnValue[segments.length];
        for(int i=0;i<segments.length;i++) {

            int finalI = i;
            returns[i]=
                    LambdaTask.startTask((t) -> {
                        for(int j = finalI *partSize;j<Math.min((finalI+1)*partSize,array.length);j++) {
                            array[j]=action.apply(array[j],j);
                        }
                        return null;
                    });
        }
        return new MultiValue(returns,ReturnValue.class);
    }

}
