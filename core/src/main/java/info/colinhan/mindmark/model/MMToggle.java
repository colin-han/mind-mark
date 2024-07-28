package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Getter
public class MMToggle implements MMBase {
    private static final Pattern TOGGLE_PATTERN = Pattern.compile("(\\w+)(?:\\((.+?)\\))?");
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\s*(\\w+|\"[^\"]+\")\\s*");
    private final String name;
    private final List<String> parameters = new ArrayList<>();

    public MMToggle(String name, Collection<String> parameters) {
        this.name = name;
        this.parameters.addAll(parameters);
    }

    public static MMToggle parse(String text) {
        var matcher = TOGGLE_PATTERN.matcher(text);
        if (!matcher.matches()) {
            throw new MindMarkParseException("Invalid toggle format: " + text);
        }

        if (matcher.group(2) == null) {
            return new MMToggle(matcher.group(1), List.of());
        }

        var parameters = matcher.group(2);
        var parameterMatcher = PARAMETER_PATTERN.matcher(parameters);
        var result = new ArrayList<String>();
        while (parameterMatcher.find()) {
            result.add(parameterMatcher.group(1));
        }
        return new MMToggle(matcher.group(1), result);
    }

    public static List<MMToggle> findToggle(MMNode node, List<MMNode> ancestors, Predicate<MMToggle> finder) {
        List<MMToggle> toggles = new ArrayList<>();
        node.getDirectives().stream()
                .filter(d -> d instanceof MMEnableDirective)
                .flatMap(d -> {
                    var directive = (MMEnableDirective) d;
                    return directive.getToggles().stream()
                            .filter(finder);
                })
                .forEach(toggles::add);

        for (int i = ancestors.size() - 1; i >= 0; i--) {
            var ancestor = ancestors.get(i);
            ancestor.getDirectives().stream()
                    .filter(d -> d instanceof MMEnableDirective)
                    .flatMap(d -> {
                        var directive = (MMEnableDirective) d;
                        return directive.getToggles().stream()
                                .filter(finder);
                    })
                    .forEach(toggles::add);
        }
        return toggles;
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
