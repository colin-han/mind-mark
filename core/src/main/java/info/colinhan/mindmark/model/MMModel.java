package info.colinhan.mindmark.model;

import info.colinhan.mindmark.processor.ModelTraveller;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
public class MMModel implements MMBase {
    private final String name;
    private final List<MMDirective> directives = new ArrayList<>();
    private final List<MMNode> nodes = new ArrayList<>();

    public MMModel(String name) {
        this.name = name;
    }

    public MMModel(String name, Collection<MMNode> nodes, Collection<MMDirective> directives) {
        this(name);
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

    public List<MMNode> findDescendant(String nodeTitle) {
        return findDescendant(n -> n.getTitle().equals(nodeTitle) ? true : null);
    }

    public List<MMNode> findDescendant(Function<MMNode, Boolean> filter) {
        return MMNode.findDescendant(filter, nodes);
    }

    public void accept(ModelTraveller traveller) {
        MMNode.travel(nodes, traveller);
    }

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        // 合并directives和nodes
        return Stream.concat(directives.stream(), nodes.stream())
                .toList();
    }
}
