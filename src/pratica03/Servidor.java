package pratica03;

import java.net.*;
import java.io.*;

public class Servidor {
	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(2001);
		
		while (true) {
			System.out.print("Esperando conectar...");
			Socket conexao = s.accept();
			System.out.println("Conectou!");
			DataInputStream entrada = new DataInputStream(conexao.getInputStream());
			DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());

			String linha = entrada.readUTF();
			while (linha != null && !(linha.trim().equals(""))) {
				saida.writeUTF("echo servidor: " + linha);
				linha = entrada.readUTF();
			}
			saida.writeUTF(linha);
			conexao.close();

		}
	}
}
