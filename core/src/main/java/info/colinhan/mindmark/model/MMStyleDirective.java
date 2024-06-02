package info.colinhan.mindmark.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MMStyleDirective extends MMDirective {
    private final String selector;
    private final Map<String, String> styles = new HashMap<>();

    public MMStyleDirective(String selector) {
        this.selector = selector;
    }

    public static MMDirective parse(String line) {
        return new MMStyleDirective("fake");
    }
}
