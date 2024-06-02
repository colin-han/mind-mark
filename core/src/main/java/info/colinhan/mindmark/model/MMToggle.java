package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class MMToggle {
    private static final Pattern TOGGLE_PATTERN = Pattern.compile("(\\w+)(?:\\(([^)]+)\\))?");
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
        return new MMToggle(matcher.group(1), List.of(matcher.group(2)));
    }
}
