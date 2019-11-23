package aicogdev.tp3;

import aicogdev.agent.ValuationSystem;
import aicogdev.interaction.Interaction;
import fr.bruju.util.Pair;

import java.util.*;

public class PossibleInteractions {
    private Map<Interaction, Map<Integer, PossibleInteraction>> interactions = new HashMap<>();

    private ValuationSystem valuationSystem;

    public PossibleInteractions(ValuationSystem valuationSystem) {
        this.valuationSystem = valuationSystem;
    }

    public void registerSequence(Interaction premiere, Interaction seconde) {
        if (!interactions.containsKey(premiere)) {
            interactions.put(premiere, createEmptyPossibles());
        }

        Map<Integer, PossibleInteraction> reactionsAnticipees = interactions.get(premiere);
        reactionsAnticipees.get(seconde.action).incrementerPoids(seconde.reaction);
    }

    public Interaction deciderAction(Interaction precedente) {
        if (!interactions.containsKey(precedente)) {
            return new Interaction(1, 0);
        }

        Map<Integer, PossibleInteraction> reactionsAnticipees = interactions.get(precedente);


        Interaction aFaire = null;
        int esperance = 0;

        for (Map.Entry<Integer, PossibleInteraction> integerPossibleInteractionEntry : reactionsAnticipees.entrySet()) {
            Pair<Interaction, Integer> possibilite = integerPossibleInteractionEntry.getValue().exploiterInteraction();

            if (aFaire == null || esperance > possibilite.getRight()) {
                aFaire = possibilite.getLeft();
                esperance = possibilite.getRight();
            }
        }

        return aFaire;
    }

    private Map<Integer, PossibleInteraction> createEmptyPossibles() {
        Map<Integer, PossibleInteraction> list = new HashMap<>();

        for (int action = 1 ; action <= 2 ; action++) {
            list.put(action, new PossibleInteraction(action, valuationSystem));
        }

        return list;
    }

	public String stringifyValences(Interaction premiere, int actionFaite, int reactionAttendue, int reactionRecue) {
		if (!interactions.containsKey(premiere)) {
			return "N/A";
		}

		PossibleInteraction ps = interactions.get(premiere).get(actionFaite);

		return ps.stringifyValences(reactionAttendue, reactionRecue);
	}
}
