package aicogdev.agent;

import aicogdev.interaction.Decision;
import aicogdev.interaction.ResultatInteraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP2 extends Agent {
    private Feedback feedback = new Feedback(new int[]{
            1, 1,
            1, -1,
            -1, 1
    });

    private Map<Integer, Integer> attentes = new HashMap<>();

    private int actionActuellementExploree = 1;
    private int numberOfTimesRight;

    public AgentTP2() {
    }


    @Override
    protected Decision getDecision() {
        return new Decision(actionActuellementExploree, attentes.getOrDefault(actionActuellementExploree, 0));
    }

    @Override
    protected ResultatInteraction processReaction(int actionFaite, int reactionAttendue, int reactionRecue) {
        int recompense = feedback.getValue(actionFaite, reactionRecue);

        if (Objects.equals(reactionAttendue, reactionRecue)) {
            numberOfTimesRight++;

            boolean isBored = false;

            if (numberOfTimesRight == 3) {
                isBored = true;

                actionActuellementExploree = changerDAction();

                numberOfTimesRight = 0;
            }

            return new ResultatInteraction(true, recompense, isBored);
        } else {
            attentes.put(actionFaite, reactionRecue);
            numberOfTimesRight = 0;

            return new ResultatInteraction(false, recompense, false);
        }
    }

    private int changerDAction() {
        // Exploration
        for (int i = 1 ; i <= 3 ; i++) {
            if (!attentes.containsKey(i)) {
                return i;
            }
        }

        // Exploitation
        int bestAction = 0;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 1 ; i <= 3 ; i++) {
            if (i == actionActuellementExploree) {
                continue;
            }

            int valuation = evaluerValeurAttendue(i);
            System.out.println("===");

            if (bestAction == 0 || bestValue < valuation) {
                bestAction = i;
                bestValue = valuation;
            }
        }

        return bestAction;
    }

    private int evaluerValeurAttendue(int i) {
        return feedback.getValue(i, attentes.get(i));
    }

}
