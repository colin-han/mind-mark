package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMTag;

public class TagProcessor {
    public static void applyTo(MMModel model) {
        new TagProcessor().process(model);
    }

    private void process(MMModel model) {
        model.accept(node -> {
            var tags = node.getTags();
            for (var tag : tags) {
                node.addLabel("🏷 " + tag.getName());
            }
            return true;
        });
    }
}
