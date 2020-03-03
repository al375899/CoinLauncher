package comun;

import java.util.Random;

public class Moneda {

	public static final int MINIMO = 0;
	public static final int MAXIMO = 100000;

	public Moneda() {
	}

	public static synchronized int lanzar() {

		// Calcular un valor aleatorio entre minimo y maximo
		Random r = new Random();
		int lanzamiento = r.nextInt(MAXIMO - MINIMO) + MINIMO;

		return lanzamiento;
	}

	public static synchronized String extraerResultado(int lanzamiento) {
		if (lanzamiento >= MAXIMO / 2) {
			return "cara";
		} else {
			return "cruz";
		}
	}
}
