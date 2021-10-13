import core.*;
import interfaces.ThrowingFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {
    int fib(int k) {
        if(k==0) return 1;
        if(k==1) return 1;
        return fib(k-1)+fib(k-2);
    }
    ThrowingFunction<TaskWrapper,Integer> getFunc(int i) {
        return (wrapper) -> {
            if(i<35) return fib(i);
            ReturnValue<Integer> r1=LambdaTask.startTask(getFunc(i-1));
            ReturnValue<Integer> r2=LambdaTask.startTask(getFunc(i-2));
            wrapper.requireRead(r1);
            wrapper.requireRead(r2);
            wrapper.awaitAcquire();
            return r1.get()+r2.get();
        };
    }
    @Test
    public void fibtest() {
        JMTF jmtf=JMTF.get();
        jmtf.init();

        int depth=43;
        LambdaTask.startTask(getFunc(depth)).then((wrapper,value) -> {
            System.out.println("Value:"+value);
            assertEquals(Integer.valueOf(701408733),value);

            jmtf.shutdown();
        });
        jmtf.awaitTermination(true);
    }
    public static void main(String[] args) {
        (new FibonacciTest()).fibtest();
    }
}
