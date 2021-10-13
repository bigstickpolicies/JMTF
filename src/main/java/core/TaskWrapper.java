package core;

import interfaces.ThrowingConsumer;
import interfaces.ThrowingFunction;
import util.Null;

import java.util.function.Consumer;
import java.util.function.Function;

public class TaskWrapper {
    Task task;
    public TaskWrapper(Task task) {
        this.task=task;
    }
    public <U extends Resource> U requireRead(U r) {
        return (U) task.requireRead(r);
    }
    public <U extends Resource> U requireWrite(U r) {
        return (U) task.requireWrite(r);
    }
    public void awaitAcquire() {
        task.awaitAcquire();
    }
    public void release(Resource r) {
        task.release(r);
    }
    public void releaseAll() {
        task.releaseAll();
    }
    public void drop(Resource r) {
        task.drop(r);
    }
    public void dropAll() {
        task.dropAll();
    }
    public void sleep(int millis) {
        task.sleep(millis);
    }
    public <U> ReturnValue<U> async(Task<U> t) {
        return task.async(t);
    }
    public <U> ReturnValue<U> async(ThrowingFunction<TaskWrapper,U> fxn) {
        return task.async(fxn);
    }
    public ReturnValue<Null> async(ThrowingConsumer<TaskWrapper> con) {
        return task.async(con);
    }
}
