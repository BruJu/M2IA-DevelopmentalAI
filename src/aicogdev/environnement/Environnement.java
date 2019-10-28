package aicogdev.environnement;

import aicogdev.interaction.Action;
import aicogdev.interaction.Reaction;

public interface Environnement {
	Reaction agir(Action action);
}
