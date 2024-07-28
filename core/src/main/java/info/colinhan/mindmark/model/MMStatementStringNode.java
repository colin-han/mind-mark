package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public class MMStatementStringNode extends MMStatementNode {
    private final String text;

    public MMStatementStringNode(String text) {
        this.text = text;
    }

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return List.of();
    }
}
