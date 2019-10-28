package aicogdev.agent;

import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;
import fr.bruju.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Feedback {
    private Map<Pair<Action, Reaction>, Integer> table = new HashMap<>();

    public Feedback register(Action action, Reaction reaction, int value) {
        table.put(new Pair<>(action, reaction), value);
        return this;
    }

    public Feedback register(int action, int reaction, int value) {
        return register(new Action(action), new Reaction(reaction), value);
    }

    public int getValue(Action action, Reaction reaction) {
        return table.getOrDefault(new Pair<>(action, reaction), 0);
    }
}
