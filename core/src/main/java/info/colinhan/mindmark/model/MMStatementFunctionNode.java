package info.colinhan.mindmark.model;

import info.colinhan.mindmark.func.MMFunction;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class MMStatementFunctionNode extends MMStatementNode {
    private final MMFunction func;
    private final List<String> args = new ArrayList<>();

    public MMStatementFunctionNode(MMFunction func, String... args) {
        this.func = func;
        this.args.addAll(Arrays.asList(args));
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
