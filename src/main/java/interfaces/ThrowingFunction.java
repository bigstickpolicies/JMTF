package interfaces;

public interface ThrowingFunction<U,V> {
    V apply(U arg) throws Exception,Error;
}
