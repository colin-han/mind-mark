package info.colinhan.mindmark.model;

import info.colinhan.mindmark.util.MindMarkParseException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Getter
public class MMEstimation {
    private static final Pattern PATTERN = Pattern.compile(
            "(\\d+(?:\\.\\d+)?)\\s*(h|d|w|hours?|days?|weeks?)",
            Pattern.CASE_INSENSITIVE
    );
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

    public static MMEstimation day(double days) {
        return new MMEstimation(MMEstimationUnit.DAY, days);
    }

    public static MMEstimation hour(double hours) {
        return new MMEstimation(MMEstimationUnit.HOUR, hours);
    }

    public static MMEstimation week(double weeks) {
        return new MMEstimation(MMEstimationUnit.WEEK, weeks);
    }

    public static MMEstimation zero() {
        return new MMEstimation(MMEstimationUnit.HOUR, 0);
    }

    public double getHours() {
        return this.unit.toHours(this.value);
    }

    @Override
    public String toString() {
        BigDecimal hours = BigDecimal.valueOf(this.getHours());
        long longHours = hours.toBigInteger().longValue();
        hours = hours.subtract(BigDecimal.valueOf(longHours));

        StringBuilder sb = new StringBuilder();
        longHours = MMEstimationUnit.WEEK.buildString(sb, longHours);
        longHours = MMEstimationUnit.DAY.buildString(sb, longHours);
        var restHours = hours.add(BigDecimal.valueOf(longHours));
        if (!restHours.stripTrailingZeros().equals(BigDecimal.ZERO)) {
            if (longHours == 0) sb.append(" ");
            sb.append(MMEstimationUnit.HOUR.toString(restHours));
        } else if (sb.isEmpty()) {
            sb.append("0 hours");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MMEstimation estimation1) {
            return estimation1.getHours() == getHours();
        }
        return super.equals(obj);
    }

    public boolean isZero() {
        return this.value == 0;
    }
}
