package aicogdev.environnement;

/**
 * An environment that produces the feedback 1 to the action 1, and 2 for every other action.
 * Accepts 1, 2 and 3 as inputs.
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
