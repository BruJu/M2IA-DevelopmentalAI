package aicogdev.environnement;

/**
 * Un environnement qui renvoie 1 si l'action 1 est faite, 2 si l'action 2 ou 3 est faite
 */
public class Environnement2 implements Environnement {
    @Override
    public int agir(int action) {
        switch (action) {
            case 1: return 1;
            case 2: return 2;
            case 3: return 2;
            default: throw new IllegalAction();
        }
    }
}
