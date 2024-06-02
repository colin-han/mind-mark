package info.colinhan.mindmark.model;

import lombok.Getter;

@Getter
public class MMTag {
    private final String name;

    public MMTag(String name) {
        this.name = name;
    }
}
