package aicogdev.environnement;

import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;

public class Environnement2 implements Environnement {
	@Override
	public Reaction agir(Action action) {
		return new Reaction(action.numero - 1);
	}
}
