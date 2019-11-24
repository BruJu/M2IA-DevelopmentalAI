package aicogdev.environnement;

import aicogdev.agent.Agent;

/**
 * An environment that acts like the environment 3, then switch to the environment 1.
 */
public class Environnement4 implements Environnement {
    private static final int NUMBER_OF_ACTIONS_BEFORE_SWITCH = 10;

    private Environnement simulatedEnvironment = new Environnement1();
    private int numberOfActions = 0;

    @Override
    public int agir(int action) {
        if (numberOfActions == NUMBER_OF_ACTIONS_BEFORE_SWITCH) {
            simulatedEnvironment = new Environnement3();
            if (Agent.PRODUCE_TRACE) {
                System.out.println("| Environment change |");
            }
        }

        numberOfActions++;

        return simulatedEnvironment.agir(action);
    }
}
