package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.MindMarkParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutoNumberProcessorTest {

    @Test
    void process() {
        var model = MindMarkParser.parseModel("Root", """
                @style #TechResearch fill:#f9f,stroke:#333,stroke-width:4px,color:#333
                @macro #M?=max(/M#(\\d+)/g)
                
                Cards #M?
                  @enable AutoNumber
                  Epic A #M?
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d
                Tech Research Tasks
                  @include Cards(#TechResearch)
                Tech Design Cards
                  @include Cards(#TechDesign)
                Milestones
                  M1 实现基本功能
                    @include Cards(#M1)
                  M2 实现附加的功能
                    @include Cards(#M2)
                  M3 实现更多的功能
                    @include Cards(#M3)""");
        var processor = new AutoNumberProcessor();
        processor.process(model);
        assertEquals("Cards", model.getNode(0).getText());
        assertEquals("1. Epic A", model.getNode(0).getChild(0).getText());
        assertEquals("1.1. Story A.1", model.getNode(0).getChild(0).getChild(0).getText());
        assertEquals("1.2. Story A.2", model.getNode(0).getChild(0).getChild(1).getText());
        assertEquals("2. Epic B", model.getNode(0).getChild(1).getText());
        assertEquals("2.1. Story B.1", model.getNode(0).getChild(1).getChild(0).getText());
        assertEquals("2.2. Story B.2", model.getNode(0).getChild(1).getChild(1).getText());
        assertEquals("Tech Research Tasks", model.getNode(1).getText());
    }
}