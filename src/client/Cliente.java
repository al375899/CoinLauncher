package client;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class Cliente {

	AuxiliarCliente aux;

	public Cliente() {
		super();
	}

	public boolean conectar(String ip, int puerto) {
		try {
			aux = new AuxiliarCliente(ip, puerto);
			
			if (aux.conectar()) {
				System.out.println("Un cliente se ha conectado a " + ip + ":" + puerto);
				return true;
			} else {
				System.out.println("El cliente no ha podido conectarse a " + ip + ":" + puerto);
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public boolean desconectar() {
		try {
			if (aux.salir()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error al desconectar");
			return false;
		}
	}

	public String lanzarMoneda() {
		try {
			System.out.println("El cliente lanza una moneda");

			return aux.lanzarMoneda();
			
		} catch (Exception e) {
			System.out.println("Error al lanzar la moneda des del cliente");
			return "";
		}

	}

}
