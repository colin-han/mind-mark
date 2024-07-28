package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;

import java.util.List;

public interface MMBase {
    <T> T accept(ModelVisitor<T> visitor);
    List<? extends MMBase> children();
}
