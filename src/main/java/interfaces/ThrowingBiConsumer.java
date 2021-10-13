package interfaces;

public interface ThrowingBiConsumer<U,V> {
    void accept(U arg1,V arg2) throws Exception,Error;
}
