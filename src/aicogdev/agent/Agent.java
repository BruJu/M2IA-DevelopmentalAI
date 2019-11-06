package aicogdev.agent;

import aicogdev.interaction.Decision;

public abstract class Agent {
	private Decision decision;

	public int getAction() {
		decision = getDecision();
		return decision.actionChoisie;
	}

    public void recevoirReaction(int reaction) {
        String resultat = processReaction(decision.actionChoisie, decision.reactionAttendue, reaction);

        System.out.println(decision.actionChoisie + " ; "
                + decision.reactionAttendue + " ; "
                + resultat);
    }

    protected abstract Decision getDecision();

	protected abstract String processReaction(int actionFaite, int reactionAttendue, int reactionRecue);

	protected void printLog() { }
}
