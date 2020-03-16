package server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.awt.event.ActionEvent;


public class ServidorGUI {

	private JFrame frame;
	private JTextField textField_IP;
	private JButton btn_stop;
	private JLabel lbl_status;
	private JButton btn_status;
	private JTextField textField_puerto;
	private String ip;
	private Servidor servidor;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		//System.setProperty("javax.net.debug", "ssl,handshake");
		System.setProperty("javax.net.ssl.keyStore", "src/certs/server/serverKey.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "patata");
		System.setProperty("javax.net.ssl.trustStore", "src/certs/server/serverTrustedCerts.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "patata");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServidorGUI window = new ServidorGUI();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServidorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ServidorGUI.class.getResource("/img/coin.png")));
		frame.getContentPane().setBackground(SystemColor.window);
		frame.setTitle("Servidor CoinLauncher");
		frame.setBounds(100, 100, 354, 242);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// INICIAR SERVIDOR
		JButton btn_iniciar = new JButton("Iniciar servidor");
		btn_iniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int puerto = 1234; // Puerto por defecto

				try {
					// Leemos el puerto que ha introducido el usuario
					puerto = Integer.parseInt(textField_puerto.getText());

					// Si el puerto no es correcto tiramos excepcion
					if (puerto < 1024 || puerto > 49151) {
						throw new NumberFormatException();
					}

					// Intanciamos un servidor
					servidor = new Servidor(puerto);

					// Arrancamos el servidor en una hebra aux para que el GUI pueda seguir
					// funcionando
					Thread aux = new Thread(new Runnable() {
						@Override
						public void run() {
							servidor.iniciar();
						}
					});
					aux.start();

					// Estado servidor: VERDE
					btn_status.setBackground(Color.GREEN);
					
					// Activamos/desactivamos varios botones
					btn_iniciar.setEnabled(false);
					btn_stop.setEnabled(true);
					textField_puerto.setEditable(false);
					

				} catch (NumberFormatException e2) {
					textField_puerto.setText("");
					JOptionPane.showMessageDialog(null, "Debes introducir un numero de puerto entre 1024 y 49151.",
							"Numero de puerto incorrecto", JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		btn_iniciar.setFont(new Font("Arial", Font.BOLD, 14));
		btn_iniciar.setBackground(Color.ORANGE);
		btn_iniciar.setBounds(22, 124, 145, 51);
		frame.getContentPane().add(btn_iniciar);

		// IP DEL SERVIDOR (coger la ip del equipo)
		JLabel lbl_IP = new JLabel("IP:");
		lbl_IP.setFont(new Font("Arial", Font.BOLD, 16));
		lbl_IP.setBounds(41, 16, 41, 16);
		frame.getContentPane().add(lbl_IP);

		textField_IP = new JTextField();
		textField_IP.setHorizontalAlignment(SwingConstants.CENTER);
		textField_IP.setEditable(false);
		textField_IP.setBounds(118, 16, 145, 25);
		frame.getContentPane().add(textField_IP);
		textField_IP.setColumns(10);

		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
			textField_IP.setText(ip);
		} catch (Exception e3) {

		}

		// PUERTO
		JLabel lbl_puerto = new JLabel("Puerto:");
		lbl_puerto.setFont(new Font("Arial", Font.BOLD, 16));
		lbl_puerto.setBounds(41, 40, 65, 32);
		frame.getContentPane().add(lbl_puerto);

		// PARAR SERVIDOR
		btn_stop = new JButton("Parar servidor");
		btn_stop.setEnabled(false);
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					servidor.parar();
				} catch (Exception e2) {
				}

				// habilitar/deshabilitar botones
				btn_status.setBackground(Color.RED);
				btn_iniciar.setEnabled(true);
				btn_stop.setEnabled(false);
				textField_puerto.setEditable(true);

			}
		});
		btn_stop.setFont(new Font("Arial", Font.BOLD, 14));
		btn_stop.setBackground(Color.ORANGE);
		btn_stop.setBounds(179, 124, 141, 51);
		frame.getContentPane().add(btn_stop);

		lbl_status = new JLabel("Estado:");
		lbl_status.setFont(new Font("Arial", Font.BOLD, 16));
		lbl_status.setBounds(41, 76, 65, 25);
		frame.getContentPane().add(lbl_status);

		btn_status = new JButton("");
		btn_status.setBackground(Color.RED);
		btn_status.setEnabled(false);
		btn_status.setBounds(118, 85, 41, 16);
		frame.getContentPane().add(btn_status);

		textField_puerto = new JTextField();
		textField_puerto.setHorizontalAlignment(SwingConstants.CENTER);
		textField_puerto.setColumns(10);
		textField_puerto.setBounds(118, 48, 145, 25);
		frame.getContentPane().add(textField_puerto);

	}
}
