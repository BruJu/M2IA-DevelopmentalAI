package aicogdev;

import aicogdev.environnement.Environnement;
import aicogdev.environnement.Environnement1;
import aicogdev.environnement.Environnement2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentUnique();
    	Environnement environnement = new Environnement2();

    	while (true) {
    		Action action = agent.getAction();
    		Reaction reaction = environnement.agir(action);
    		agent.setReaction(reaction);
    		Thread.sleep(500);
		}
    }
}
