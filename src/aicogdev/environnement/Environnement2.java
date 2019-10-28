package aicogdev.environnement;

import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;

public class Environnement2 implements Environnement {
    @Override
    public Reaction agir(Action action) {
        switch (action.numero) {
            case 1:
                return new Reaction(1);
            case 2:
                return new Reaction(2);
            case 3:
                return new Reaction(2);
            default:
                throw new IllegalAction();
        }
    }
}
