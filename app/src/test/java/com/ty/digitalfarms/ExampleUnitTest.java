package com.ty.digitalfarms;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
           String s="山西省武乡县蟠龙镇";
        String[] split = s.split("省");
        System.out.println(split[0]+"---"+split[1]);

    }
}