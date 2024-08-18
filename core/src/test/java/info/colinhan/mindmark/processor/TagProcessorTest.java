package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.MindMarkParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagProcessorTest {
    @Test
    void should_contains_prefix() {
        var model = MindMarkParser.parseModel("Root", """
                Hello, World! #tag
                  Card #tag1
                    SubCard #tag2
                """);
        TagProcessor.applyTo(model);
        assertEquals(1, model.getNode(0).getTags().size());
        assertEquals("🏷 tag", model.getNode(0).getLabel(0));
        assertEquals("🏷 tag1", model.getNode(0).getChild(0).getLabel(0));
        assertEquals("🏷 tag2", model.getNode(0)
                .getChild(0)
                .getChild(0)
                .getLabel(0));
    }
}