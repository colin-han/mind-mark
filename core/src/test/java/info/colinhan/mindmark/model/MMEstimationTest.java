package info.colinhan.mindmark.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MMEstimationTest {

    @Test
    void testDaysToString() {
        assertEquals("0 hours", MMEstimation.day(0).toString());
        assertEquals("1 day", MMEstimation.day(1).toString());
        assertEquals("2 days", MMEstimation.day(2).toString());
    }

    @Test
    void test_hours_to_string() {
        assertEquals("0 hours", MMEstimation.hour(0).toString());
        assertEquals("1 hour", MMEstimation.hour(1).toString());
        assertEquals("2 hours", MMEstimation.hour(2).toString());
        assertEquals("1.5 hours", MMEstimation.hour(1.5).toString());
    }

    @Test
    void test_weeks_to_string() {
        assertEquals("0 hours", MMEstimation.week(0).toString());
        assertEquals("1 week", MMEstimation.week(1).toString());
        assertEquals("2 weeks", MMEstimation.week(2).toString());
    }

    @Test
    void test_complex_to_string() {
        assertEquals("1 day 1 hour", MMEstimation.hour(9).toString());
        assertEquals("1 day 2 hours", MMEstimation.day(1.25).toString());
        assertEquals("1 day 0.1 hours", MMEstimation.hour(8.1).toString());
    }

    @Test
    void parse() {
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3d"));
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3day"));
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3days"));
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3 d"));
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3 day"));
        assertEquals(MMEstimation.day(3), MMEstimation.parse("3  days"));
        assertEquals(MMEstimation.hour(3), MMEstimation.parse("3H"));
    }
}