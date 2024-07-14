package info.colinhan.mindmark.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MMToggleTest {

    @Test
    void parse_no_parameter() {
        var toggle = MMToggle.parse("AutoNumber");
        assertEquals("AutoNumber", toggle.getName());
        assertEquals(0, toggle.getParameters().size());
    }

    @Test
    void parse_one_parameter() {
        var toggle = MMToggle.parse("AutoNumber(1)");
        assertEquals("AutoNumber", toggle.getName());
        assertEquals(1, toggle.getParameters().size());
        assertEquals("1", toggle.getParameters().get(0));
    }

    @Test
    void parse_multiple_parameters() {
        var toggle = MMToggle.parse("Estimation(atEndOfTitle fixedDays \"︴ ⏰ (%s)\")");
        assertEquals(3, toggle.getParameters().size());
    }
}