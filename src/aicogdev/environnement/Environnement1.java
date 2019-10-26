package aicogdev.environnement;

import aicogdev.Action;
import aicogdev.Reaction;

public class Environnement1 implements Environnement {
	@Override
	public Reaction agir(Action action) {
		return new Reaction(action.numero);
	}
}
