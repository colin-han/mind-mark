package info.colinhan.mindmark.processor;

import info.colinhan.mindmark.model.*;

public class AutoNumberProcessor extends ToggleProcessor<String> {
    public static void applyTo(MMModel model) {
        new AutoNumberProcessor().process(model);
    }

    @Override
    protected String calcNewValue(boolean toggle, String currentValue, int i) {
        return toggle ? currentValue + (i + 1) + "." : "";
    }

    @Override
    protected void applyToggle(MMNode node, String currentValue) {
        node.withTitlePrefix(currentValue);
    }

    @Override
    protected boolean isToggle(MMToggle t) {
        return t.getName().equalsIgnoreCase("AutoNumber");
    }
}
