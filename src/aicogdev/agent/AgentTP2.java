package aicogdev.agent;

import aicogdev.interaction.Action;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Reaction;
import aicogdev.interaction.ResultatInteraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP2 extends Agent {
    private Feedback feedback;

    private Map<Action, Reaction> attentes = new HashMap<>();

    private Action actionActuellementExploree = new Action(1);
    private int numberOfTimesRight;

    public AgentTP2() {
        feedback = new Feedback();
        feedback.register(1,1, 1)
                .register(1,2,1)
                .register(2,1,1)
                .register(2,2,-1)
                .register(3,1,-1)
                .register(3,2,1);
    }


    @Override
    protected Decision getDecision() {
        return new Decision(actionActuellementExploree, attentes.get(actionActuellementExploree));
    }

    @Override
    protected ResultatInteraction processReaction(Action actionFaite, Reaction reactionAttendue, Reaction reactionRecue) {
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

    private Action changerDAction() {
        // Exploration
        for (int i = 1 ; i <= 3 ; i++) {
            if (!attentes.containsKey(new Action(i))) {
                return new Action(i);
            }
        }

        // Exploitation
        int bestAction = 0;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 1 ; i <= 3 ; i++) {
            if (i == actionActuellementExploree.numero) {
                continue;
            }

            int valuation = evaluerValeurAttendue(i);
            System.out.println("===");

            if (bestAction == 0 || bestValue < valuation) {
                bestAction = i;
                bestValue = valuation;
            }
        }

        return new Action(bestAction);
    }

    private int evaluerValeurAttendue(int i) {
        return feedback.getValue(new Action(i), attentes.get(new Action(i)));
    }

}
