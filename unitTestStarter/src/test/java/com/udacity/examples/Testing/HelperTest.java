package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertEquals;



public class HelperTest {

    @Ignore
	@Test
    public void Test(){
        assertEquals(3,3);
    }

    @Test
    public void verifyGetCount(){
	    List<String> empNames = Arrays.asList("Sareeta","Amol");
	    long actual = Helper.getCount(empNames);
	    assertEquals(2,actual);
    }

    @Test
    public void verifyGetStats(){
        List<Integer> yrsOfExperience = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yrsOfExperience);
        assertEquals(stats.getMax(),19);
    }

    @Test
    public void verifyMergedList(){
        List<String> empNames = Arrays.asList("sareeta", "", "john","");
        assertEquals("sareeta, john", Helper.getMergedList(empNames));
    }

    @Before
    public void init(){
        System.out.println("Before each test");
    }

    @BeforeClass
    public static void setup(){
        System.out.println("Before class");
    }

    @AfterClass
    public static void tearDown(){
        System.out.println("After class");
    }

    @After
    public void initEnd(){
        System.out.println("After each test");
    }


}
