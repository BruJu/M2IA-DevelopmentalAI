package fr.bruju.util;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MapsUtils {


	/**
	 * Renvoi la valeur stockée à la clé donnée dans la map. Si aucune valeur n'est dans la map, utilise le
	 * supplier pour mettre une valeur initiale, puis renvoie cette dernière.
	 * @param map La map
	 * @param key La clé
	 * @param supplier Un fournisseur de valeur de base
	 * @return La valeur présente dans la map, ou à défaut la valeur fournie par supplier.
	 */
	public static <K, V> V getX(Map<K, V> map, K key, Supplier<? extends V> supplier) {
		V value = map.get(key);

		if (value == null) {
			value = supplier.get();
			map.put(key, value);
		}

		return value;
	}

	public static <K, V> V getY(Map<K, V> map, K key, Function<K, V> initialisateur) {
		V value = map.get(key);

		if (value == null) {
			value = initialisateur.apply(key);
			map.put(key, value);
		}

		return value;
	}

	/**
	 * Ajoute dans la map receveur toutes les clés de donneur
	 * @param receveur La map destination
	 * @param donneur La map source
	 * @param fonctionDAjout if (valeurPresente == null) receveur.put(cle, fonctionDAjout.apply(valeur));
	 * @param fonctionSiPresent if (valeurPresente != null)
	 * receveur.put(cle, fonctionSiPresent.apply(valeurPresente, valeur));
	 */
	public static <K, V> void fusionnerDans(Map<K, V> receveur,
											Map<K, V> donneur, UnaryOperator<V> fonctionDAjout, BinaryOperator<V> fonctionSiPresent) {
		donneur.forEach((cle, valeur) -> {
			V valeurPresente = receveur.get(cle);

			if (valeurPresente == null) {
				receveur.put(cle, fonctionDAjout.apply(valeur));
			} else {
				receveur.put(cle, fonctionSiPresent.apply(valeurPresente, valeur));
			}
		});
	}

	/**
	 * Ajoute un élément à la liste qui est associé à la clé donnée. Si la clé n'est pas initialisée, met une
	 * ArrayList pour cette clé.
	 * @param map La map
	 * @param cle La clé
	 * @param element L'élément à ajouter à la liste
	 */
	public static <K, V> void ajouterElementDansListe(Map<K, List<V>> map, K cle, V element) {
		List<V> liste = map.get(cle);
		if (liste == null) {
			liste = new ArrayList<>();
			map.put(cle, liste);
		}

		liste.add(element);
	}

	/**
	 * Ajoute un élément à l'ensemble qui est associé à la clé donnée. Si la clé n'est pas initialisée, met un
	 * TreeSet pour cette clé.
	 * @param map La map
	 * @param cle La clé
	 * @param element L'élément à ajouter à l'ensemble
	 */
	public static <K, V> void ajouterElementDansSet(Map<K, Set<V>> map, K cle, V element) {
		Set<V> liste = map.get(cle);
		if (liste == null) {
			liste = new TreeSet<>();
			map.put(cle, liste);
		}

		liste.add(element);
	}

	public static <K, V> Map<K, V> combiner(Map<K, V> destination, Map<K, V> source1, Map<K, V> source2,
								   BinaryOperator<V> fonctionDeCombinaison) {
		Stream.of(source1.keySet(), source2.keySet())
			  .flatMap(Set::stream)
			  .distinct()
			  .forEach(cle -> {
				  V valeur1 = source1.get(cle);
				  V valeur2 = source2.get(cle);
				  if (valeur1 != null || valeur2 != null) {
					  destination.put(cle, fonctionDeCombinaison.apply(valeur1, valeur2));
				  }
			  });

		return destination;
	}

	public static <K, V> void combinerNonNull(Map<K, V> destination, Map<K, V> source1, Map<K, V> source2,
										  BinaryOperator<V> fonctionDeCombinaison) {
		Stream.of(source1.keySet(), source2.keySet())
				.flatMap(Set::stream)
				.distinct()
				.forEach(cle -> {
					V valeur1 = source1.get(cle);
					V valeur2 = source2.get(cle);
					destination.put(cle, fonctionDeCombinaison.apply(valeur1, valeur2));
				});
	}
}
