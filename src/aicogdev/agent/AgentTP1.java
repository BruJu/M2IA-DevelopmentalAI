package aicogdev.agent;

import aicogdev.interaction.Interaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An agent that learns pairs of action - feedback.
 */
public class AgentTP1 extends Agent {
    /** Maps action to feedback */
	private Map<Integer, Integer> expectations = new HashMap<>();

	/** Currently explored action */
	private int exploredAction = 1;

	/** Number of times the agent correctly predicted the action */
	private int numberOfTimesRight = 0;

	/** Number of times the agent have to be right to be bored and explore a new action */
	private static final int NUMBER_OF_TIMES_FOR_BORED = 3;

	@Override
	protected Interaction getDecision() {
		return new Interaction(exploredAction, expectations.getOrDefault(exploredAction, 0));
	}

	@Override
	protected String processReaction(int action, int expectedFeedback, int actualFeedback) {
		if (Objects.equals(expectedFeedback, actualFeedback)) {
			numberOfTimesRight++;

			if (numberOfTimesRight != NUMBER_OF_TIMES_FOR_BORED) {
			    return "Happy";
            } else {
			    // Change current action
                exploredAction = exploredAction == 1 ? 2 : 1;
                numberOfTimesRight = 0;

			    return "Bored";
            }
		} else {
			expectations.put(action, actualFeedback);
			numberOfTimesRight = 0;

			return "Surprised";
		}
	}
}
