package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public class MMTag implements MMBase {
    private final String name;

    public MMTag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
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
