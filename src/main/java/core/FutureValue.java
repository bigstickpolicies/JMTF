package core;


import java.util.concurrent.locks.Condition;
import java.util.logging.Level;

public class FutureValue<T> extends Value<T> {
    private boolean done=false;
    private Object readReady=new Object();
    T inner;
    public T get() {
        return inner;
    }
    
    void set(T value) {
        inner=value;
    }
    @Override
    public void lockRead() {
        //actually, uses the write lock
        synchronized(readReady) {
            if(done) {
                return;
            }
            try {
                readReady.wait();
            } catch (InterruptedException e) {
                JMTF.get().getSettings().getLogger().log(Level.SEVERE
                        ,"Thread interrupted while sleeping on read lock! This is a bug!");
            }
        }
    }
    @Override
    public void unlockRead() {
        //doesn't do anything
    }
    @Override
    public void lockWrite() {
        //a future value can only be written once
    }
    @Override
    public void unlockWrite() {
        //simply notify
        synchronized(readReady) {
            done=true;
            readReady.notifyAll();
        }
    }


}
