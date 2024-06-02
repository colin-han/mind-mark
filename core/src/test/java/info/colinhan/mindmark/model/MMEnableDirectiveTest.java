package info.colinhan.mindmark.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MMEnableDirectiveTest {
    @Test
    void parse_with_one_toggle() {
        var directive = MMEnableDirective.parse("AutoNumber");
        assertEquals(1, directive.getToggles().size());
        assertEquals("AutoNumber", directive.getToggles().get(0).getName());
        assertEquals(0, directive.getToggles().get(0).getParameters().size());
    }

    @Test
    void parse_with_two_toggles() {
        var directive = MMEnableDirective.parse("AutoNumber(1), AutoLink(test)");
        assertEquals(2, directive.getToggles().size());
        assertEquals("AutoNumber", directive.getToggles().get(0).getName());
        assertEquals(1, directive.getToggles().get(0).getParameters().size());
        assertEquals("AutoLink", directive.getToggles().get(1).getName());
        assertEquals(1, directive.getToggles().get(1).getParameters().size());
    }
}