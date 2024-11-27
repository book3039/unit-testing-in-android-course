package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {
    NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void test1() {
        boolean result = SUT.isNegative(-1);
        Assert.assertTrue(result);
    }

    @Test
    public void test2() {
        boolean result = SUT.isNegative(0);
        Assert.assertFalse(result);
    }

    @Test
    public void test3() {
        boolean result = SUT.isNegative(1);
        Assert.assertFalse(result);
    }
}