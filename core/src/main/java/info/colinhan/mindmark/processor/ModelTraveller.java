package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.MMNode;

import java.util.List;

public interface ModelTraveller {
    boolean visit(MMNode node);
}
