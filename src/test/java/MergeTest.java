import core.*;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MergeTest {
    @Test
    public void resourceArrayTest() {
        JMTF.get().init();
        AtomicInteger state=new AtomicInteger(0);
        ReturnValue<Integer> r1= LambdaTask.startTask((wrapper) -> {
            wrapper.sleep(500);
            state.incrementAndGet();
        });
        ReturnValue<Integer> r2=LambdaTask.startTask((wrapper) -> {
            wrapper.sleep(600);
            state.incrementAndGet();
        });
        ResourceArray res= ResourceArray.merge(r1,r2);
        LambdaTask.startTask((wrapper) -> {
            wrapper.requireRead(res);
            wrapper.awaitAcquire();
            assertTrue(state.get()==2);
            JMTF.get().shutdown();
        });
        JMTF.get().awaitTermination(true);

    }
    @Test
    public void multiValueTest() {
        JMTF jmtf=JMTF.get();
        jmtf.init();
        ReturnValue<Integer> r1=LambdaTask.startTask((wrapper) -> {
            wrapper.sleep(500);
            return 10;
        });
        ReturnValue<Integer> r2=LambdaTask.startTask((wrapper) -> {
            wrapper.sleep(600);
            return 20;
        });
        LambdaTask.startTask((wrapper) -> {
            MultiValue<Integer> mv=wrapper.requireRead(MultiValue.merge(Integer.class,r1,r2));
            wrapper.awaitAcquire();
            assertTrue(mv.get()[0]==10);
            assertTrue(mv.get()[1]==20);
            jmtf.shutdown();
        });
        jmtf.awaitTermination(true);
    }
}
