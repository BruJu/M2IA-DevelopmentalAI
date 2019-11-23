package aicogdev.environnement;

/**
 * An environment that returns 1 if the action is the same as the previous one and 2 if it is different.
 */
public class Environnement3 implements Environnement {
    private int lastAction = 0;

    @Override
    public int agir(int action) {
        if (action == lastAction) {
            return 1;
        } else {
            lastAction = action;
            return 2;
        }
    }
}
