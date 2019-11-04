package aicogdev.environnement;

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
