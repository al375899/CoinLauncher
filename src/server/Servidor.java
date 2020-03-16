package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.LinkedList;

import javax.net.ssl.SSLServerSocketFactory;

import comun.MyStreamSocket;

public class Servidor {

	// Variables del servidor
	int puertoServidor;
	ServerSocket serverSocket;
	MyStreamSocket miSocketDatos;
	LinkedList<MyStreamSocket> listaClienteSockets = new LinkedList<>(); // listado de clientes

	// SSL
	SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

	public Servidor() {
		super();
	}

	public Servidor(int puertoServidor) {
		super();
		this.puertoServidor = puertoServidor;
	}

	public void iniciar() {
		// Acepta conexiones via socket de distintos clientes.
		// Por cada conexion establecida lanza una hebra

		try {
			// Instanciar un socket stream para aceptar las conexiones

			// serverSocket = new ServerSocket(puertoServidor); // SIN SSL
			serverSocket = serverFactory.createServerSocket(puertoServidor); // SSL

			String ip = InetAddress.getLocalHost().getHostAddress().toString();
			System.out.println("Servidor listo en " + ip + ":" + puertoServidor);

			while (true) { // Bucle infinito

				// Esperar para aceptar una conexion
				System.out.println("Esperando una conexion.");

				miSocketDatos = new MyStreamSocket(serverSocket.accept());
				System.out.println("Conexion aceptada");

				// Guardamos el cliente en la lista para luego poder cerrarlos todos:
				listaClienteSockets.add(miSocketDatos);

				// Arrancar un hilo para manejar la sesion de cliente
				Thread elHilo = new Thread(new HiloServidor(miSocketDatos));
				elHilo.start();

				// Y continua con el siguiente cliente
			}

		} catch (Exception ex) {
		}

	}

	public void parar() {

		try {
			// Cerramos el socket servidor
			serverSocket.close();

			// Cerramos las conexiones con todos los clientes
			for (MyStreamSocket conexionCliente : listaClienteSockets) {
				conexionCliente.close();
			}

			// El servidor y todas las conexiones ya han finalizado
			System.out.println("El servidor ha sido parado");

		} catch (Exception e) {
		}
	}

}
