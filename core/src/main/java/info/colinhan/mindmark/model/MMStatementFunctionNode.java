package info.colinhan.mindmark.model;

import info.colinhan.mindmark.func.MMFunction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MMStatementFunctionNode extends MMStatementNode {
    private final MMFunction func;
    private final List<String> args = new ArrayList<>();

    public MMStatementFunctionNode(MMFunction func, String... args) {
        this.func = func;
        for (String arg : args) {
            this.args.add(arg);
        }
    }
}
