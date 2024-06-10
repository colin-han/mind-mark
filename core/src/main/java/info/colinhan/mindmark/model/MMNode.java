package info.colinhan.mindmark.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Getter
public class MMNode {
    private final int indent;
    private final String title;
    private final List<MMTag> tags = new ArrayList<>();

    private MMEstimation estimation;
    private final List<MMDirective> directives = new ArrayList<>();
    private final List<String> assignees = new ArrayList<>();
    private final List<MMNode> children = new ArrayList<>();
    @Setter
    private String num;

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
        if (this.num == null || this.num.isEmpty()) {
            return this.title;
        }
        return this.num + " " + this.title;
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
}
