package info.colinhan.mindmark.util;

import info.colinhan.mindmark.model.*;

public class Directives {
    public static MMDirective parse(String type, String line) {
        return switch (type) {
            case "enable" -> MMEnableDirective.parse(line);
            case "macro" -> MMMacroDirective.parse(line);
            case "style" -> MMStyleDirective.parse(line);
            case "include" -> MMIncludeDirective.parse(line);
            default -> throw new MindMarkParseException("Unknown directive type: " + type);
        };
    }
}
