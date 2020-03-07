package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import comun.HashGenerator;
import comun.MyStreamSocket;

public class AuxiliarCliente {

	private MyStreamSocket socket;
	private InetAddress serverHost;
	private int serverPort;
	private String hostname;

	public AuxiliarCliente(String hostname, int serverPort) {
		this.hostname = hostname;
		this.serverPort = serverPort;
	}

	public boolean conectar() throws SocketException, IOException {
		try {
			serverHost = InetAddress.getByName(hostname);
			socket = new MyStreamSocket(serverHost, serverPort);
			
			//comprobar que el socket se ha conectado al servidor, si no, salta excepcion
			socket.sendMessage("99");
			
			return true;
		} catch (Exception e) {
			System.out.println("La conexion con el serverSocket ha fallado des del cliente");
			return false;
		}
	}

	public boolean salir() {
		try {
			socket.sendMessage("0");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String lanzarMoneda() {
		
		String resultado = "";
		try {
			socket.sendMessage("1");
			String lanzamiento = resultado = socket.receiveMessage();
			String hash_servidor = socket.receiveMessage();
			String hash_cliente = HashGenerator.generarHash(lanzamiento);

			resultado = lanzamiento + "#" + hash_servidor + "#" + hash_cliente;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return resultado;
	}

}
