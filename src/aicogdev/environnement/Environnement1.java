package aicogdev.environnement;

/**
 * Un environnement qui renvoie la réaction égale à l'action faite. Actions possibles : 1 et 2
 */
public class Environnement1 implements Environnement {
	@Override
	public int agir(int action) {
		switch (action) {
			case 1: return 1;
			case 2: return 2;
			default : throw new IllegalAction();
		}
	}
}
