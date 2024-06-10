package info.colinhan.mindmark;

import info.colinhan.mindmark.model.MMNode;
import info.colinhan.mindmark.processor.AutoNumberProcessor;
import info.colinhan.mindmark.processor.IncludeProcessor;
import info.colinhan.mindmark.processor.StyleProcessor;
import info.colinhan.mindmark.processor.SumEstimationProcessor;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class XMindConverterTest {

    @Test
    void test_basic_usage() {
        var model = MindMarkParser.parseModel("Root", """
              @style #TechResearch fill:#95FF9595,stroke-color:#460101,stroke-width:4,color:#460101
              @macro #M?=max(/M#(\\d+)/g)
              
              Cards #M?
                @enable AutoNumber, SumEstimation
                Epic A #M?
                  Story A.1 #M1 &3d
                  Story A.2 #M1 &2d
                    TechResearch A.2.1 #TechResearch
                Epic B #M?
                  Story B.1 #M2 &1w
                  Story B.2 #M3 &1d
                Epic C #M1
                  Story C.1 &1d
                  Story C.2 &1d
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
        StyleProcessor.applyTo(model);
        AutoNumberProcessor.applyTo(model);
        SumEstimationProcessor.applyTo(model);
        IncludeProcessor.applyTo(model);
        var converter = new XMindConverter();
        converter.convert(model, Path.of("./tmp"));
    }
}