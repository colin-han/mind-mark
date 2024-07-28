package info.colinhan.mindmark;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMEstimationUnit;
import info.colinhan.mindmark.model.MMModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MindMarkParserTest {
    @Test
    void parse_single_node() {
        MMModel model = MindMarkParser.parseModel("Root", "Hello, World!");
        assertNotNull(model);
        assertEquals(1, model.getNodeCount());
        assertEquals(0, model.getDirectiveCount());
        assertEquals(0, model.getNode(0).getChildCount());
    }

    @Test
    void parse_multiple_node() {
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World!

                This is a test.""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(0, model.getDirectiveCount());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(0, model.getNode(1).getChildCount());
    }

    @Test
    void parse_comments() {
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World!

                # This is a comment.""");
        assertNotNull(model);
        assertEquals(1, model.getNodeCount());
        assertEquals(0, model.getDirectiveCount());
        assertEquals(0, model.getNode(0).getChildCount());
    }

    @Test
    void parse_tree() {
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World!
                This is a test.
                  This is a child.
                    This is a grandchild.
                This is another test.""");
        assertNotNull(model);
        assertEquals(3, model.getNodeCount());
        assertEquals(0, model.getDirectiveCount());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(1, model.getNode(1).getChildCount());
    }

    @Test
    void support_directives() {
        MMModel model = MindMarkParser.parseModel("Root", """
                @enable AutoNumber
                
                Hello, World!
                This is a test.
                  This is a child.""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(1, model.getDirectiveCount());
        assertEquals(0, model.getNode(0).getChildCount());
        assertEquals(1, model.getNode(1).getChildCount());
    }

    @Test
    void support_tags() {
        MMModel model = MindMarkParser.parseModel("Root", """
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
        MMModel model = MindMarkParser.parseModel("Root", """
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
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World! & 1 hours
                This is a test. &2d
                  This is a child. &3 w""");
        assertNotNull(model);
        assertEquals(2, model.getNodeCount());
        assertEquals(MMEstimation.hour(1), model.getNode(0).getEstimation());
        assertEquals(MMEstimation.day(2), model.getNode(1).getEstimation());
        assertEquals(MMEstimation.week(3), model.getNode(1).getChild(0).getEstimation());
    }

    @Test
    void support_directive_in_node() {
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World! &1h
                This is a test. &2d
                  @enable AutoNumber
                  This is a child. &3w""");
        assertNotNull(model);
        assertEquals(1, model.getNode(1).getChildCount());
        assertEquals("This is a child.", model.getNode(1).getChild(0).getTitle());
    }

    @Test
    void support_parent_relation() {
        MMModel model = MindMarkParser.parseModel("Root", """
                Hello, World! &1h
                This is a test. &2d
                  @enable AutoNumber
                  This is a child. &3w""");
        assertNotNull(model);
        assertEquals(model.getNode(1), model.getNode(1).getChild(0).getParent());
    }
}