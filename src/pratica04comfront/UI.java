package pratica04comfront;

import java.awt.event.WindowAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

public class UI extends JFrame implements Runnable {
	private static final long serialVersionUID = 1l;

	private JButton btnEnviar;
	private JTextField entrada;
	private JScrollPane jScrollPane1;
	private JTextArea saida;

	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;
	private Thread listener;
	
	private JLabel lblName;
	
	private boolean first = true;

	public UI(Socket socket) throws IOException {
		initComponents();

		this.socket = socket;
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.listener = new Thread(this);
		this.listener.start();
		this.setVisible(true);
	}

	private void initComponents() {

		btnEnviar = new JButton();
		entrada = new JTextField();
		jScrollPane1 = new JScrollPane();
		saida = new JTextArea();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat Socket");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		btnEnviar.setText("Enviar");
		btnEnviar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnEnviarActionPerformed(evt);
			}
		});

		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setAutoscrolls(true);

		saida.setColumns(20);
		saida.setLineWrap(true);
		saida.setRows(5);
		jScrollPane1.setViewportView(saida);
		
		lblName = new JLabel("Digite seu nome:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
							.addComponent(entrada, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEnviar))
						.addComponent(lblName))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(lblName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEnviar)
						.addComponent(entrada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addContainerGap())
		);
		getContentPane().setLayout(layout);

		pack();
	}

	private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			if (this.entrada.getText().trim().length() > 0) {
				if (first == true) {
					first = false;
					lblName.setText("Agora digite suas mensagens:");;
					out.writeUTF("#NOME" + this.entrada.getText());
					out.flush();
				} else {
					out.writeUTF(this.entrada.getText());
					out.flush();					
				}
				this.entrada.setText(null);
			}

			this.entrada.requestFocus();
		} catch (Exception ex) {
			ex.printStackTrace();
			sair();
		}
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		sair();
	}

	public void run() {
		try {
			while (true) {
				String msg = this.in.readUTF();
				this.saida.append(msg);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void sair() {
		try {
			out.writeUTF("SAIR");
			out.flush();
			if (!this.socket.isClosed()) {
				this.socket.close();
			}

			if (listener != null) {
				listener.interrupt();
				listener = null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Problema ao fechar socket.");
		}
	}

	public static void main(String args[]) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 2000);
			new UI(socket);
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			System.out.println("Host inválido.");
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Conexão inválida");
		}
	}
}