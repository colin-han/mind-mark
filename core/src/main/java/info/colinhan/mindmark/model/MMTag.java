package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Getter
public class MMTag implements MMBase {
    private String name;

    public MMTag(String name) {
        this.name = name;
    }

    public MMTag withName(String name) {
        this.name = name;
        return this;
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

    public MMTag deepClone() {
        return new MMTag(this.name);
    }
}
