package aicogdev.agent;

import aicogdev.interaction.Interaction;

public abstract class Agent {
	private Interaction interactionAttendue;

	public int getAction() {
		interactionAttendue = getDecision();
		return interactionAttendue.action;
	}

    public void recevoirReaction(int reaction) {
        String resultat = processReaction(interactionAttendue.action, interactionAttendue.reaction, reaction);

        System.out.println(interactionAttendue.action + " ; "
                + interactionAttendue.reaction + " ; "
                + resultat);
    }

    protected abstract Interaction getDecision();

	protected abstract String processReaction(int actionFaite, int reactionAttendue, int reactionRecue);
}
