package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.MindMarkParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncludeProcessorTest {

    @Test
    void test_include_with_tag() {
        var model = MindMarkParser.parseModel("root", """
                @style #TechResearch fill:#f9f,stroke:#333,stroke-width:4px,color:#333
                @macro #M?=max(/M#(\\d+)/g)
                
                Cards #M?
                  @enable AutoNumber, SumEstimation
                  Epic A #M?
                    Story A.1 #M1 &3d
                    Story A.2 #M1 &2d #TechResearch
                  Epic B #M?
                    Story B.1 #M2 &1w
                    Story B.2 #M3 &1d #TechResearch
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
        IncludeProcessor.applyTo(model);
        assertEquals(2, model.findDescendant("Tech Research Tasks").get(0).getChildCount());
        assertEquals(0, model.findDescendant("Tech Design Cards").get(0).getChildCount());
    }
}