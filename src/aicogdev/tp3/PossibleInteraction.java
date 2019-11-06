package aicogdev.tp3;

import aicogdev.agent.Feedback;
import aicogdev.interaction.Interaction;
import fr.bruju.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class PossibleInteraction {
    public PossibleInteraction(int action, Feedback feedback) {
        this.action = action;
        possibleReactions.put(1, new ReactionValeur(feedback.getValue(action, 1)));
        possibleReactions.put(2, new ReactionValeur(feedback.getValue(action, 2)));
    }

	public String stringifyValences(int reactionAttendue, int reactionRecue) {
    	String choixUn = possibleReactions.get(1).toString(1);
    	String choixDeux = possibleReactions.get(2).toString(2);

		if (reactionRecue == 2) {
			choixDeux = "==>" + choixDeux;
			choixUn =   "   " + choixUn;
		} else {
			choixUn =   "==>" + choixUn;
			choixDeux = "   " + choixDeux;
		}

		if (reactionAttendue == 2) {
			choixDeux = choixDeux + "==>";
			choixUn = choixUn     + "   ";
		} else {
			choixUn = choixUn     + "==>";
			choixDeux = choixDeux + "   ";
		}

		return choixUn + " " + choixDeux;
	}

	public static class ReactionValeur {
        public final int valeur;
        public int poids;

        public ReactionValeur(int valeur) {
            this.valeur = valeur;
            this.poids = 0;
        }

        public int getValance() {
            return valeur * poids;
        }


        public String toString(int prefixe) {
        	return "" + prefixe + "->" + valeur + "*" + poids + "=" + (valeur * poids);
		}
    }

    public final int action;
    private final Map<Integer, ReactionValeur> possibleReactions = new HashMap<>();

    public void incrementerPoids(int reaction) {
        possibleReactions.get(reaction).poids++;
    }

    public Pair<Interaction, Integer> exploiterInteraction() {
        Interaction interactionLaPlusProbable = null;
        int occurencesDeCetteInteraction = 0;

        int sommeDesValeurs = 0;

        for (Map.Entry<Integer, ReactionValeur> integerReactionValeurEntry : possibleReactions.entrySet()) {
            int occurences = integerReactionValeurEntry.getValue().poids;

            if (interactionLaPlusProbable == null || occurences > occurencesDeCetteInteraction) {
                interactionLaPlusProbable = new Interaction(action, integerReactionValeurEntry.getKey());
                occurencesDeCetteInteraction = occurences;
            }

            sommeDesValeurs += occurences * integerReactionValeurEntry.getValue().valeur;
        }

        return new Pair<>(interactionLaPlusProbable, sommeDesValeurs);
    }
}
