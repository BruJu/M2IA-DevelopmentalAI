package aicogdev;

import java.util.Objects;

public class Action {
	public final int numero;

	public Action(int numero) {
		this.numero = numero;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Action action = (Action) o;
		return numero == action.numero;
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
