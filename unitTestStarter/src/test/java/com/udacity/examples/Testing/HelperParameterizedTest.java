package com.udacity.examples.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class HelperParameterizedTest {

    private String input;
    private String output;



    public HelperParameterizedTest(String input, String output) {
        super();
        this.input = input;
        this.output = output;
    }

    @Parameters
    public static Collection initData(){
        String empName[][] = {{"Amol","Amol"},{"Amol","Amruta"}};
        return Arrays.asList(empName);
    }

    @Test
    public void verifyInputNameIsNotTheSameAsOutputName(){
        assertNotEquals(input,output);
    }

    //@RunWith
    //@SelectClasses(HelperParameterizedTest.class)
}
