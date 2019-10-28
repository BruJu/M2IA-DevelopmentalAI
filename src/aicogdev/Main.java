package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentUnique;
import aicogdev.environnement.Environnement;
import aicogdev.environnement.Environnement2;
import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;

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
