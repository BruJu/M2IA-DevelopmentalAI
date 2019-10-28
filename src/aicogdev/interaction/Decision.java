package aicogdev.interaction;

public class Decision {
	public final Action actionChoisie;
	public final Reaction reactionAttendue;

	public Decision(Action actionChoisie, Reaction reactionAttendue) {
		this.actionChoisie = actionChoisie;
		this.reactionAttendue = reactionAttendue;
	}
}
