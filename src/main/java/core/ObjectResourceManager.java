package core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ObjectResourceManager {
    private Cache<Object,ObjectResource> cache;
    public ObjectResourceManager() {
        cache = CacheBuilder.newBuilder().weakKeys().build();
    }
    public <T> ObjectResource<T> getResource(T o) {
        //returns null if no object.
        ObjectResource r=cache.getIfPresent(o);
        if(r!=null) {
            return r;
        }
        r=new ObjectResource<T>(o);
        cache.put(o,r);
        return r;
    }
}
