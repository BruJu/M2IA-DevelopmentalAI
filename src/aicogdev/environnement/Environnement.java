package aicogdev.environnement;

/**
 * Un environnement est quelque chose sur lequel on peut agir et qui produit une réaction. Conventionnellement, les
 * réactions sont égales soit à 1 soit à 2 et les actions commencent à partir de 1.
 */
public interface Environnement {
	/**
	 * Agit sur l'environnement
	 * @param action Numéro de l'action (supérieur à 0, les actions possibles sont contigues)
	 * @return Numéro de la réaction (1 ou 2)
	 */
	int agir(int action);
}
