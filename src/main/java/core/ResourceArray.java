package core;

public class ResourceArray extends Resource{
    private final Resource[] resources;
    public ResourceArray(Resource[] resources) {
        this.resources=resources;
    }
    @Override
    void lockRead() {
        for(int i=0;i<resources.length;i++) {
            resources[i].lockRead();
        }
    }
    @Override
    void lockWrite() {
        for(int i=0;i<resources.length;i++) {
            resources[i].lockWrite();
        }
    }
    @Override
    void unlockRead() {
        for(int i=0;i<resources.length;i++) {
            resources[i].unlockRead();
        }
    }
    @Override
    void unlockWrite() {
        for(int i=0;i<resources.length;i++) {
            resources[i].unlockWrite();
        }
    }
    public static ResourceArray merge(Resource... resources) {
        return new ResourceArray(resources);
    }
}