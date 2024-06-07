package info.colinhan.mindmark;

import info.colinhan.mindmark.processor.AutoNumberProcessor;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class XMindConverterTest {

    @Test
    void test_basic_usage() {
        var parser = new MindMarkParser();
        var model = parser.parse("Root", """
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
        var processor = new AutoNumberProcessor();
        processor.process(model);
        var converter = new XMindConverter();
        converter.convert(model, Path.of("./tmp"));
    }
}