package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class MMStyleDirective extends MMDirective {
    private static final Pattern PATTERN = Pattern.compile("\\s*#([\\w\\-._]+)\\s+(.*?)\\s*$");
    private static final Pattern STYLE_PATTERN = Pattern.compile("^([\\w\\-._]+)\\s*:\\s*([^,]+)$");
    private final String selector;
    private final Map<String, String> styles = new HashMap<>();

    public MMStyleDirective(String selector) {
        this.selector = selector;
    }

    public static MMDirective parse(String line) {
        var matcher = PATTERN.matcher(line);
        if (!matcher.find()) {
            throw new MindMarkParseException("Invalid style directive: " + line);
        }
        var directive = new MMStyleDirective(matcher.group(1));
        var styles = matcher.group(2).split("\\s*,\\s*");
        for (int i = 0; i < styles.length; i++) {
            var keyValue = STYLE_PATTERN.matcher(styles[i]);
            if (!keyValue.find()) {
                throw new MindMarkParseException("Invalid style syntax: " + styles[i]);
            }
            directive.styles.put(keyValue.group(1).toLowerCase(), keyValue.group(2));
        }
        return directive;
    }

    public String getFill() {
        return this.styles.getOrDefault("fill", null);
    }

    public String getStrokeWidth() {
        return this.styles.getOrDefault("stroke-width", null);
    }

    public String getStrokeColor() {
        return this.styles.getOrDefault("stroke-color", null);
    }

    public String getColor() {
        return this.styles.getOrDefault("color", null);
    }
}
