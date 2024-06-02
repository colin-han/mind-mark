package info.colinhan.mindmark.model;

import lombok.Getter;

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
}
