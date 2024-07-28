package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public class MMMacroDirective extends MMDirective {
    private final String name;
    private final MMExpression expression;

    public MMMacroDirective(String name, MMExpression expression) {
        this.name = name;
        this.expression = expression;
    }

    public static MMDirective parse(String line) {
        return new MMMacroDirective("fake", null);
    }

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return List.of(expression);
    }
}
