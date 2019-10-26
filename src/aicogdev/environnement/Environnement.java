package aicogdev.environnement;

import aicogdev.Action;
import aicogdev.Reaction;

public interface Environnement {
	Reaction agir(Action action);
}
