package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import comun.Moneda;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class ClienteGUI {

	private Cliente cliente;

	// Componentes GUI
	private JFrame frmCoinlauncher;
	private JTextField textField_port;
	private JTextField textField_ip;
	private JButton btn_conectar;
	private JButton btn_desconectar;
	private JButton btn_tirarMoneda;
	private JButton btn_estado;
	private JLabel img_moneda;
	private JLabel lblHashServidor;
	private JLabel lblHashCliente;
	private JLabel lblHashIsOk;

	// Conexión
	private boolean conectado = false;
	private String ip;
	private int puerto;

	// Lanzamiento moneda
	private String resultados[]; // valor#hash_servidor#hash_cliente
	// Guardamos el array resultados en variables separadas:
	private int lanzamiento; // valor entre 0 y 100.000
	private String hashServidor;
	private String hashCliente;
	private String moneda; // cara o cruz

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteGUI window = new ClienteGUI();
					window.frmCoinlauncher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClienteGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cliente = new Cliente();

		// FRAME DEL PROGRAMA PRINCIPAL
		frmCoinlauncher = new JFrame();
		frmCoinlauncher.setTitle("Cliente coinLauncher ");
		frmCoinlauncher
				.setIconImage(Toolkit.getDefaultToolkit().getImage(ClienteGUI.class.getResource("/img/coin.png")));
		frmCoinlauncher.setBounds(100, 100, 357, 464);
		frmCoinlauncher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCoinlauncher.getContentPane().setLayout(null);

		// Boton "Desconectar" inicialmente deshabilitado
		btn_desconectar = new JButton("Desconectar");
		btn_desconectar.setEnabled(false);

		// Boton "Tirar la moneda" inicialmente deshabilitado
		btn_tirarMoneda = new JButton("Tirar la moneda");
		btn_tirarMoneda.setEnabled(false);

		// IMAGEN MONEDA SIN LANZAR
		img_moneda = new JLabel("");
		img_moneda.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/sin_lanzar.png")));
		img_moneda.setEnabled(true);
		img_moneda.setBounds(110, 205, 134, 162);
		frmCoinlauncher.getContentPane().add(img_moneda);

		// PUERTO
		textField_port = new JTextField();
		textField_port.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_port.setHorizontalAlignment(SwingConstants.CENTER);
		textField_port.setBounds(226, 42, 99, 33);
		frmCoinlauncher.getContentPane().add(textField_port);
		textField_port.setColumns(10);

		JLabel lbl_puerto = new JLabel("Puerto");
		lbl_puerto.setFont(new Font("Arial", Font.BOLD, 24));
		lbl_puerto.setBounds(191, 13, 99, 29);
		frmCoinlauncher.getContentPane().add(lbl_puerto);

		// IP
		textField_ip = new JTextField();
		textField_ip.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_ip.setHorizontalAlignment(SwingConstants.CENTER);
		textField_ip.setBounds(12, 42, 202, 33);
		frmCoinlauncher.getContentPane().add(textField_ip);
		textField_ip.setColumns(10);

		JLabel lbl_ip = new JLabel("IP Servidor");
		lbl_ip.setFont(new Font("Arial", Font.BOLD, 24));
		lbl_ip.setBounds(12, 13, 167, 22);
		frmCoinlauncher.getContentPane().add(lbl_ip);

		// ETIQUETAS PARA MOSTRAR EL HASH
		lblHashServidor = new JLabel(" ");
		lblHashServidor.setHorizontalAlignment(SwingConstants.LEFT);
		lblHashServidor.setBounds(12, 359, 327, 16);
		frmCoinlauncher.getContentPane().add(lblHashServidor);

		lblHashCliente = new JLabel(" ");
		lblHashCliente.setHorizontalAlignment(SwingConstants.LEFT);
		lblHashCliente.setBounds(12, 374, 327, 16);
		frmCoinlauncher.getContentPane().add(lblHashCliente);

		lblHashIsOk = new JLabel(" ");
		lblHashIsOk.setHorizontalAlignment(SwingConstants.LEFT);
		lblHashIsOk.setBounds(12, 388, 284, 16);
		frmCoinlauncher.getContentPane().add(lblHashIsOk);
		// --------------------------------------

		// ESTADO DE LA CONEXION
		btn_estado = new JButton("");
		btn_estado.setEnabled(false);
		btn_estado.setBackground(Color.RED);
		btn_estado.setBounds(191, 137, 47, 16);
		frmCoinlauncher.getContentPane().add(btn_estado);

		JLabel lbl_EstadoConexin = new JLabel("Estado conexión");
		lbl_EstadoConexin.setFont(new Font("Arial", Font.BOLD, 16));
		lbl_EstadoConexin.setBounds(12, 137, 152, 16);
		frmCoinlauncher.getContentPane().add(lbl_EstadoConexin);

		// CONECTAR
		btn_conectar = new JButton("Conectar");
		btn_conectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// Coger IP y puerto
					ip = textField_ip.getText();

					if (ip == "" || ip == null) {
						throw new NumberFormatException();
					}

					puerto = Integer.parseInt(textField_port.getText());

					if (puerto < 1024 || puerto > 49151) {
						throw new NumberFormatException();
					}

					conectado = cliente.conectar(ip, puerto);

					if (conectado) {
						textField_ip.setEnabled(false);
						textField_port.setEnabled(false);
						btn_conectar.setEnabled(false);
						btn_estado.setBackground(Color.GREEN);
						btn_desconectar.setEnabled(true);
						btn_tirarMoneda.setEnabled(true);
					
					} else {
						btn_estado.setBackground(Color.RED);
						JOptionPane.showMessageDialog(null, "Conexión fallida", "Conexion con el servidor", JOptionPane.ERROR_MESSAGE);
					}

				} catch (Exception e2) {
					textField_port.setText("");
					JOptionPane.showMessageDialog(null, "La ip o el puerto son incorrectos", "Configuracion con el servidor incorrecta", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btn_conectar.setFont(new Font("Arial", Font.BOLD, 16));
		btn_conectar.setBounds(12, 88, 134, 25);
		frmCoinlauncher.getContentPane().add(btn_conectar);

		// DESCONECTAR
		btn_desconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectar();
			}
		});
		btn_desconectar.setFont(new Font("Arial", Font.BOLD, 16));
		btn_desconectar.setBounds(191, 88, 134, 25);
		frmCoinlauncher.getContentPane().add(btn_desconectar);

		// TIRAR LA MONEDA
		btn_tirarMoneda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// numero#hash_servidor#hash_cliente
					resultados = cliente.lanzarMoneda().split("#");

					if (resultados.length == 3) {
						// numero
						lanzamiento = Integer.parseInt(resultados[0]);

						// hashes
						hashServidor = resultados[1];
						hashCliente = resultados[2];

						//mostrar los hashes
						lblHashServidor.setText("Hash servidor: " + hashServidor);
						lblHashCliente.setText("Hash cliente: " + hashServidor);

						if (hashServidor.equals(hashCliente)) {
							lblHashIsOk.setText("Hash correcto");
						} else {
							lblHashIsOk.setText("Hash incorrecto!");
						}

						// cara o cruz
						moneda = Moneda.extraerResultado(lanzamiento);

						if (moneda.equals("cara")) {
							img_moneda.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/cara.png")));
						} else if (moneda.equals("cruz")) {
							img_moneda.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/cruz.png")));
						} else {
							JOptionPane.showMessageDialog(null, "Error al lanzar la moneda", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Error al lanzar la moneda, conexión con el servidor finalizada", "Error",
								JOptionPane.ERROR_MESSAGE);
						desconectar();
					}

				} catch (Exception e2) {
				}

			}
		});
		btn_tirarMoneda.setFont(new Font("Arial", Font.PLAIN, 36));
		btn_tirarMoneda.setBounds(12, 179, 313, 39);
		frmCoinlauncher.getContentPane().add(btn_tirarMoneda);
	}

	public void desconectar() {
		if (cliente.desconectar()) {
			conectado = false;
			textField_ip.setEnabled(true);
			textField_port.setEnabled(true);
			btn_conectar.setEnabled(true);
			btn_desconectar.setEnabled(false);
			btn_tirarMoneda.setEnabled(false);
			btn_estado.setBackground(Color.RED);
			img_moneda.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/sin_lanzar.png")));

			lblHashServidor.setText("");
			lblHashCliente.setText("");
			lblHashIsOk.setText("");
		} else {
			JOptionPane.showMessageDialog(null, "Error al desconectar, cierra el cliente des de la X",
					"Configuracion del servidor", JOptionPane.ERROR_MESSAGE);
		}
	}

}
