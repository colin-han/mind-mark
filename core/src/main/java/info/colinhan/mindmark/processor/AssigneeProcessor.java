package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMTag;

public class AssigneeProcessor {
    public static void applyTo(MMModel model) {
        new AssigneeProcessor().process(model);
    }

    private void process(MMModel model) {
        model.accept(node -> {
            var assignees = node.getAssignees();
            for (String assignee : assignees) {
                node.addLabel("🙍‍" + assignee);
            }
            return true;
        });
    }
}
