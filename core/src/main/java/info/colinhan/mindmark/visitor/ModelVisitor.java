package info.colinhan.mindmark.visitor;

import info.colinhan.mindmark.model.*;

public interface ModelVisitor<T> {
    T visit(MMModel model);

    T visit(MMEnableDirective enableDirective);

    T visit(MMEstimation estimation);

    T visit(MMExpression expression);

    T visit(MMIncludeDirective includeDirective);

    T visit(MMMacroDirective macroDirective);

    T visit(MMNode node);

    T visit(MMStatementFunctionNode functionNode);

    T visit(MMStatementStringNode stringNode);

    T visit(MMStyleDirective styleDirective);

    T visit(MMTag tag);

    T visit(MMToggle toggle);
}
