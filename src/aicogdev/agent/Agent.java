package aicogdev.agent;

import aicogdev.interaction.Action;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Reaction;
import aicogdev.interaction.Status;

public abstract class Agent {
	private Decision decision;

	public Action getAction() {
		decision = getDecision();
		return decision.actionChoisie;
	}

	protected abstract Decision getDecision();

	public Status getFeedback(Reaction reaction) {
		if (decision.reactionAttendue == null) {
			return Status.surprised;
		} else {
			return decision.reactionAttendue.equals(reaction) ? Status.happy : Status.surprised;
		}
	}

	public void setReaction(Reaction reaction) {
		Status isHappy = getFeedback(reaction);

		isHappy = processReaction(decision, reaction, isHappy);

		System.out.println(decision.actionChoisie + " ; "
				+ decision.reactionAttendue + " ; "
				+ reaction + " ; " + isHappy);
	}

	protected abstract Status processReaction(Decision decision, Reaction reaction, Status status);
}
