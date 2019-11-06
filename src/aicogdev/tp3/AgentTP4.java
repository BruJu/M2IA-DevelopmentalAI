package aicogdev.tp3;

import aicogdev.agent.Agent;
import aicogdev.agent.Feedback;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Interaction;

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
    protected String processReaction(int actionFaite, int reactionAttendue, int reactionRecue) {


		Interaction obtenue = new Interaction(actionFaite, reactionRecue);

		String patternAppris = interactionPrecedente == null ? "N/A ; N/A"
				: "[" + interactionPrecedente.toString() + ", " + obtenue.toString() + "]";

		if (interactionPrecedente != null) {
			patternAppris += " ; " + interactionsManager.stringifyValences(interactionPrecedente, actionFaite, reactionAttendue, reactionRecue);
		}

		if (interactionPrecedente != null)
			interactionsManager.registerSequence(interactionPrecedente, new Interaction(actionFaite, reactionRecue));

		interactionPrecedente = obtenue;

		int feedback = this.feedback.getValue(actionFaite, reactionRecue);
		return (reactionAttendue == reactionRecue ? "Content" : "Surpris")
				+ " ; " + feedback
				+ " ; " + patternAppris;
    }
}
