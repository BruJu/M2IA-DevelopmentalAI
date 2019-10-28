package aicogdev.interaction;

import java.util.Objects;

public class Reaction {
	public final int numero;

	public Reaction(int numero) {
		this.numero = numero;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reaction reaction = (Reaction) o;
		return numero == reaction.numero;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero);
	}

	@Override
	public String toString() {
		return Integer.toString(numero);
	}
}
