package aicogdev.agent;

import aicogdev.interaction.Action;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Reaction;
import aicogdev.interaction.ResultatInteraction;

public abstract class Agent {
	private Decision decision;

	public Action getAction() {
		decision = getDecision();
		return decision.actionChoisie;
	}

    public void setReaction(Reaction reaction) {
        ResultatInteraction resultat = processReaction(decision.actionChoisie, decision.reactionAttendue, reaction);

        System.out.println(decision.actionChoisie + " ; "
                + decision.reactionAttendue + " ; "
                + resultat.toString());
    }

    protected abstract Decision getDecision();

	protected abstract ResultatInteraction processReaction(Action actionFaite, Reaction reactionAttendue,
                                                           Reaction reactionRecue);
}
