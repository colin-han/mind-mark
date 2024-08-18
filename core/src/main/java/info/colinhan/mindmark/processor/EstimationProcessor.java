package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.*;
import info.colinhan.mindmark.util.NamedEnum;

import java.text.DecimalFormat;
import java.util.List;

public class EstimationProcessor {
    private static DecimalFormat decimalFormat = new DecimalFormat("0.##");

    public static class Position extends NamedEnum<Position> {
        public static Position asLabel = define(Position.class, "asLabel");
        public static Position atBeginningOfTitle = define(Position.class, "atBeginningOfTitle", "atBeginning", "atBegin");
        public static Position atEndOfTitle = define(Position.class, "atEndOfTitle", "atEnd");

        public Position(String... names) {
            super(names);
        }
        public static Position from(String name) {
            return find(Position.class, name);
        }
    }

    public static class Unit extends NamedEnum<Unit> {
        public static Unit useMinUnit = define(Unit.class, "useMinUnit", "minUnit", "useMinimumUnit", "minimumUnit");
        public static Unit useMaxUnit = define(Unit.class, "useMaxUnit", "maxUnit", "useMaximumUnit", "maximumUnit");
        public static Unit useMixinUnit = define(Unit.class, "useMixinUnit", "mixinUnit", "useMixUnit", "mixUnit");
        public static Unit useDay = define(Unit.class, "useDay", "useDays", "useDayUnit", "dayUnit", "days");
        public static Unit useWeek = define(Unit.class, "useWeek", "useWeeks", "useWeekUnit", "weekUnit", "weeks");
        public static Unit useHour = define(Unit.class, "useHour", "useHours", "useHourUnit", "hourUnit", "hours");

        public Unit(String... names) {
            super(names);
        }
        public static Unit from(String name) {
            return find(Unit.class, name);
        }
    }

    public static class UnitStyle extends NamedEnum<UnitStyle> {
        public static UnitStyle useLongUnit = define(UnitStyle.class, "useLongUnit", "longUnit");
        public static UnitStyle useShortUnit = define(UnitStyle.class, "useShortUnit", "shortUnit");
        public static UnitStyle useSingularUnit = define(UnitStyle.class, "useSingularUnit", "singularUnit");

        public UnitStyle(String... names) {
            super(names);
        }
        public static UnitStyle from(String name) {
            return find(UnitStyle.class, name);
        }
    }

    private record EstimationSetting(
            Position position,
            Unit unit,
            UnitStyle style,
            String format
    ) {
        EstimationSetting() {
            this(null, null, null, null);
        }

        public static EstimationSetting parse(List<String> parameters) {
            var setting = new EstimationSetting();
            for (var param : parameters) {
                var position = Position.from(param);
                var unit = Unit.from(param);
                var style = UnitStyle.from(param);
                String format = null;
                if (position == null && unit == null && style == null) {
                    format = param;
                }
                setting = setting.merge(new EstimationSetting(position, unit, style, format));
            }
            return setting;
        }

        public EstimationSetting merge(EstimationSetting setting) {
            if (setting == null) {
                return this;
            }
            return new EstimationSetting(
                    this.position != null ? this.position : setting.position,
                    this.unit != null ? this.unit : setting.unit,
                    this.style != null ? this.style : setting.style,
                    this.format != null ? this.format : setting.format
            );
        }

        @Override
        public Position position() {
            return position == null ? Position.asLabel : position;
        }

        @Override
        public Unit unit() {
            return unit == null ? Unit.useMinUnit : unit;
        }

        @Override
        public UnitStyle style() {
            return style == null ? UnitStyle.useLongUnit : style;
        }

        @Override
        public String format() {
            return format == null ? "⏰ %s" : format;
        }
    }

    public static void applyTo(MMModel model) {
        new EstimationProcessor().process(model);
    }

    private void process(MMModel model) {
        model.accept(node -> {
            var estimationSetting = findEstimationSetting(node);
            MMEstimation estimation = node.getEstimation();
            if (estimation != null) {
                var text = estimationToText(estimation, estimationSetting);
                if (estimationSetting.position().equals(Position.asLabel)) {
                    node.getTags().add(new MMTag(text));
                } else if (estimationSetting.position().equals(Position.atBeginningOfTitle)) {
                    node.withTitlePrefix(text);
                } else if (estimationSetting.position().equals(Position.atEndOfTitle)) {
                    node.withTitleSuffix(text);
                }
            }
            return true;
        });
    }

    private String estimationToText(MMEstimation estimation, EstimationSetting setting) {
        return String.format(setting.format(), estimationToText(estimation, setting.unit(), setting.style()));
    }
    private String estimationToText(MMEstimation estimation, Unit unit, UnitStyle style) {
        var items = estimation.split();
        MMEstimationUnit estimationUnit = items.get(0).unit();
        if (Unit.useMinUnit.equals(unit)) {
            estimationUnit = items.get(items.size() - 1).unit();
        } else if (Unit.useMaxUnit.equals(unit)) {
            estimationUnit = items.get(0).unit();
        } else if (Unit.useMixinUnit.equals(unit)) {
            return estimation.toString();
        } else if (Unit.useDay.equals(unit)) {
            estimationUnit = MMEstimationUnit.DAY;
        } else if (Unit.useWeek.equals(unit)) {
            estimationUnit = MMEstimationUnit.WEEK;
        } else if (Unit.useHour.equals(unit)) {
            estimationUnit = MMEstimationUnit.HOUR;
        }

        return estimationToText(estimation, estimationUnit, style);
    }

    private String estimationToText(MMEstimation estimation, MMEstimationUnit unit, UnitStyle style) {
        var hours = estimation.getHours();
        var value = hours / unit.getHours();
        String unitName;
        if (style.equals(UnitStyle.useShortUnit)) {
            unitName = unit.getAlias();
        } else if (style.equals(UnitStyle.useLongUnit)) {
            unitName = " " + (value == 1 ? unit.getName() : unit.getPlural());
        } else if (style.equals(UnitStyle.useSingularUnit)) {
            unitName = " " + unit.getName();
        } else {
            throw new IllegalArgumentException("Unknown unit style: " + style);
        }
        return decimalFormat.format(value) + unitName;
    }

    private EstimationSetting findEstimationSetting(MMNode node) {
        EstimationSetting settings = new EstimationSetting();
        settings = settings.merge(findEstimationSettingForNode(node));
        while (node.getParent() != null) {
            settings = settings.merge(findEstimationSettingForNode(node.getParent()));
            node = node.getParent();
        }
        return settings;
    }

    private EstimationSetting findEstimationSettingForNode(MMNode node) {
        return node.getDirectives().stream()
                .filter(d -> d instanceof MMEnableDirective)
                .flatMap(d -> ((MMEnableDirective) d).getToggles().stream())
                .filter(t -> t.getName().equalsIgnoreCase("estimation"))
                .map(t -> EstimationSetting.parse(t.getParameters()))
                .reduce(EstimationSetting::merge)
                .orElse(null);
    }
}
