package info.colinhan.mindmark.util;

import info.colinhan.mindmark.model.MMEstimation;

public class EstimationCalculator {
    public static MMEstimation add(MMEstimation estimation1, MMEstimation estimation2) {
        if (estimation1 == null && estimation2 == null) {
            return MMEstimation.zero();
        }
        if (estimation1 == null) return estimation2;
        if (estimation2 == null) return estimation1;
        return MMEstimation.hour(estimation1.getHours() + estimation2.getHours());
    }
}
