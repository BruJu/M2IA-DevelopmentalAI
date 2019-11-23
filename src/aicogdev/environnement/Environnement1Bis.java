package aicogdev.environnement;

/**
 * An environment that produces an opposite feedback from the action.
 * Accepts 1 and 2 as input.
 */
public class Environnement1Bis implements Environnement {
	@Override
	public int agir(int action) {
		switch (action) {
			case 1: return 2;
			case 2: return 1;
			default : throw new IllegalAction();
		}
	}
}
