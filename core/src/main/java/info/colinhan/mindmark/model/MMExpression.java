package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MMExpression implements MMBase {
    // TODO: Implement macro directive.
    private final List<MMStatementNode> nodes = new ArrayList<>();

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return Collections.unmodifiableList(nodes);
    }
}
