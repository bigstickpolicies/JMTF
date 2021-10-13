package core;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MultiValue<T> extends Value<T[]> {
    private Object[] inner;
    private Class<T> type;
    public MultiValue(Value<T>[] resources,Class<T> type) {
        inner= new Object[resources.length];
        for(int i=0;i<resources.length;i++) {
            inner[i]=resources[i];
        }
        this.type=type;
    }
    @Override
    void lockRead() {
        for(int i=0;i<inner.length;i++) {
            ((Value<T>) inner[i]).lockRead();
        }
    }
    @Override
    void lockWrite() {
        for(int i=0;i<inner.length;i++) {
            ((Value<T>) inner[i]).lockWrite();
        }
    }
    @Override
    void unlockRead() {
        for(int i=0;i<inner.length;i++) {
            ((Value<T>) inner[i]).unlockRead();
        }
    }
    @Override
    void unlockWrite() {
        for(int i=0;i<inner.length;i++) {
            ((Value<T>) inner[i]).unlockWrite();
        }
    }
    @Override
    public T[] get() {
        T[] result= (T[]) Array.newInstance(type,inner.length);
        for(int i=0;i<result.length;i++) {
            result[i]=((Value<T>) inner[i]).get();
        }
        return result;
    }
    public int getSize() {
        return inner.length;
    }
    public T get(int i) {
        return ((Value<T>) inner[i]).get();
    }
    public static <U> MultiValue<U> merge(Class<U> cls,Value<U>... resources) {
        return new MultiValue(resources,cls);
    }

}
