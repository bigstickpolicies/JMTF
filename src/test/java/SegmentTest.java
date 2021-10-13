import core.JMTF;
import org.junit.Test;
import util.SegmentedArray;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class SegmentTest {
    @Test
    public void segmentTest() {
        JMTF jmtf=JMTF.get();
        jmtf.init();
        SegmentedArray<Integer> nums=jmtf.newSegmentedArray(Integer.class,1000,20);
        nums.batchForEach((val,index) -> {
            return index;
        }).then((wrapper) -> {
            nums.batchForEach((val) -> {
                return val*2;
            });
        }).then((wrapper) -> {
            Integer[] ints=nums.get();
            for(int i=0;i<ints.length;i++) {
                assertTrue(ints[i]==2*i);
            }
            jmtf.shutdown();
        });
        jmtf.awaitTermination(true);
    }
    public static void main(String[] args) {
        (new SegmentTest()).segmentTest();
    }
}
