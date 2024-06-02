package info.colinhan.mindmark.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class MMModel {
    private final List<MMDirective> directives = new ArrayList<>();
    private final List<MMNode> nodes = new ArrayList<>();

    public MMModel() {
    }

    public MMModel(Collection<MMNode> nodes, Collection<MMDirective> directives) {
        this.nodes.addAll(nodes);
        this.directives.addAll(directives);
    }

    public MMModel addNode(MMNode node) {
        nodes.add(node);
        return this;
    }
    public MMModel addDirective(MMDirective directive) {
        directives.add(directive);
        return this;
    }

    public MMModel addNodes(Collection<MMNode> nodes) {
        this.nodes.addAll(nodes);
        return this;
    }

    public MMNode getNode(int index) {
        return getNodes().get(index);
    }

    public int getNodeCount() {
        return getNodes().size();
    }

    public MMDirective getDirective(int index) {
        return directives.get(index);
    }

    public int getDirectiveCount() {
        return directives.size();
    }
}
