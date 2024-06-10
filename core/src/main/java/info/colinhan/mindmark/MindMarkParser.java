package info.colinhan.mindmark;

import info.colinhan.mindmark.model.MMEstimation;
import info.colinhan.mindmark.model.MMModel;
import info.colinhan.mindmark.model.MMNode;
import info.colinhan.mindmark.util.Directives;

import java.util.Stack;
import java.util.regex.Pattern;

public class MindMarkParser {
    private static final Pattern INDENT_PATTERN = Pattern.compile("^(\\s*)(.*)$");
    private static final Pattern DIRECTIVE_PATTERN = Pattern.compile("^\\s*@(\\w+)\\s+(.*)$");
    private static final Pattern NODE_ADDITIONS_PATTERN = Pattern.compile("(@[^@#&]*)|(#([^@#&]*))|(&([^@#&]*))");
    private final Stack<MMNode> stack = new Stack<>();
    private final MMNode rootNode = new MMNode(-1, "Root");
    private MMNode currentNode = rootNode;

    public static MMModel parseModel(String name, String text) {
        var parser = new MindMarkParser();
        return parser.parse(name, text);
    }

    public MMModel parse(String name, String text) {
        var lines = text.split("[\r\n]+");
        for (String line : lines) {
            if (isComments(line)) {
                continue;
            }

            if (isDirective(line)) {
                processForDirective(line);
            } else {
                processForNode(line);
            }
        }
        return new MMModel(name, rootNode.getChildren(), rootNode.getDirectives());
    }

    private void processForDirective(String line) {
        var matcher = DIRECTIVE_PATTERN.matcher(line);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();
        String directive = matcher.group(1);
        String value = matcher.group(2);
        currentNode.addDirective(Directives.parse(directive, value));
    }

    private boolean isDirective(String line) {
        return line.matches("^\\s*@.*$");
    }

    private void processForNode(String line) {
        MMNode mmNode = parseNode(line);
        if (mmNode.getIndent() > currentNode.getIndent()) {
            currentNode.getChildren().add(mmNode);
            stack.push(currentNode);
        } else {
            while (stack.peek().getIndent() >= mmNode.getIndent()) {
                currentNode = stack.pop();
            }
            stack.peek().getChildren().add(mmNode);
        }
        currentNode = mmNode;
    }

    private static MMNode parseNode(String line) {
        var matcher = INDENT_PATTERN.matcher(line);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();

        int indent = matcher.group(1).length();
        String content = matcher.group(2);

        String title = content;
        MMNode node = null;
        matcher = NODE_ADDITIONS_PATTERN.matcher(content);
        while (matcher.find()) {
            if (node == null) {
                title = content.substring(0, matcher.start()).trim();
                node = new MMNode(indent, title);
            }

            String addition = matcher.group();
            if (addition.startsWith("@")) {
                node.addAssignee(addition.substring(1).trim());
                continue;
            }
            if (addition.startsWith("#")) {
                node.addTag(addition.substring(1).trim());
                continue;
            }
            if (addition.startsWith("&")) {
                node.withEstimation(MMEstimation.parse(addition.substring(1).trim()));
            }
        }
        if (node == null) {
            node = new MMNode(indent, title);
        }

        return node;
    }

    private static boolean isComments(String line) {
        return line.matches("^\\s*#.*");
    }
}
