package com.techyourchance.unittestingfundamentals.exercise3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }

    // interval1 is before interval2
    @Test
    public void isAdjacent_interval1BeforeInterval2_false() {
        Interval interval1 = new Interval(-4, -2);
        Interval interval2 = new Interval(2, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    // interval1 is adjacent to interval2 on start
    @Test
    public void isAdjacent_interval1IsAdjacentInterval2OnStart_true() {
        Interval interval1 = new Interval(-4, 2);
        Interval interval2 = new Interval(2, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertTrue(result);
    }

    // interval1 overlaps interval2 on start
    @Test
    public void isAdjacent_interval1OverlapsInterval2OnStart_false() {
        Interval interval1 = new Interval(-4, 2);
        Interval interval2 = new Interval(0, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    // interval1 is adjacent to interval2 on end
    @Test
    public void isAdjacent_interval1IsAdjacentInterval2OnEnd_true() {
        Interval interval1 = new Interval(4, 8);
        Interval interval2 = new Interval(2, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertTrue(result);
    }
    // interval2 overlaps interval2 on end
    @Test
    public void isAdjacent_interval1OverlapsInterval2OnEnd_false() {
        Interval interval1 = new Interval(2, 6);
        Interval interval2 = new Interval(0, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    // interval1 is after interval2
    @Test
    public void isAdjacent_interval1AfterInterval2_false() {
        Interval interval1 = new Interval(8, 10);
        Interval interval2 = new Interval(2, 4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }
    // interval1 is contained within interval2
    @Test
    public void isAdjacent_interval1IsContainedWithinInterval2_false() {
        Interval interval1 = new Interval(3, 5);
        Interval interval2 = new Interval(0, 10);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    // interval1 contains interval2
    @Test
    public void isAdjacent_interval1ContainsInterval2_false() {
        Interval interval1 = new Interval(0, 10);
        Interval interval2 = new Interval(3, 5);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }
}