package aicogdev.agent;

import aicogdev.interaction.Interaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP1 extends Agent {
	private Map<Integer, Integer> expectations = new HashMap<>();

	private int actionWanted = 1;

	private int numberOfTimesRight = 0;

	@Override
	protected Interaction getDecision() {
		return new Interaction(actionWanted, expectations.getOrDefault(actionWanted, 0));
	}

	@Override
	protected String processReaction(int action, int expectedFeedback, int actualFeedback) {
		if (Objects.equals(expectedFeedback, actualFeedback)) {
			numberOfTimesRight++;

			boolean isBored = false;

			if (numberOfTimesRight == 3) {
				isBored = true;

				if (actionWanted == 1)
					actionWanted = 2;
				else
					actionWanted = 1;

				numberOfTimesRight = 0;
			}

			return isBored ? "Ennuyé" : "Content";
		} else {
			expectations.put(action, actualFeedback);
			numberOfTimesRight = 0;

			return "Surpris";
		}
	}
}
