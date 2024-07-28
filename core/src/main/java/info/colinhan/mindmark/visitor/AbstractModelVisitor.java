package info.colinhan.mindmark.visitor;

import info.colinhan.mindmark.model.*;

public abstract class AbstractModelVisitor<T> implements ModelVisitor<T> {
    protected T defaultResult() {
        return null;
    }

    protected T aggregateResult(T aggregate, T nextResult) {
        return nextResult;
    }

    protected boolean shouldVisitNextChild(MMBase node, T currentResult) {
        return true;
    }

    public T visitChildren(MMBase node) {
        T result = this.defaultResult();
        var children = node.children();
        var n = children.size();
        for(int i = 0; i < n && this.shouldVisitNextChild(node, result); ++i) {
            MMBase c = children.get(i);
            T childResult = c.accept(this);
            result = this.aggregateResult(result, childResult);
        }

        return result;
    }

    @Override
    public T visit(MMModel model) {
        return visitChildren(model);
    }

    @Override
    public T visit(MMEnableDirective enableDirective) {
        return visitChildren(enableDirective);
    }

    @Override
    public T visit(MMEstimation estimation) {
        return visitChildren(estimation);
    }

    @Override
    public T visit(MMExpression expression) {
        return visitChildren(expression);
    }

    @Override
    public T visit(MMIncludeDirective includeDirective) {
        return visitChildren(includeDirective);
    }

    @Override
    public T visit(MMMacroDirective macroDirective) {
        return visitChildren(macroDirective);
    }

    @Override
    public T visit(MMNode node) {
        return visitChildren(node);
    }

    @Override
    public T visit(MMStatementFunctionNode functionNode) {
        return visitChildren(functionNode);
    }

    @Override
    public T visit(MMStatementStringNode stringNode) {
        return visitChildren(stringNode);
    }

    @Override
    public T visit(MMStyleDirective styleDirective) {
        return visitChildren(styleDirective);
    }

    @Override
    public T visit(MMTag tag) {
        return visitChildren(tag);
    }

    @Override
    public T visit(MMToggle toggle) {
        return visitChildren(toggle);
    }
}
