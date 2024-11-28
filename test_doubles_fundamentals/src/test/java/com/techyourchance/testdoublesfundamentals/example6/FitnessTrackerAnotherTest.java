package com.techyourchance.testdoublesfundamentals.example6;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FitnessTrackerAnotherTest {
    
    FitnessTracker SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FitnessTracker();
    }

    @Test
    public void step_totalIncreased() {
        SUT.step();
        int total = SUT.getTotalSteps();
        assertEquals(total, 1);
    }

    @Test
    public void runStep_totalIncreasedByCorrectRatio() {
        SUT.runStep();
        int total = SUT.getTotalSteps();
        assertEquals(total, 2);
    }
}