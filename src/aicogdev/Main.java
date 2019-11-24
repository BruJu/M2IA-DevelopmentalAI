package aicogdev;

import aicogdev.agent.Agent;
import aicogdev.agent.AgentTP4;
import aicogdev.environnement.*;
import fr.bruju.util.Pair;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // tracedExecution(new AgentTP4(), new Environnement4());
        checkPerformance();
    }

    private static void checkPerformance() {
        Function<String, Agent> agentCreator = AgentTP4::new;
        String[] valueSystems = new String[] { "TP4-Values1", "TP4-Values2", "TP4-Values3" };
        Supplier<Environnement>[] environmentCreators =
                new Supplier[] {
                    Environnement1::new,
                    Environnement1Bis::new,
                    Environnement3::new,
                    Environnement4::new
                };

        Agent.PRODUCE_TRACE = false;
        String[] environmentNames = new String[] { "Env1", "Env2", "Env3", "Env4" };

        String[][] results = new String[valueSystems.length][environmentCreators.length];

        for (int iValueSystem = 0 ; iValueSystem != valueSystems.length ; ++iValueSystem) {
            for (int iEnvironment = 0 ; iEnvironment != environmentCreators.length ; iEnvironment++) {
                Agent agent = agentCreator.apply(valueSystems[iValueSystem]);
                Environnement environment = environmentCreators[iEnvironment].get();

                int positivFeedbackFirst10 = 0;
                int positivFeedbackFollowing10 = 0;
                int positivFeedbackFrom100To199 = 0;

                for (int i = 0 ; i != 10 ; i++) {
                    if (produceAStep(agent, environment)) {
                        positivFeedbackFirst10++;;
                    }
                }

                for (int i = 10 ; i != 20 ; i++) {
                    if (produceAStep(agent, environment)) {
                        positivFeedbackFollowing10++;;
                    }
                }

                for (int i = 20 ; i != 100 ; i++) {
                    produceAStep(agent, environment);
                }

                for (int i = 100 ; i != 200 ; i++) {
                    if (produceAStep(agent, environment)) {
                        positivFeedbackFrom100To199++;;
                    }
                }

                results[iValueSystem][iEnvironment] = "" + positivFeedbackFirst10 + " - " + positivFeedbackFollowing10 + " - " + positivFeedbackFrom100To199;
            }
        }

        // = Produce markdown table
        // Agent names
        StringJoiner sj = new StringJoiner(" | ", "| ", " |");
        sj.add("");
        for (String valueSystem : valueSystems) {
            sj.add(valueSystem);
        }
        System.out.println(sj.toString());

        sj = new StringJoiner(" | ", "| ", " |");
        for (int i = -1; i < valueSystems.length; i++) {
            sj.add("---");
        }
        System.out.println(sj.toString());

        // For each Environemnts
        for (int i = 0; i < environmentNames.length; i++) {
            sj = new StringJoiner(" | ", "| ", " |");

            sj.add("*"+environmentNames[i]+"*");

            for (int j = 0; j < valueSystems.length; j++) {
                sj.add(results[j][i]);
            }

            System.out.println(sj.toString());
        }
    }

    private static boolean produceAStep(Agent agent, Environnement environnement) {
        int action = agent.getAction();
        int feedback = environnement.agir(action);
        return agent.receiveFeedback(feedback);
    }

    private static void tracedExecution(Agent agent, Environnement environnement)  throws InterruptedException {
        while (true) {
            produceAStep(agent, environnement);
            Thread.sleep(500);
        }
    }
}
