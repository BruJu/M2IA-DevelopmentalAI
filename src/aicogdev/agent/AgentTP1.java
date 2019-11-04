package aicogdev.agent;

import aicogdev.interaction.Decision;
import aicogdev.interaction.ResultatInteraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP1 extends Agent {
	private Map<Integer, Integer> expectations = new HashMap<>();

	private int actionWanted = 1;

	private int numberOfTimesRight = 0;

	@Override
	protected Decision getDecision() {
		return new Decision(actionWanted, expectations.getOrDefault(actionWanted, 0));
	}

	@Override
	protected ResultatInteraction processReaction(int actionFaite, int reactionAttendue, int reactionRecue) {
		if (Objects.equals(reactionAttendue, reactionRecue)) {
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

			return new ResultatInteraction(true, 0, isBored);
		} else {
			expectations.put(actionFaite, reactionRecue);
			numberOfTimesRight = 0;

			return new ResultatInteraction(false, 0, false);
		}
	}
}
