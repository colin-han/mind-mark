package info.colinhan.mindmark.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
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
}
