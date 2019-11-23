package aicogdev.tp3;

import aicogdev.agent.Agent;
import aicogdev.agent.ValuationSystem;
import aicogdev.interaction.Interaction;

public class AgentTP4 extends Agent {

    private ValuationSystem valuationSystem;

    private PossibleInteractions interactionsManager;
    private Interaction interactionPrecedente;

    public AgentTP4() {
        valuationSystem = new ValuationSystem(new int[] {1,1,-1,-1});
        interactionsManager = new PossibleInteractions(valuationSystem);
        interactionPrecedente = null;
    }

    @Override
    protected Interaction getDecision() {
        Interaction suivante = interactionsManager.deciderAction(interactionPrecedente);

        return new Interaction(suivante.action, suivante.reaction);
    }

    @Override
    protected String[] processReaction(int action, int expectedFeedback, int actualFeedback) {


		Interaction obtenue = new Interaction(action, actualFeedback);

		String patternAppris = interactionPrecedente == null ? "N/A ; N/A"
				: "[" + interactionPrecedente.toString() + ", " + obtenue.toString() + "]";

		if (interactionPrecedente != null) {
			patternAppris += " ; " + interactionsManager.stringifyValences(interactionPrecedente, action, expectedFeedback, actualFeedback);
		}

		if (interactionPrecedente != null)
			interactionsManager.registerSequence(interactionPrecedente, new Interaction(action, actualFeedback));

		interactionPrecedente = obtenue;

		int feedback = this.valuationSystem.getValue(action, actualFeedback);
		return new String[] { (expectedFeedback == actualFeedback ? "Content" : "Surpris")
				+ " ; " + feedback
				+ " ; " + patternAppris};
    }
}
