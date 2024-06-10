package info.colinhan.mindmark.util;

public interface TriFunction<T1, T2, T3, TR> {
    TR apply(T1 a, T2 b, T3 c);
}
