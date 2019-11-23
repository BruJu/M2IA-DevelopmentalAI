package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentTP1;
import aicogdev.agent.AgentTP2;
import aicogdev.environnement.*;
import aicogdev.tp3.AgentTP3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    	Agent agent = new AgentTP2();
    	Environnement environnement = new Environnement2();

    	while (true) {
    		int action = agent.getAction();
    		int reaction = environnement.agir(action);
    		agent.receiveFeedback(reaction);
    		Thread.sleep(500);
		}
    }
}
