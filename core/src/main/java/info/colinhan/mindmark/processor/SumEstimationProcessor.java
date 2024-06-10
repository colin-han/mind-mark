package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMNode;
import info.colinhan.mindmark.model.MMToggle;
import info.colinhan.mindmark.util.EstimationCalculator;

public class SumEstimationProcessor extends ToggleProcessor<MMEstimation> {
    @Override
    protected boolean isToggle(MMToggle t) {
        return t.getName().equalsIgnoreCase("SumEstimation");
    }

    @Override
    protected void processNode(MMNode node, boolean toggle, MMEstimation currentValue) {
        super.processNode(node, toggle, currentValue);

        var estimation = node.getChildren().stream().reduce(
                MMEstimation.zero(),
                (e, n) -> EstimationCalculator.add(e, n.getEstimation()),
                EstimationCalculator::add
        );
        if (estimation != null && !estimation.isZero()) {
            node.withEstimation(estimation);
        }
    }

    public static void applyTo(MMModel model) {
        new SumEstimationProcessor().process(model);
    }
}
