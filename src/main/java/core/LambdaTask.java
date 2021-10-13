package core;

import interfaces.ThrowingConsumer;
import interfaces.ThrowingFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaTask<T> extends Task<T> {
    private final ThrowingFunction<TaskWrapper,T> runs;
    private LambdaTask(@NotNull ThrowingFunction<TaskWrapper,T> r) {
        this.runs=r;
    }
    public static <U> LambdaTask<U> newTask(@NotNull ThrowingFunction<TaskWrapper,U> r) {
        return new LambdaTask<U>(r);
    }
    public static <U> LambdaTask<U> newTask(@NotNull ThrowingConsumer<TaskWrapper> r) {
        ThrowingFunction<TaskWrapper,U> func=(wrapper) -> {
            r.accept(wrapper);
            return null;
        };
        return new LambdaTask<U>(func);
    }
    public static <U> ReturnValue<U> startTask(@NotNull ThrowingFunction<TaskWrapper,U> r) {
        return (new LambdaTask<U>(r)).start();
    }
    public static <U> ReturnValue<U> startTask(@NotNull ThrowingConsumer<TaskWrapper> r) {
        ThrowingFunction<TaskWrapper,U> func=(wrapper) -> {
            r.accept(wrapper);
            return null;
        };
        return (new LambdaTask<U>(func)).start();
    }
    @Override
    protected T run() throws Exception,Error{
        TaskWrapper wrapper=new TaskWrapper(this);
        return runs.apply(wrapper);
    }

}
