package info.colinhan.mindmark.model;

import lombok.Getter;

@Getter
public enum MMEstimationUnit {
    HOUR("hour", "h", "hours"),
    DAY("day", "d", "days"),
    WEEK("week", "w", "weeks");

    private final String name;
    private final String[] aliases;

    MMEstimationUnit(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public static MMEstimationUnit parse(String text) {
        for (MMEstimationUnit unit : values()) {
            if (unit.name.equalsIgnoreCase(text)) {
                return unit;
            }
            for (String alias : unit.aliases) {
                if (alias.equalsIgnoreCase(text)) {
                    return unit;
                }
            }
        }
        return null;
    }
}
