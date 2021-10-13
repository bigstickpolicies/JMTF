package interfaces;

public interface ThrowingBiFunction<T,U,V> {
    V apply(T arg1,U arg2) throws Exception,Error;
}
