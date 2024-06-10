package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.MindMarkParser;
import info.colinhan.mindmark.model.MMEstimation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumEstimationProcessorTest {

    @Test
    void test_sum_estimation() {
        var model = MindMarkParser.parseModel("Root", """
                @style #TechResearch fill:#f9f,stroke:#333,stroke-width:4px,color:#333
                @macro #M?=max(/M#(\\d+)/g)
                
                Cards #M?
                  @enable AutoNumber, SumEstimation
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
        SumEstimationProcessor.applyTo(model);

        assertEquals(MMEstimation.day(5), model.getNode(0).getChild(0).getEstimation());
        assertEquals(MMEstimation.day(3), model.getNode(0).getChild(0).getChild(0).getEstimation());
        assertEquals(MMEstimation.day(6), model.getNode(0).getChild(1).getEstimation());
    }
}