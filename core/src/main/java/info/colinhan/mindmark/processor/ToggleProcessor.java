package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.*;

import java.util.List;

public abstract class ToggleProcessor<T> {
    private boolean findToggle(List<MMDirective> directives) {
        return directives.stream().anyMatch(d -> {
            if (d instanceof MMEnableDirective enableDirective) {
                return enableDirective.getToggles().stream().anyMatch(
                        this::isToggle
                );
            }
            return false;
        });
    }

    public void process(MMModel model) {
        boolean toggle = findToggle(model.getDirectives());
        List<MMNode> nodes = model.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            MMNode n = nodes.get(i);
            T currentValue = calcNewValue(toggle, null, i);
            processNode(n, toggle, currentValue);
        }
    }

    protected void processNode(MMNode node, boolean toggle, T currentValue) {
        if (!toggle) {
            toggle = findToggle(node.getDirectives());
        } else {
            applyToggle(node, currentValue);
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            processNode(node.getChild(i), toggle, calcNewValue(toggle, currentValue, i));
        }
    }

    protected abstract boolean isToggle(MMToggle t);

    protected T calcNewValue(boolean toggle, T currentValue, int i) {
        return currentValue;
    };

    protected void applyToggle(MMNode node, T currentValue) {

    };
}
