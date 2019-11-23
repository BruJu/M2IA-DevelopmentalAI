package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.environnement.*;
import aicogdev.agent.AgentTP3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentTP3();
    	Environnement environnement = new Environnement4();

    	while (true) {
    		int action = agent.getAction();
    		int reaction = environnement.agir(action);
    		agent.receiveFeedback(reaction);
    		Thread.sleep(500);
		}
    }
}
