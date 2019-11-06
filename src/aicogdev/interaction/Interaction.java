package aicogdev.interaction;

import java.util.Objects;

public class Interaction {
    public final int action;
    public final int reaction;

    public Interaction(int action, int reaction) {
        this.action = action;
        this.reaction = reaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaction that = (Interaction) o;
        return action == that.action &&
                reaction == that.reaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, reaction);
    }

    @Override
    public String toString() {
    	return ("I" + action) + reaction;
	}
}
