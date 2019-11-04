package aicogdev.environnement;

/**
 * Un environnement qui renvoie 1 si la dernière action est égale à l'action en cours, 2 sinon
 */
public class Environnement3 implements Environnement {
    /**
     * Dernière action produite
     */
    int derniereAction = 0;

    @Override
    public int agir(int action) {
        if (action == derniereAction) {
            return 1;
        } else {
            derniereAction = action;
            return 2;
        }
    }
}
