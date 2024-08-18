package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMNode;

public interface ModelTraveller {
    boolean visit(MMNode node);
}
