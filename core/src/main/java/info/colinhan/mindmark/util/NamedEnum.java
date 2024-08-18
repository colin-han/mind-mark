package info.colinhan.mindmark.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class NamedEnum<T extends NamedEnum<?>> {
    private record NamedEnumClass<T2 extends NamedEnum<?>>(String name, Constructor<T2> ctor, Map<String, T2> values) {}
    private static final Map<Class<?>, NamedEnumClass<?>> classMap = new HashMap<>();

    protected final String[] names;
    protected NamedEnum(String... names) {
        this.names = names;
    }

    @SuppressWarnings("unchecked")
    protected static <R extends NamedEnum<?>> R define(Class<R> clazz, String... names) {
        try {
            var enumClass = (NamedEnumClass<R>) classMap.computeIfAbsent(clazz, c -> {
                try {
                    var ctor = (Constructor<R>)c.getConstructor(String[].class);
                    var name = clazz.getSimpleName();
                    return new NamedEnumClass<>(name, ctor, new HashMap<>());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            var ctor = enumClass.ctor();
            var valueMap = enumClass.values();
            R r = ctor.newInstance((Object) names);
            for (var n : names) {
                valueMap.put(n, r);
            }
            return r;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <R extends NamedEnum<?>> R find(Class<R> clazz, String name) {
        var enumClass = (NamedEnumClass<R>) classMap.get(clazz);
        if (enumClass == null) {
            throw new IllegalArgumentException("Class " + clazz + " is not a NamedEnum");
        }
        return enumClass.values().getOrDefault(name, null);
    }

    public String getName() {
        return names[0];
    }

    @Override
    public String toString() {
        return this.getClass().getTypeName();
    }
}
