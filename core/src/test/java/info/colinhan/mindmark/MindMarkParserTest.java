package info.colinhan.mindmark;

import info.colinhan.mindmark.model.MMEstimationUnit;
import info.colinhan.mindmark.model.MMModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MindMarkParserTest {
    @Test
    void parse_single_node() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("Hello, World!");
        assertNotNull(model);
        assertEquals(1, model.getNodeCount());
        assertEquals(0, model.getDirectives().size());
        assertEquals(0, model.getNode(0).getChildCount());
    }

    @Test
    void parse_multiple_node() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("Hello, World!\n\nThis is a test.");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(0, model.getDirectives().size());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(0, model.getNode(1).getChildCount());
    }

    @Test
    void parse_comments() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("Hello, World!\n\n# This is a comment.");
        assertNotNull(model);
        assertEquals(1, model.getNodeCount());
        assertEquals(0, model.getDirectives().size());
        assertEquals(0, model.getNode(0).getChildCount());
    }

    @Test
    void parse_tree() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("""
                Hello, World!
                This is a test.
                  This is a child.
                    This is a grandchild.
                This is another test.""");
        assertNotNull(model);
        assertEquals(3, model.getNodeCount());
        assertEquals(0, model.getDirectives().size());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(1, model.getNode(1).getChildCount());
    }

    @Test
    void support_directives() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("""
                @enable AutoNumber
                
                Hello, World!
                This is a test.
                  This is a child.""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(1, model.getDirectives().size());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(1, model.getNode(1).getChildCount());
    }

    @Test
    void support_tags() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("""
                Hello, World! #tag1
                This is a test. #tag2 #tag3
                  This is a child. #tag4""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(1, model.getNode(0).getTagCount());
        assertEquals("tag1", model.getNode(0).getTag(0).getName());
        assertEquals(2, model.getNode(1).getTagCount());
        assertEquals("tag2", model.getNode(1).getTag(0).getName());
        assertEquals("tag3", model.getNode(1).getTag(1).getName());
    }

    @Test
    void support_assignee() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("""
                Hello, World! @Alice
                This is a test. @Bob @Lily
                  This is a child. @Charlie""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(1, model.getNode(0).getAssigneeCount());
        assertEquals("Alice", model.getNode(0).getAssignee(0));
        assertEquals(2, model.getNode(1).getAssigneeCount());
        assertEquals("Bob", model.getNode(1).getAssignee(0));
        assertEquals("Lily", model.getNode(1).getAssignee(1));
    }

    @Test
    void support_estimation() {
        MindMarkParser parser = new MindMarkParser();
        MMModel model = parser.parse("""
                Hello, World! &1h
                This is a test. &2d
                  This is a child. &3w""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(1, model.getNode(0).getEstimation().getValue());
        assertEquals(MMEstimationUnit.HOUR, model.getNode(0).getEstimation().getUnit());
        assertEquals(2, model.getNode(1).getEstimation().getValue());
        assertEquals(MMEstimationUnit.DAY, model.getNode(1).getEstimation().getUnit());
        assertEquals(3, model.getNode(1).getChild(0).getEstimation().getValue());
        assertEquals(MMEstimationUnit.WEEK, model.getNode(1).getChild(0).getEstimation().getUnit());
    }
}