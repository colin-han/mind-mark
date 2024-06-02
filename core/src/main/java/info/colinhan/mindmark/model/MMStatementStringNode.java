package info.colinhan.mindmark.model;

import lombok.Getter;

@Getter
public class MMStatementStringNode extends MMStatementNode {
    private final String text;

    public MMStatementStringNode(String text) {
        this.text = text;
    }
}
