package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class MMEstimation {
    private static final Pattern PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)(h|d|w|hours?|days?|weeks?)");
    private final MMEstimationUnit unit;
    private final double value;

    public MMEstimation(MMEstimationUnit unit, double value) {
        this.unit = unit;
        this.value = value;
    }

    public static MMEstimation parse(String text) {
        var matcher = PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new MindMarkParseException("Invalid estimation format: " + text);
        }
        double value = Double.parseDouble(matcher.group(1));
        MMEstimationUnit unit = MMEstimationUnit.parse(matcher.group(2));
        return new MMEstimation(unit, value);
    }
}
