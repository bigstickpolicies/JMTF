package interfaces;

public interface ThrowingConsumer<U> {
    void accept(U arg) throws Exception,Error;
}
