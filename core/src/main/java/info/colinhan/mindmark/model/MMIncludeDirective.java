package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import info.colinhan.mindmark.visitor.ModelVisitor;
import lombok.Getter;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    @Override
    public <T> T accept(ModelVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<? extends MMBase> children() {
        return List.of();
    }
}
