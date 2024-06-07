package info.colinhan.mindmark.util;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMEstimationUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstimationCalculatorTest {

    @Test
    void one_day_add_one_day() {
        assertEquals(
                MMEstimation.day(2),
                EstimationCalculator.add(
                        MMEstimation.day(1),
                        MMEstimation.day(1)
                )
        );
    }

    @Test
    void one_day_add_one_hour() {
        assertEquals(
                MMEstimation.hour(9),
                EstimationCalculator.add(
                        MMEstimation.day(1),
                        MMEstimation.hour(1)
                )
        );
    }
}