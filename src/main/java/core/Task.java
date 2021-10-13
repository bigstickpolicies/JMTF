package core;

import interfaces.ThrowingConsumer;
import interfaces.ThrowingFunction;
import util.Null;

import java.util.TreeMap;
import java.util.logging.Level;

abstract public class Task<T> {
    private TreeMap<Resource, Access> has;
    private TreeMap<Resource, Access> wants;
    private ReturnValue<T> returnValue;

    Object returnValueReadyLock;
    //run by calling thread
    public Task() {
        has=new TreeMap();
        wants=new TreeMap();
        returnValueReadyLock=new Object();
    }
    public ReturnValue<T> getReturnValue() {
        return returnValue;
    }
    public ReturnValue<T> start() {
        return JMTF.get().startTask(this);
    }


    //run by runner thread
    void wrapperRun(ReturnValue rv) {

        T value =null;
        returnValue=rv;

        try {
            value=run();
        }catch(Exception e) {
            e.printStackTrace();
        }catch(Error e) {
            //e.printStackTrace();
            JMTF.get().getSettings().getLogger().log(Level.SEVERE,"Fatal error in task! Shutting down");
            JMTF.get().shutdown(e);

            releaseAll();
            returnValue.set(value);
            returnValue.unlockWrite();

            throw e;
        }
        finally {
            releaseAll();
            returnValue.set(value);
            returnValue.unlockWrite();

        }
    }
    protected abstract T run() throws Exception,Error;
    protected <U extends Resource> U requireRead(U r) {
        wants.put(r, Access.READ);
        return r;
    }
    protected <U> ObjectResource<U> getObjectResource(U obj) {
        return JMTF.get().getObjectResourceManager().getResource(obj);
    }
    protected <U extends Resource> U requireWrite(U r) {
        wants.put(r, Access.WRITE);
        return r;
    }
    protected void drop(Resource r) {
        if(!Access.isPersistent(wants.get(r))) {
            wants.remove(r);
        }
    }
    protected void dropAll() {
        for(Resource r:wants.keySet()) {
            drop(r);
        }
    }
    protected void awaitAcquire() {
        releaseAll();
        for(Resource r: wants.keySet()) {
            if(wants.get(r) == Access.READ) {
                r.lockRead();
            }
            if(wants.get(r) == Access.WRITE) {
                r.lockWrite();
            }
        }
        has = wants;
        wants=(TreeMap) wants.clone();
    }
    protected void release(Resource r) {

        if(Access.isRead(has.get(r))) {
            r.unlockRead();
        }
        if(Access.isWrite(has.get(r))) {
            r.unlockWrite();
        }
        if(!Access.isPersistent(has.get(r))) {
            wants.put(r,has.get(r));
        }
        has.remove(r);
    }
    protected void releaseAll() {

        for(Resource r: has.keySet()) {
            if(Access.isRead(has.get(r))) {
                r.unlockRead();
            }
            if(Access.isWrite(has.get(r))) {
                r.unlockWrite();
            }

        }
        has=new TreeMap();
    }
    protected <U> ReturnValue<U> async(Task<U> task) {
        return task.start();
    }
    protected <U> ReturnValue<U> async(ThrowingFunction<TaskWrapper,U> fxn) {
        return LambdaTask.startTask(fxn);
    }
    protected ReturnValue<Null> async(ThrowingConsumer<TaskWrapper> fxn) {
        return LambdaTask.startTask(fxn);
    }
    protected void sleep(int millis) {
        releaseAll();
        for(Resource r: has.keySet()) {
            if(Access.isRead(has.get(r))) {
                r.lockRead();
            }
            if(Access.isWrite(Access.WRITE)) {
                r.lockWrite();
            }
        }

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Resource r: has.keySet()) {
            if(Access.isRead(has.get(r))) {
                r.unlockRead();
            }
            if(Access.isWrite(Access.WRITE)) {
                r.unlockWrite();
            }
        }
    }

}
