package info.colinhan.mindmark.model;

import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class MMEnableDirective extends MMDirective {
    private final List<MMToggle> toggles = new ArrayList<>();

    public MMEnableDirective(Collection<MMToggle> toggles) {
        this.toggles.addAll(toggles);
    }

    public static MMEnableDirective parse(String toggles) {
        var items = toggles.split("\\s*,\\s*");
        return new MMEnableDirective(Stream.of(items)
                .map(MMToggle::parse)
                .toList());
    }

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return Collections.unmodifiableList(toggles);
    }
}
