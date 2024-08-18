package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Getter
public class MMToggle implements MMBase {
    private static final Pattern TOGGLE_PATTERN = Pattern.compile("(\\w+)(?:\\((.+?)\\))?");
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\s*(?:\"((?:[^\"]|\"\")*)\"|(\\w+))\\s*");
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

        String functionName = matcher.group(1);
        if (matcher.group(2) == null) {
            return new MMToggle(functionName, List.of());
        }

        var result = parseParameters(matcher.group(2));
        return new MMToggle(functionName, result);
    }

    private static ArrayList<String> parseParameters(String parameters) {
        var parameterMatcher = PARAMETER_PATTERN.matcher(parameters);
        var result = new ArrayList<String>();
        while (parameterMatcher.find()) {
            String quotedContent = parameterMatcher.group(1);
            String word = parameterMatcher.group(2);
            if (quotedContent != null) {
                result.add(quotedContent.replace("\"\"", "\""));
            } else if (word != null) {
                result.add(word);
            }
        }
        return result;
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
