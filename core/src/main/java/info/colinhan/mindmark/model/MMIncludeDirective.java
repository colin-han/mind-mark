package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class MMIncludeDirective extends MMDirective {
    private static final Pattern PATTERN = Pattern.compile("\\s*([^()]+)\\s*\\(\\s*([^()]+)\\s*\\)");
    private final String ancestorNode;
    private final String filter;

    public MMIncludeDirective(String ancestorNode, String filter) {
        this.ancestorNode = ancestorNode;
        this.filter = filter;
    }

    public static MMIncludeDirective parse(String line) {
        var matcher = PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new MindMarkParseException("Invalid include directive: " + line);
        }

        return new MMIncludeDirective(matcher.group(1), matcher.group(2));
    }

    public boolean isMatchFilter(MMNode node) {
        return node.getTags().stream().anyMatch(t -> t.getName().equals(filter.substring(1)));
    }
}
