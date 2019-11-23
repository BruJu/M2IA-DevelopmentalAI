package aicogdev.agent;

import aicogdev.interaction.Interaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An agent that learns pairs of action - feedback.
 */
public class AgentTP2 extends Agent {
    /** Maps action to feedback */
    private Map<Integer, Integer> expectations = new HashMap<>();

    /** Currently explored action */
    private int exploredAction = 1;

    /** Number of times the agent correctly predicted the action */
    private int numberOfTimesRight = 0;

    /** Number of times the agent have to be right to be bored and explore a new action */
    private static final int NUMBER_OF_TIMES_FOR_BORED = 3;

    private ValuationSystem valuationSystem = new ValuationSystem(1, -1, 1, -1, -1, 1);

    @Override
    protected Interaction getDecision() {
        return new Interaction(exploredAction, expectations.getOrDefault(exploredAction, 0));
    }

    @Override
    protected String[] processReaction(int action, int expectedFeedback, int actualFeedback) {
        int value = valuationSystem.getValue(action, actualFeedback);
        String valueString = Integer.toString(value);

        if (Objects.equals(expectedFeedback, actualFeedback)) {
            numberOfTimesRight++;

            if (numberOfTimesRight != NUMBER_OF_TIMES_FOR_BORED) {
                return new String[]{ "Happy", valueString };
            } else {
                // Change current action
                exploredAction = decideNewAction();
                numberOfTimesRight = 0;

                return new String[]{ "Bored", valueString };
            }
        } else {
            expectations.put(action, actualFeedback);
            numberOfTimesRight = 0;

            return new String[]{ "Surprised", valueString };
        }
    }

    private int decideNewAction() {
        // Exploration
        for (int i = 1 ; i <= 3 ; i++) {
            if (!expectations.containsKey(i)) {
                return i;
            }
        }

        // Exploitation
        int bestAction = 0;
        int bestValue = Integer.MIN_VALUE;

        for (int potentialAction = 1 ; potentialAction <= 3 ; potentialAction++) {
            if (potentialAction == exploredAction) {
                continue;
            }

            int valuation = valuationSystem.getValue(potentialAction, expectations.get(potentialAction));

            if (bestAction == 0 || bestValue < valuation) {
                bestAction = potentialAction;
                bestValue = valuation;
            }
        }

        return bestAction;
    }
}
