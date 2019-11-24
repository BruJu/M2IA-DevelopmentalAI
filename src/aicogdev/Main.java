package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentTP4;
import aicogdev.environnement.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentTP4();
    	Environnement environnement = new Environnement4();

    	while (true) {
    		int action = agent.getAction();
    		int reaction = environnement.agir(action);
    		agent.receiveFeedback(reaction);
    		Thread.sleep(500);
		}
    }
}
