package server;

import comun.HashGenerator;
import comun.Moneda;
import comun.MyStreamSocket;

public class HiloServidor implements Runnable {
	MyStreamSocket socket;

	public HiloServidor(MyStreamSocket socket) {
		this.socket = socket;
	}

	public void run() {
		boolean finalizar = false;
		String op;
		try {
			while (!finalizar) {
				op = socket.receiveMessage();
				int operacion = Integer.parseInt(op);

				switch (operacion) {
				case 0:
					try {
						socket.close();
					} catch (Exception e) {

					}
					break;

				case 1:
					Moneda moneda = new Moneda();
					String resultado = moneda.lanzar() + ""; // numero entre 0 y 100.000
					String hash = HashGenerator.generarHash(resultado); // hash del numero que ha salido
					socket.sendMessage(resultado); // mandamos el numero al cliente
					socket.sendMessage(hash); // luego mandamos el hash para verificar que no hay trampa
					break;

				}
			}
		} catch (Exception e) {

		}
	}

}
