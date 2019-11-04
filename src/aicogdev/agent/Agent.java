package aicogdev.agent;

import aicogdev.interaction.Decision;
import aicogdev.interaction.ResultatInteraction;

public abstract class Agent {
	private Decision decision;

	public int getAction() {
		decision = getDecision();
		return decision.actionChoisie;
	}

    public void recevoirReaction(int reaction) {
        ResultatInteraction resultat = processReaction(decision.actionChoisie, decision.reactionAttendue, reaction);

        System.out.println(decision.actionChoisie + " ; "
                + decision.reactionAttendue + " ; "
                + resultat.toString());
    }

    protected abstract Decision getDecision();

	protected abstract ResultatInteraction processReaction(int actionFaite, int reactionAttendue, int reactionRecue);

	protected void printLog() { }
}
