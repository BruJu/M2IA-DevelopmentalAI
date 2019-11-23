package aicogdev.tp3;

import aicogdev.agent.Agent;
import aicogdev.agent.Feedback;
import aicogdev.interaction.Interaction;
import fr.bruju.util.Pair;

import javax.sound.midi.Sequence;
import java.util.*;

public class AgentTP3 extends Agent {
    private Feedback feedback = new Feedback(new int[] { -1, 1, -1, 1 });

	/**
	 * Associate a previous interaction and a following action with the expected reaction
	 * ie
	 * <code>Map<Interaction(A, B), C> = D</code>
	 * I did A, i got feedback B. If i do C, i'll get D
	 */
	private Map<Pair<Interaction, Integer>, Integer> learnedInteractions = new HashMap<>();

    private Interaction derniereInteraction = null;



    @Override
    protected Interaction getDecision() {
        if (derniereInteraction == null) {
            return new Interaction(1, 0);
        } else {
            Interaction i = trouverInteractionSuivante();
            return new Interaction(i.action, i.reaction);
        }
    }

    private Interaction trouverInteractionSuivante() {
        List<Pair<Interaction, Integer>> avoid = new ArrayList<>();
		List<Pair<Interaction, Integer>> exploit = new ArrayList<>();

        // Find the interactions that we think are possible at this point
        learnedInteractions
				.entrySet()
				.stream()
				// Keep relevant first interaction
				.filter(entry -> entry.getKey().getLeft().equals(derniereInteraction))
				// Keep the following action - reactions
				.map(entry -> new Interaction(entry.getKey().getRight(), entry.getValue()))
				// Add the relevant interactions
				.forEach(expectedInteraction -> {
					int expectedFeedback = feedback.getValue(expectedInteraction);

					if (expectedFeedback < 0) {
						avoid.add(new Pair<>(expectedInteraction, expectedFeedback));
					} else {
						exploit.add(new Pair<>(expectedInteraction, expectedFeedback));
					}
				});


        if (exploit.isEmpty()) {
            Set<Integer> choices = new TreeSet<>();
            choices.add(1);
            choices.add(2);

            avoid.forEach(p -> choices.remove(p.getLeft().action));

            for (Integer i : choices) {
                return new Interaction(i, 0);
            }

            return new Interaction(1, 0);
        } else {
        	return exploit.stream().max((i1, i2) -> -Integer.compare(i1.getRight(), i2.getRight())).get().getLeft();
        }
    }

    @Override
    protected String processReaction(int action, int expectedFeedback, int actualFeedback) {
        Interaction obtenue = new Interaction(action, actualFeedback);

        int feedback = this.feedback.getValue(action, actualFeedback);


		String patternAppris = "N/A";


        if (derniereInteraction != null) {
			Pair<Interaction, Integer> beginSequence = new Pair<>(derniereInteraction, action);

			boolean learnedSomething = true;

			if (learnedInteractions.containsKey(beginSequence)) {
				if (!learnedInteractions.get(beginSequence).equals(expectedFeedback)) {
					System.out.println("La séquence enregistrée était fausse");
				} else {
					learnedSomething = false;
				}
			}

			if (learnedSomething) {
				learnedInteractions.put(beginSequence, actualFeedback);
				patternAppris = "[" + beginSequence.getLeft()
						+ ", " + new Interaction(beginSequence.getRight(), actualFeedback) + "]";
			} else {
				patternAppris = "";
			}
        }

		derniereInteraction = obtenue;

        return (expectedFeedback == actualFeedback ? "Content" : "Surpris")
				+ " ; " + feedback
				+ " ; " + patternAppris;
    }
}
