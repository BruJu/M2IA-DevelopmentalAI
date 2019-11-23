package aicogdev.agent;

import aicogdev.interaction.Interaction;

import java.util.StringJoiner;

/**
 * Represents an agent that can require an action and consider the feedback
 */
public abstract class Agent {
    // ==== Unified interface between every agents

    /** Expected interaction (couple made action - expected feedback) */
	private Interaction expectedInteraction;

    /**
     * Returns the number of the next action to do
     * @return The number of the next action
     */
	public final int getAction() {
		expectedInteraction = getDecision();
		return expectedInteraction.action;
	}

    /**
     * Gives the feedback produced by the environement from the action that has been made
     * @param reaction The produced feedback
     */
    public final void receiveFeedback(int reaction) {
        String[] resultats = processReaction(expectedInteraction.action, expectedInteraction.reaction, reaction);

        StringJoiner sj = new StringJoiner(" | ", "| ", " |");
        sj.add(Integer.toString(expectedInteraction.action));
        sj.add(Integer.toString(expectedInteraction.reaction));
        sj.add(Integer.toString(reaction));

        for (String resultat : resultats) {
            sj.add(resultat);
        }

        System.out.println(sj.toString());
    }

    // ==== Implementation of agents

    /**
     * Returns the next interaction that the agent wants to make
     * @return An interaction with the wanted action and the expected feedback from it
     */
    protected abstract Interaction getDecision();

    /**
     * Give to the agent's implementation the actual feedback from the produced interaction
     * @param action The action required by the agent
     * @param expectedFeedback The feedback the agent expected to have
     * @param actualFeedback The feedback produced by the environment
     * @return The strings that represents some notes to print to the user about the agent internal state
     */
	protected abstract String[] processReaction(int action, int expectedFeedback, int actualFeedback);
}
