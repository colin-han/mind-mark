package info.colinhan.mindmark.util;

public class MindMarkParseException extends RuntimeException {
    public MindMarkParseException(String message) {
        super(message);
    }

    public MindMarkParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
