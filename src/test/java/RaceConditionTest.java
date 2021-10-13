import core.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RaceConditionTest {
    @Test
    public void raceCondition() {
        JMTF jmtf=JMTF.get();
        jmtf.init();
        int num_resource=10000;
        int num_thread=3000;
        ObjectResource<IntWrapper>[] resources=new ObjectResource[num_resource];
        for(int i=0;i<num_resource;i++) {
            resources[i]=ObjectResource.get(new IntWrapper(0));
        }

        ReturnValue[] ends=new ReturnValue[num_thread];
        for(int i=0;i<num_thread;i++) {
            int start=i;
            ends[i]=LambdaTask.startTask((wrap) -> {
                for(int j=0;j<num_resource;j++) {
                    ObjectResource<IntWrapper> r=resources[(start+j)%num_resource];
                    wrap.requireWrite(r);
                    wrap.awaitAcquire();
                    r.get().increment();
                    wrap.drop(r);
                }
            });
        }
        ResourceArray.merge(ends).then((wrapper) -> {
            for(int i=0;i<num_resource;i++) {
                assertEquals(resources[i].get().get(),num_thread);
            }
            jmtf.shutdown();
        });
        jmtf.awaitTermination(true);



    }
    public class IntWrapper {
        int k;
        public IntWrapper(int k) {
            this.k=k;
        }
        public int get() {
            return k;
        }
        public void set(int k) {
            this.k=k;
        }
        public void increment() {
            this.k++;
        }
    }

}
