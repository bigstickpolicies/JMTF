package core;

public class ObjectResource<T> extends Value<T> {
    T inner;
    ObjectResource(T o) {
        inner=o;
    }
    public static <U> ObjectResource<U> get(U o) {
        return JMTF.get().getObjectResourceManager().getResource(o);
    }


    @Override
    public T get() {
        return inner;
    }
}