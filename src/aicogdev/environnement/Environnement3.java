package aicogdev.environnement;

public class Environnement3 implements Environnement {
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
