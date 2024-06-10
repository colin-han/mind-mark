package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMStyleDirective;
import info.colinhan.mindmark.util.MindMarkParseException;

import java.util.List;

public class StyleProcessor {
    private final MMModel model;

    public StyleProcessor(MMModel model) {
        this.model = model;
    }

    public static void applyTo(MMModel model) {
        new StyleProcessor(model).process();
    }

    private void process() {
        model.accept(n -> {
            if (n.getDirectives().stream().anyMatch(d -> d instanceof MMStyleDirective)) {
                throw new MindMarkParseException("Style directive not allowed in node");
            }
            return true;
        });

        var styleDirectives = getStyles(model);
        var styleNames = styleDirectives.stream()
                .map(MMStyleDirective::getSelector)
                .map(String::toLowerCase)
                .toList();
        model.accept(n -> {
            var styles = n.getTags().stream()
                    .filter(t -> styleNames.contains(t.getName().toLowerCase()))
                    .toList();
            if (styles.size() == 1) {
                n.setClassName(styles.get(0).getName());
            } else if (styles.size() > 1) {
                throw new MindMarkParseException("Multiple style tags found: " + styles);
            }
            return true;
        });
    }

    public static  List<MMStyleDirective> getStyles(MMModel model1) {
        return model1.getDirectives().stream()
                .filter(directive -> directive instanceof MMStyleDirective)
                .map(directive -> (MMStyleDirective) directive)
                .toList();
    }

}
