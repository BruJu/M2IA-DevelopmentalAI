package aicogdev.environnement;

import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;

public class Environnement1 implements Environnement {
	@Override
	public Reaction agir(Action action) {
		return new Reaction(action.numero);
	}
}
