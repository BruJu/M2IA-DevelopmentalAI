package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentTP4Rework;
import aicogdev.environnement.*;
import aicogdev.agent.AgentTP3;
import aicogdev.tp3.AgentTP4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentTP4Rework();
    	Environnement environnement = new Environnement4();

    	while (true) {
    		int action = agent.getAction();
    		int reaction = environnement.agir(action);
    		agent.receiveFeedback(reaction);
    		Thread.sleep(500);
		}
    }
}
