package aicogdev.tp3;

import aicogdev.agent.Agent;
import aicogdev.agent.Feedback;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Interaction;
import aicogdev.interaction.ResultatInteraction;

import java.util.*;

public class AgentTP3 extends Agent {
    private Feedback feedback = new Feedback(new int[] { -1, 1, -1, 1 });

    private Set<Sequence> learnedInteractions = new HashSet<>();

    private Interaction derniereInteraction = null;



    @Override
    protected Decision getDecision() {
        if (derniereInteraction == null) {
            return new Decision(1, 0);
        } else {
            Interaction i = trouverInteractionSuivante();
            return new Decision(i.action, i.reaction);
        }
    }

    private Interaction trouverInteractionSuivante() {
        List<Sequence> avoid = new ArrayList<>();
        List<Sequence> exploit = new ArrayList<>();

        for (Sequence s : learnedInteractions) {
            if (s.premiere.equals(derniereInteraction)) {
                int expectedFeedback = feedback.getValue(s.seconde);

                if (expectedFeedback < 0) {
                    avoid.add(s);
                } else {
                    exploit.add(s);
                }
            }
        }

        if (exploit.isEmpty()) {
            Set<Integer> choices = new TreeSet<>();
            choices.add(1);
            choices.add(2);

            for (Sequence toAvoid : avoid) {
                choices.remove(toAvoid.seconde.action);
            }

            for (Integer i : choices) {
                return new Interaction(i, 0);
            }

            return new Interaction(1, 0);
        } else {
            return new Interaction(exploit.get(0).seconde.action, exploit.get(0).seconde.reaction);
        }
    }

    @Override
    protected ResultatInteraction processReaction(int actionFaite, int reactionAttendue, int reactionRecue) {
        Interaction obtenue = new Interaction(actionFaite, reactionRecue);

        int feedback = this.feedback.getValue(actionFaite, reactionRecue);

        if (derniereInteraction != null) {
            Sequence learnedSequence = new Sequence(derniereInteraction, obtenue);
            this.learnedInteractions.add(learnedSequence);
        }

        derniereInteraction = obtenue;

        return new ResultatInteraction(reactionAttendue == reactionRecue, feedback, false);
    }
}
