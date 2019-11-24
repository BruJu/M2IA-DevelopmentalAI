package aicogdev.agent;

import aicogdev.interaction.Interaction;
import fr.bruju.util.ListSearch;
import fr.bruju.util.MapsUtils;

import java.util.*;

/**
 * An agent that learns the number of occurences of each seuenences of interaction to choose the best
 * action using past experience and the previous interaction.
 */
public class AgentTP4 extends Agent {
    /** Learned sequences of interactions */
    private Map<Interaction, Map<Integer, PossibleFeedback>> learnedInteractions = new HashMap<>();

    /** Valuation of interactions */
    private ValuationSystem valuationSystem;

    /** Previous interaction : used to compute the next one */
    private Interaction previousInteraction = null;

    /** Number of possibles actions */
    private static final int NUMBER_OF_ACTIONS = 2;

    public AgentTP4() {
        this("TP4-Values1");
    }

    public AgentTP4(String valueSystem) {
        valuationSystem = new ValuationSystem(valueSystem);
    }

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
            return new String[]{"Surprised", Integer.toString(value), "N/A", "N/A", "N/A"};
        }

        // Get intern representation of the current state
        Map<Integer, PossibleFeedback> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);
        PossibleFeedback possibleFeedback = MapsUtils.getY(expectations, action, PossibleFeedback::new);

        // Trace
        String type = expectedFeedback == actualFeedback ? "Happy" : "Surprised";
        String prefix = previousInteraction.toString();
        String[] suffixes = new String[NUMBER_OF_ACTIONS];
        for (int idaction = 0 ; idaction != NUMBER_OF_ACTIONS ; idaction++) {
            int internFeedback = (idaction + 1 == action) ? actualFeedback : -1;
            if (expectedFeedback == actualFeedback)
                internFeedback = -1;

            suffixes[idaction] = MapsUtils.getY(expectations, idaction + 1, PossibleFeedback::new).toString(internFeedback);
        }

        // Update knowledge
        possibleFeedback.registerFeedback(actualFeedback);
        previousInteraction = producedInteraction;

        String[] displayArray = new String[3 + suffixes.length];
        displayArray[0] = type;
        displayArray[1] = Integer.toString(value);
        displayArray[2] = prefix;
        for (int i = 0; i < suffixes.length; i++) {
            displayArray[i + 3] = suffixes[i];
        }
        return displayArray;
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

        public String toString(int highlight) {
            String calc = "";

            for (int feedback = 0 ; feedback != NUMBER_OF_FEEDBACK ; feedback++) {
                int proclivity = getProclivity(feedback + 1);

                if (proclivity >= 0 && !calc.equals("")) {
                    calc += "+";
                } else if (proclivity < 0) {
                    calc += "-";
                }

                String symbol = highlight == feedback + 1 ? "**" : "";

                calc = calc + symbol + Math.abs(proclivity) + symbol;
            }

            int proclivity = getProclivity();

            return calc + "=" + proclivity;
        }
    }
}
