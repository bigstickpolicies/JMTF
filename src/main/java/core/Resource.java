package core;

import com.google.common.base.Function;
import util.Null;

import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Resource implements Comparable<Resource>{
    private UUID id;
    private ReadWriteLock rwl=new ReentrantReadWriteLock(true);
    public Resource() {
        id =UUID.randomUUID();
    }

    @Override
    public int compareTo(Resource o) {
        return id.compareTo(o.id);
    }
    protected ReadWriteLock getLock() {
        return rwl;
    }
    void lockRead() {
        rwl.readLock().lock();
    }
    void lockWrite() {
        rwl.writeLock().lock();
    }
    void unlockRead() {
        rwl.readLock().unlock();
    }
    void unlockWrite() {
        rwl.writeLock().unlock();
    }

    public <U> ReturnValue<U> then(Function<TaskWrapper,U> run) {
        return LambdaTask.newTask((task) -> {
            task.requireRead(this);
            task.awaitAcquire();
            task.dropAll();
            return run.apply(task);
        }).start();
    }
    public ReturnValue<Null> then(Consumer<TaskWrapper> run) {
        return LambdaTask.newTask((task) -> {
            task.requireRead(this);
            task.awaitAcquire();
            task.dropAll();
            run.accept(task);
            return Null.get();
        }).start();
    }
}
