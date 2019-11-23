package aicogdev.agent;

import aicogdev.interaction.Interaction;
import fr.bruju.util.MapsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * An agent that learns pairs of interactions.
 * It considers that for each (first action, first feedback, second action), there is an unique feedback
 */
public class AgentTP4Rework extends Agent {
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
            return new String[]{"Surprised", Integer.toString(value), "N/A"};
        }

        Map<Integer, PossibleFeedback> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);
        PossibleFeedback possibleFeedback = MapsUtils.getY(expectations, action, PossibleFeedback::new);

        possibleFeedback.registerFeedback(actualFeedback);


        if (expectedFeedback == actualFeedback) {
            // Situation that the agent already knows
            previousInteraction = producedInteraction;
            return new String[]{"Happy", Integer.toString(value), ""};
        } else {
            String type = "Learned";

            // Learn new sequence
            String learnedSequence = "[" + previousInteraction.toString() + ";" + producedInteraction.toString() + "]";

            previousInteraction = producedInteraction;
            return new String[]{type, Integer.toString(value), learnedSequence};
        }
    }

    class PossibleFeedback {
        private static final int NUMBER_OF_FEEDBACK = 2;

        int[] weights = new int[NUMBER_OF_FEEDBACK];

        private final int action;

        PossibleFeedback(int action) {
            this.action = action;
        }

        public void registerFeedback(int feedback) {
            weights[Math.min(feedback, NUMBER_OF_FEEDBACK) - 1]++;
        }

        public int getProclivity() {
            int sum = 0;

            for (int feedback = 0 ; feedback != NUMBER_OF_FEEDBACK ; feedback++) {
                sum = sum + weights[feedback] * valuationSystem.getValue(action, feedback + 1);
            }

            return sum;
        }

        public int getExpectedFeedback() {
            int iMax = 0;

            for (int i = 1; i < weights.length; i++) {
                if (weights[i] > weights[iMax]) {
                    iMax = i;
                }
            }

            return iMax + 1;
        }
    }
}
