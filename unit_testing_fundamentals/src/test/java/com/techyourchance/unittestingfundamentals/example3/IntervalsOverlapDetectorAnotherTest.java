package com.techyourchance.unittestingfundamentals.example3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IntervalsOverlapDetectorAnotherTest {

    IntervalsOverlapDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsOverlapDetector();
    }

    // interval1 is before interval2
    @Test
    public void isOverlap_interval1BeforeInterval2_falseReturned() {
        Interval interval1 = new Interval(-5, -1);
        Interval interval2 = new Interval(0, 5);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }

    // interval1 overlaps interval2 on start
    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        Interval interval1 = new Interval(-5, 5);
        Interval interval2 = new Interval(3, 7);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    // interval1 is contained within interval2
    @Test
    public void isOverlap_interval1ContainedWithinInterval2_trueReturned() {
        Interval interval1 = new Interval(1, 2);
        Interval interval2 = new Interval(0, 4);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    // interval1 contains interval2
    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned() {
        Interval interval1 = new Interval(0, 10);
        Interval interval2 = new Interval(3, 5);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    // interval1 overlaps interval2 on end
    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        Interval interval1 = new Interval(7, 10);
        Interval interval2 = new Interval(3, 9);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    // interval1 is after interval2
    @Test
    public void isOverlap_interval1AfterInterval2_falseReturned() {
        Interval interval1 = new Interval(7, 10);
        Interval interval2 = new Interval(3, 4);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }

    @Test
    public void isOverlap_interval1BeforeAdjacentInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(5, 8);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }

    @Test
    public void isOverlap_interval1AfterAdjacentInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-3, -1);

        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }
}