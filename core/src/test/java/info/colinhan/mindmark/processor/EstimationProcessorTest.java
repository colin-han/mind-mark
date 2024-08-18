package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.MindMarkParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstimationProcessorTest {
    @Test
    void test_asLabel_and_fixedDays() {
        var model = MindMarkParser.parseModel("Root", """
                @enable estimation(asLabel useDays "⏰ %s")
                
                Cards &4h
                  Epic A &1w
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d
                """);
        EstimationProcessor.applyTo(model);

        assertEquals("Cards", model.getNode(0).getText());
        assertEquals("⏰ 0.5 days", model.getNode(0).getLabel(0));
        assertEquals("⏰ 5 days", model.getNode(0).getChild(0).getLabel(0));
    }

    @Test
    void test_asLabel_and_fixedWeeks() {
        var model = MindMarkParser.parseModel("Root", """
                @enable estimation(asLabel useWeeks "(%s)")
                Cards &4h
                  Epic A &1w
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d
                """);
        EstimationProcessor.applyTo(model);

        assertEquals("Cards", model.getNode(0).getText());
        assertEquals("(0.1 weeks)", model.getNode(0).getLabel(0));
        assertEquals("(1 week)", model.getNode(0).getChild(0).getLabel(0));
    }

    @Test
    void test_atBeginningOfTitle_and_useMixUnit() {
        var model = MindMarkParser.parseModel("Root", """
                @enable estimation(atBeginningOfTitle useMixUnit "(%s) ")
                Cards &4h
                  Epic A &6d
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d
                """);
        EstimationProcessor.applyTo(model);

        assertEquals("(4 hours) Cards", model.getNode(0).getText());
        assertEquals("(1 week 1 day) Epic A", model.getNode(0).getChild(0).getText());
    }

    @Test
    void test_atEndOfTitle() {
        var model = MindMarkParser.parseModel("Root", """
                @enable estimation(atEndOfTitle "\\n (%s)")
                Cards &4h
                  Epic A &6d
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d
                """);
        EstimationProcessor.applyTo(model);

        assertEquals("Cards\\n (4 hours)", model.getNode(0).getText());
        assertEquals("Epic A\\n (6 days)", model.getNode(0).getChild(0).getText());
    }
}