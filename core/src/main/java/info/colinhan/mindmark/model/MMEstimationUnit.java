package info.colinhan.mindmark.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum MMEstimationUnit {
    HOUR("hour", "hours", "h", 1),
    DAY("day", "days", "d", 8),
    WEEK("week", "weeks", "w", 40);

    private final String name;
    private final String plural;
    private final String alias;
    private final int hours;

    MMEstimationUnit(String name, String plural, String alias, int hours) {
        this.name = name;
        this.plural = plural;
        this.alias = alias;
        this.hours = hours;
    }

    public static MMEstimationUnit parse(String text) {
        for (MMEstimationUnit unit : values()) {
            if (unit.name.equalsIgnoreCase(text)) {
                return unit;
            }
            if (unit.plural.equalsIgnoreCase(text)) {
                return unit;
            }
            if (unit.alias.equalsIgnoreCase(text)) {
                return unit;
            }
        }
        return null;
    }

    private static String doubleToString(double value1) {
        long longValue = Math.round(value1);
        return value1 == longValue
                ? String.valueOf(longValue)
                : String.valueOf(value1);
    }

    public double toHours(double value) {
        return value * this.hours;
    }

    String toString(long count) {
        return count == 1 ? count + " " + getName() : count + " " + getPlural();
    }

    long buildString(StringBuilder sb, long longHours) {
        if (longHours >= getHours()) {
            long longWeek = longHours / getHours();
            sb.append(toString(longWeek));
            longHours = longHours % getHours();
            if (longHours != 0) {
                sb.append(" ");
            }
        }
        return longHours;
    }

    String toString(BigDecimal restHours) {
        BigDecimal value = restHours.stripTrailingZeros();
        if (value.equals(BigDecimal.ONE)) return "1 " + getName();
        return value.toPlainString() + " " + getPlural();
    }
}
