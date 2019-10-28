package aicogdev.agent;

import aicogdev.interaction.Action;
import aicogdev.interaction.Decision;
import aicogdev.interaction.Reaction;
import aicogdev.interaction.ResultatInteraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP1 extends Agent {
	private Map<Action, Reaction> expectations = new HashMap<>();

	private Action actionWanted = new Action(1);

	private int numberOfTimesRight = 0;

	@Override
	protected Decision getDecision() {
		return new Decision(actionWanted, expectations.get(actionWanted));
	}

	@Override
	protected ResultatInteraction processReaction(Action actionFaite, Reaction reactionAttendue, Reaction reactionRecue) {
		if (Objects.equals(reactionAttendue, reactionRecue)) {
			numberOfTimesRight++;

			boolean isBored = false;

			if (numberOfTimesRight == 3) {
				isBored = true;

				if (actionWanted.numero == 1)
					actionWanted = new Action(2);
				else
					actionWanted = new Action(1);

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
