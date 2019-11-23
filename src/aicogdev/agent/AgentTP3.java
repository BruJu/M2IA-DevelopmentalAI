package aicogdev.agent;

import aicogdev.agent.Agent;
import aicogdev.agent.ValuationSystem;
import aicogdev.interaction.Interaction;
import fr.bruju.util.MapsUtils;
import fr.bruju.util.Pair;

import java.util.*;

/**
 * An agent that learns pairs of interactions.
 * It considers that for each (first action, first feedback, second action), there is an unique feedback
 */
public class AgentTP3 extends Agent {
    /** Learned sequences of interactions */
    private Map<Interaction, Map<Integer, Integer>> learnedInteractions = new HashMap<>();

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

        Map<Integer, Integer> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);

        int bestAction = 1;
        int expectedFeedback = 0;
        int valueBestAction = Integer.MIN_VALUE;

        for (int action = 1 ; action <= NUMBER_OF_ACTIONS ; action++) {
            int feedback = expectations.getOrDefault(action, 0);

            int value = feedback == 0 ? 0 : valuationSystem.getValue(action, feedback);

            if (valueBestAction < value) {
                bestAction = action;
                expectedFeedback = feedback;
                valueBestAction = value;
            }
        }

        return new Interaction(bestAction, expectedFeedback);
    }

    @Override
    protected String[] processReaction(int action, int expectedFeedback, int actualFeedback) {
        Interaction producedInteraction = new Interaction(action, actualFeedback);
        int value = this.valuationSystem.getValue(producedInteraction);

        if (expectedFeedback == actualFeedback) {
            // Situation that the agent already knows
            previousInteraction = producedInteraction;
            return new String[]{"Happy", Integer.toString(value), ""};
        } else if (previousInteraction == null) {
            // No previous interaction : can't learn sequence
            previousInteraction = producedInteraction;
            return new String[]{"Surprised", Integer.toString(value), "N/A"};
        } else {
            String type = "Learned";

            // Learn new sequence
            Map<Integer, Integer> expectations = MapsUtils.getX(learnedInteractions, previousInteraction, HashMap::new);

            if (expectedFeedback != 0) { type = "Revised"; }
            expectations.put(action, actualFeedback);

            String learnedSequence = "[" + previousInteraction.toString() + ";" + producedInteraction.toString() + "]";

            previousInteraction = producedInteraction;
            return new String[]{type, Integer.toString(value), learnedSequence};
        }
    }
}
