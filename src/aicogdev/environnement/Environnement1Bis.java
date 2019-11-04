package aicogdev.environnement;

/**
 * Un environnement qui renvoie la réaction opposée à l'action faite (action possibles : 1 et 2)
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
