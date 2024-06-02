package info.colinhan.mindmark.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MMIncludeDirectiveTest {
    @Test
    void parse() {
        var directive = MMIncludeDirective.parse("Cards(#M1)");
        assertNotNull(directive);
        assertEquals("Cards", directive.getAncestorNode());
        assertEquals("#M1", directive.getFilter());
    }
}