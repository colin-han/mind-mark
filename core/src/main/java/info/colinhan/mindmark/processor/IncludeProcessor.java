package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMDirective;
import info.colinhan.mindmark.model.MMIncludeDirective;
import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMNode;
import info.colinhan.mindmark.util.MindMarkParseException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class IncludeProcessor {
    private final MMModel model;

    public static void applyTo(MMModel model) {
        new IncludeProcessor(model).process();
    }

    private IncludeProcessor(MMModel model) {
        this.model = model;
    }

    public void process() {
        for (MMNode mmNode : model.getNodes()) {
            processNode(mmNode);
        }
    }

    private void processNode(MMNode node) {
        for (MMNode child : node.getChildren()) {
            processNode(child);
        }
        Optional<MMDirective> directive = node.getDirectives().stream()
                .filter(d -> d instanceof MMIncludeDirective)
                .findAny();
        directive.ifPresent(mmDirective -> applyDirective(node, (MMIncludeDirective) mmDirective));
    }

    private void applyDirective(MMNode node, MMIncludeDirective directive) {
        node.getChildren().addAll(findNodes(directive));
    }

    private List<? extends MMNode> findNodes(MMIncludeDirective includeDirective) {
        List<MMNode> sourceNodes = model.findDescendant(includeDirective.getAncestorNode());
        if (sourceNodes.isEmpty()) {
            throw new MindMarkParseException("Cannot find ancestor node: " + includeDirective.getAncestorNode());
        }
        var sourceNode = sourceNodes.get(0);
        return sourceNode.findDescendant(node -> includeDirective.isMatchFilter(node) ? true : null);
    }
}
