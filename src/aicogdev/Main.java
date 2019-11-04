package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentTP2;
import aicogdev.environnement.Environnement;
import aicogdev.environnement.Environnement2;
import aicogdev.environnement.Environnement3;
import aicogdev.tp3.AgentTP3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentTP3();
    	Environnement environnement = new Environnement3();

    	while (true) {
    		int action = agent.getAction();
    		int reaction = environnement.agir(action);
    		agent.recevoirReaction(reaction);
    		Thread.sleep(500);
		}
    }
}
