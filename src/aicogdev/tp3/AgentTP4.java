package aicogdev.tp3;

import aicogdev.agent.Agent;
import aicogdev.agent.Feedback;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Interaction;
import aicogdev.interaction.ResultatInteraction;

public class AgentTP4 extends Agent {

    private Feedback feedback;

    private PossibleInteractions interactionsManager;
    private Interaction interactionPrecedente;

    public AgentTP4() {
        feedback = new Feedback(new int[] {1,1,-1,-1});
        interactionsManager = new PossibleInteractions(feedback);
        interactionPrecedente = null;
    }

    @Override
    protected Decision getDecision() {
        Interaction suivante = interactionsManager.deciderAction(interactionPrecedente);

        return new Decision(suivante.action, suivante.reaction);
    }

    @Override
    protected ResultatInteraction processReaction(int actionFaite, int reactionAttendue, int reactionRecue) {

        if (interactionPrecedente != null)
            interactionsManager.registerSequence(interactionPrecedente, new Interaction(actionFaite, reactionRecue));

        interactionPrecedente = new Interaction(actionFaite, reactionRecue);

        return new ResultatInteraction(reactionAttendue == reactionRecue,

                feedback.getValue(actionFaite, reactionRecue),
                false
                );
    }
}
