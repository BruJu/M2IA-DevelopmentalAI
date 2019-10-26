package aicogdev;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentUnique extends Agent {
	private Map<Action, Reaction> expectations = new HashMap<>();

	private Action actionWanted = new Action(1);

	private int numberOfTimesRight = 0;

	@Override
	protected Decision getDecision() {
		return new Decision(actionWanted, expectations.get(actionWanted));
	}

	@Override
	protected Status processReaction(Decision decision, Reaction reaction, Status status) {
		if (Objects.equals(decision.reactionAttendue, reaction)) {
			numberOfTimesRight++;

			if (numberOfTimesRight == 3) {
				if (actionWanted.numero == 1)
					actionWanted = new Action(2);
				else
					actionWanted = new Action(1);

				numberOfTimesRight = 0;
				return Status.bored;
			}

		} else {
			expectations.put(decision.actionChoisie, reaction);
			numberOfTimesRight = 0;
		}

		return status;
	}
}
