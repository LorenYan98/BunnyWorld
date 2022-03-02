package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Script {
    public static final String ON_CLICK = "onClick";
    public static final String ON_DROP = "onDrop";
    public static final String ON_ENTER = "onEnter";

    boolean isOnClick = false;
    boolean isOnDrop = false;
    boolean isOnEnter = false;
    List<String> onClickActions, onEnterActions;
    Map<String, List<String>> onDropActions;

    public Script(String scriptString) {
        onClickActions = new ArrayList<>();
        onEnterActions = new ArrayList<>();
        onDropActions = new HashMap<>();
        parseScriptString(scriptString);
    }

    private void parseScriptString(String scriptString) {
        if (scriptString == "") return;
        for (String clause : scriptString.split(";")) {
            if (clause.length() == 0) return;
            String[] temp = clause.split(" ");
            String trigger = temp[0];
            String[] actions = Arrays.copyOfRange(temp, 1, temp.length);
            switch (trigger) {
                case ON_CLICK:
                    isOnClick = true;
                    onClickActions.addAll(Arrays.asList(actions));
                    break;
                case ON_ENTER:
                    isOnEnter = true;
                    onEnterActions.addAll(Arrays.asList(actions));
                    break;
                case ON_DROP:
                    isOnDrop = true;
                    String targetShape = actions[0];
                    String[] tempDropActions = Arrays.copyOfRange(actions, 1, actions.length);
                    onDropActions.put(targetShape, Arrays.asList(tempDropActions));
            }
        };

    }

    public boolean isOnClick() {
        return isOnClick;
    }

    public void setOnClick(boolean onClick) {
        isOnClick = onClick;
    }

    public boolean isOnDrop() {
        return isOnDrop;
    }

    public void setOnDrop(boolean onDrop) {
        isOnDrop = onDrop;
    }

    public boolean isOnEnter() {
        return isOnEnter;
    }

    public void setOnEnter(boolean onEnter) {
        isOnEnter = onEnter;
    }

    public List<String> getOnClickActions() {
        return onClickActions;
    }

    public void setOnClickActions(List<String> onClickActions) {
        this.onClickActions = onClickActions;
    }

    public List<String> getOnEnterActions() {
        return onEnterActions;
    }

    public void setOnEnterActions(List<String> onEnterActions) {
        this.onEnterActions = onEnterActions;
    }

    public Map<String, List<String>> getOnDropActions() {
        return onDropActions;
    }

    public void setOnDropActions(Map<String, List<String>> onDropActions) {
        this.onDropActions = onDropActions;
    }
}
