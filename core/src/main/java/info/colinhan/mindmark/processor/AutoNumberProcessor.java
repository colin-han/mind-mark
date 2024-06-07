package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMDirective;
import info.colinhan.mindmark.model.MMEnableDirective;
import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMNode;

import java.util.List;

public class AutoNumberProcessor {
    private boolean findAutoNumberToggle(List<MMDirective> directives) {
        return directives.stream().anyMatch(d -> {
            if (d instanceof MMEnableDirective enableDirective) {
                return enableDirective.getToggles().stream().anyMatch(
                        t -> t.getName().equalsIgnoreCase("AutoNumber")
                );
            }
            return false;
        });
    }
    public void process(MMModel model) {
        boolean autoNumber = findAutoNumberToggle(model.getDirectives());
        List<MMNode> nodes = model.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            MMNode n = nodes.get(i);
            processNode(n, autoNumber, autoNumber ? (i + 1) + "." : "");
        }
    }

    private void processNode(MMNode node, boolean autoNumber, String num) {
        if (!autoNumber) {
            autoNumber = findAutoNumberToggle(node.getDirectives());
        } else {
            node.setNum(num);
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            processNode(node.getChild(i), autoNumber, autoNumber ? num + (i + 1) + "." : "");
        }
    }
}
