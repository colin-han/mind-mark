package info.colinhan.mindmark.util;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMEstimationUnit;

public class EstimationCalculator {
    public static MMEstimation add(MMEstimation estimation1, MMEstimation estimation2) {
        return MMEstimation.hour(estimation1.getHours() + estimation2.getHours());
    }
}
