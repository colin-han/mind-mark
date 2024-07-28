package info.colinhan.mindmark.model;

import info.colinhan.mindmark.processor.ModelTraveller;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
public class MMNode implements MMBase {
    private final int indent;
    private final String title;
    private final List<MMTag> tags = new ArrayList<>();

    private MMEstimation estimation;
    private final List<MMDirective> directives = new ArrayList<>();
    private final List<String> assignees = new ArrayList<>();
    private final List<MMNode> children = new ArrayList<>();
    private String titlePrefix = "";
    private String titlePostfix = "";
    @Setter
    private String className;
    @Setter
    private MMNode parent;

    public MMNode(int indent, String title) {
        this(indent, title, null);
    }

    public MMNode(int indent, String title, MMEstimation estimation) {
        this.indent = indent;
        this.title = title;
        this.estimation = estimation;
    }

    public MMNode addTag(String tag) {
        return addTag(new MMTag(tag));
    }

    public MMNode addTag(MMTag tag) {
        tags.add(tag);
        return this;
    }

    public MMNode addDirective(MMDirective directive) {
        directives.add(directive);
        return this;
    }

    public MMNode addAssignee(String assignee) {
        assignees.add(assignee);
        return this;
    }

    public MMNode withEstimation(MMEstimation estimation) {
        this.estimation = estimation;
        return this;
    }

    public MMNode withTitlePrefix(String prefix) {
        this.titlePrefix = prefix + this.titlePrefix + " ";
        return this;
    }

    public MMNode withTitlePostfix(String postfix) {
        this.titlePostfix = this.titlePostfix + " " + postfix;
        return this;
    }

    public int getChildCount() {
        return this.children.size();
    }

    public MMNode getChild(int index) {
        return this.children.get(index);
    }

    public int getAssigneeCount() {
        return this.assignees.size();
    }

    public String getAssignee(int index) {
        return this.assignees.get(index);
    }

    public int getTagCount() {
        return this.tags.size();
    }

    public MMTag getTag(int index) {
        return this.tags.get(index);
    }

    public String getText() {
        return this.titlePrefix + this.title + this.titlePostfix;
    }

    static List<MMNode> findDescendant(Function<MMNode, Boolean> filter, List<MMNode> nodes) {
        List<MMNode> result = new ArrayList<>();
        for (MMNode n : nodes) {
            Boolean filterResult = filter.apply(n);
            if (filterResult == null) {
                result.addAll(findDescendant(filter, n.getChildren()));
            } else if (filterResult) {
                result.add(n);
            }
        }
        return result;
    }

    public List<MMNode> findDescendant(String nodeTitle) {
        return findDescendant(n -> n.getTitle().equals(nodeTitle) ? true : null);
    }

    public List<MMNode> findDescendant(Function<MMNode, Boolean> filter) {
        return MMNode.findDescendant(filter, this.children);
    }

    public void accept(ModelTraveller traveller) {
        if (traveller.visit(this)) {
            travel(this.children, traveller);
        }
    }

    public static void travel(List<MMNode> nodes, ModelTraveller traveller) {
        for (MMNode node : nodes) {
            if (traveller.visit(node)) {
                travel(node.getChildren(), traveller);
            }
        }
    }

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return Stream.of(
                tags.stream(),
                Stream.of(estimation),
                directives.stream(),
                children.stream()
        ).flatMap(s -> s).toList();
    }
}
