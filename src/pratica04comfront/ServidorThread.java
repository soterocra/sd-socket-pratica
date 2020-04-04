package pratica04comfront;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorThread extends Thread {
	private Socket socket;
	private final static List<ServidorThread> clientes = new ArrayList<ServidorThread>();
	private DataInputStream in;
	private DataOutputStream out;

	String nome = "";

	public ServidorThread(Socket cliente) {
		try {
			this.socket = cliente;
			clientes.add(this);

			in = new DataInputStream(new BufferedInputStream(cliente.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(cliente.getOutputStream()));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			try {
				boolean sair = false;
				while (!sair) {
					String texto = in.readUTF();

					if (texto.startsWith("#NOME") && nome.equals("")) {
						nome = texto.replace("#NOME", "");
						enviarMensagem("Cliente " + nome + " entrou no chat.");
						synchronized (out) {
							out.writeUTF("Bem vindo ao chat " + nome + "\n");
						}
						out.flush();
					}

					if (nome.equals("")) {
						continue;
					}

					if ("SAIR".equals(texto)) {
						sair = true;
						clientes.remove(this);
						enviarMensagem("Cliente " + nome + " saiu do chat.");
					} else {
						enviarMensagem(texto);
					}
				}
			} finally {
				if (!this.socket.isClosed()) {
					this.socket.close();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("Não conseguiu comunicar com o cliente.");
		}
	}

	private void enviarMensagem(String mensagem) {
		if (mensagem.startsWith("#NOME"))
			return;

		System.out.println("Enviar msg: " + mensagem);
		this.writeLog(socket, mensagem);
		
		synchronized (clientes) {
			for (ServidorThread cliente : clientes) {
				try {
					synchronized (cliente.out) {
						cliente.out.writeUTF(nome + ": " + mensagem + "\n");
					}
					cliente.out.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
					cliente.interrupt();
				}
			}
		}
	}

	private void writeLog(Socket cliente, String mensagem) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("D:\\log.txt", true));
			writer.append('\n');
			writer.append(cliente.getInetAddress().getHostName() + "@" + cliente.getInetAddress().getHostAddress() + "@"
					+ cliente.getPort() + "#" + mensagem);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}