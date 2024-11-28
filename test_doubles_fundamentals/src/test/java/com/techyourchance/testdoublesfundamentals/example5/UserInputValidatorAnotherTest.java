package com.techyourchance.testdoublesfundamentals.example5;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserInputValidatorAnotherTest {

    UserInputValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new UserInputValidator();
    }

    @Test
    public void isValidFullName_validFullName_trueReturned() {
        boolean result = SUT.isValidFullName("validFullName");
        assertTrue(result);
    }

    @Test
    public void isValidFullName_invalidFullName_falseReturned() {
        boolean result = SUT.isValidFullName("");
        assertFalse(result);
    }

    @Test
    public void isValidUserName_validUserName_trueReturned() {
        boolean result = SUT.isValidUsername("validUserName");
        assertTrue(result);
    }

    @Test
    public void isValidFullName_invalidUserName_falseReturned() {
        boolean result = SUT.isValidUsername("");
        assertFalse(result);
    }
}