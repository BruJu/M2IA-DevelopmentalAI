package aicogdev.environnement;

/**
 * Un environnement qui agit comme l'environnement 1, puis au bout de 30 actions se met à simuler l'environment 3
 */
public class Environnement4 implements Environnement {
    private Environnement environnementSimule = new Environnement1();

    private int number_of_actions = 0;

    @Override
    public int agir(int action) {
        if (number_of_actions == 10) {
            environnementSimule = new Environnement3();
        }

        number_of_actions++;

        return environnementSimule.agir(action);
    }
}
