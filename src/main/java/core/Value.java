package core;

import com.google.common.base.Function;
import util.Null;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class Value<T> extends Resource{
    public abstract T get();

    public <U> ReturnValue<U> then(BiFunction<TaskWrapper,T,U> run) {
        return LambdaTask.newTask((task) -> {
            task.requireRead(this);
            task.awaitAcquire();
            task.dropAll();
            return run.apply(task,get());
        }).start();
    }
    public ReturnValue<Null> then(BiConsumer<TaskWrapper,T> run) {
        return LambdaTask.newTask((task) -> {
            task.requireRead(this);
            task.awaitAcquire();
            task.dropAll();
            run.accept(task,get());
            return Null.get();
        }).start();
    }

}
