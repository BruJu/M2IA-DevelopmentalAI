package aicogdev.agent;

import aicogdev.interaction.Interaction;
import fr.bruju.util.ListSearch;
import fr.bruju.util.MapsUtils;

import java.util.*;

/**
 * An agent that learns pairs of interactions.
 * It considers that for each (first action, first feedback, second action), there is an unique feedback
 */
public class AgentTP4 extends Agent {
    /** Learned sequences of interactions */
    private Map<Interaction, Map<Integer, PossibleFeedback>> learnedInteractions = new HashMap<>();

    /** Valuation of interactions */
    private ValuationSystem valuationSystem = new ValuationSystem(-1, 1, -1, 1);

    /** Previous interaction : used to compute the next one */
    private Interaction previousInteraction = null;

    /** Number of possibles actions */
    private static final int NUMBER_OF_ACTIONS = 2;

    @Override
    protected Interaction getDecision() {
        if (previousInteraction == null) {
            return new Interaction(1, 0);
        }

        Map<Integer, PossibleFeedback> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);

        int bestAction = 1;
        int expectedFeedback = 0;
        int valueBestAction = Integer.MIN_VALUE;

        for (int action = 1 ; action <= NUMBER_OF_ACTIONS ; action++) {
            int proclivity = MapsUtils.getY(expectations, action, PossibleFeedback::new).getProclivity();

            if (valueBestAction < proclivity) {
                bestAction = action;
                expectedFeedback = MapsUtils.getY(expectations, action, PossibleFeedback::new).getExpectedFeedback();
                valueBestAction = proclivity;
            }
        }

        return new Interaction(bestAction, expectedFeedback);
    }

    @Override
    protected String[] processReaction(int action, int expectedFeedback, int actualFeedback) {
        Interaction producedInteraction = new Interaction(action, actualFeedback);
        int value = this.valuationSystem.getValue(producedInteraction);

        if (previousInteraction == null) {
            // No previous interaction : can't learn sequence
            previousInteraction = producedInteraction;
            return new String[]{"Surprised", Integer.toString(value), "N/A", "N/A"};
        }

        // Get intern representation of the current state
        Map<Integer, PossibleFeedback> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);
        PossibleFeedback possibleFeedback = MapsUtils.getY(expectations, action, PossibleFeedback::new);

        // Trace
        String type = expectedFeedback == actualFeedback ? "Happy" : "Surprised";
        String prefix = previousInteraction.toString();
        String suffix = possibleFeedback.toString(expectedFeedback, actualFeedback);

        // Update knowledge
        possibleFeedback.registerFeedback(actualFeedback);
        previousInteraction = producedInteraction;

        return new String[]{type, Integer.toString(value), prefix, suffix};
    }

    // ========================
    // POSSIBILITIES MANAGEMENT

    /**
     * This class encapsultes the information concerning the possible feedback for a given triple
     * (First Interaction, First Feedback, Second Interaction).
     */
    class PossibleFeedback {
        /** Total number of possible feedback */
        private static final int NUMBER_OF_FEEDBACK = 2;

        /** ID of the second action (to be able to evaluate the interactions) */
        private final int action;

        /** Number of time each feedback occurend */
        int[] weights = new int[NUMBER_OF_FEEDBACK];

        /**
         * Creates a possible feedback manager
         * @param action The action that produces the described feeback
         */
        private PossibleFeedback(int action) {
            this.action = action;
        }

        /**
         * Add 1 to the weight of the feedback
         * @param feedback The feedback
         */
        public void registerFeedback(int feedback) {
            weights[Math.min(feedback, NUMBER_OF_FEEDBACK) - 1]++;
        }

        /**
         * Returns the value of the interaction number feedback
         * @param feedback The feedback
         * @return The value (weight * preference)
         */
        private int getProclivity(int feedback) {
            return weights[feedback - 1] * valuationSystem.getValue(action, feedback);
        }

        /**
         * Returns the total value of every interaction that this action can produce
         * @return The total value
         */
        public int getProclivity() {
            int sum = 0;

            for (int feedback = 0 ; feedback != NUMBER_OF_FEEDBACK ; feedback++) {
                sum = sum + getProclivity(feedback + 1);
            }

            return sum;
        }

        /**
         * Returns the number of the expected feedback (the one that occurs the most)
         * @return The number of the expected feedback
         */
        public int getExpectedFeedback() {
            return ListSearch.searchMaxValue(weights.length, i -> weights[i]) + 1;
        }

        /**
         * Stringify the information for a given interactin this action x the given feedback
         */
        private String toStringFeedback(int feedback) {
            String s = "" + weights[feedback - 1] + "x" + valuationSystem.getValue(action, feedback) + "=";
            int value = getProclivity(feedback);
            return "I" + action + "" + feedback  + " (" + s + value + ")";
        }

        /**
         * Stringify the situation of this representation considering that the agent expected a
         * feedback and got the actual feedback.
         */
        public String toString(int expected, int actual) {
            StringJoiner sj = new StringJoiner("  ");

            for (int feedback = 0 ; feedback != NUMBER_OF_FEEDBACK ; feedback++) {
                String s = toStringFeedback(feedback + 1);

                if (actual == feedback + 1) {
                    s = s + "<--";
                } else {
                    s = s + "   ";
                }

                if (expected == feedback + 1) {
                    s = "<--" + s;
                } else {
                    s = "  " + s;
                }

                sj.add(s);
            }

            return sj.toString();
        }
    }
}
