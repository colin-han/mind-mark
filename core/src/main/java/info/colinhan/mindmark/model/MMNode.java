package info.colinhan.mindmark.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MMNode {
    private final int indent;
    private final String title;
    private final List<MMTag> tags = new ArrayList<>();
    private MMEstimation estimation;
    private final List<MMDirective> directives = new ArrayList<>();
    private final List<String> assignees = new ArrayList<>();
    private final List<MMNode> children = new ArrayList<>();

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
}
