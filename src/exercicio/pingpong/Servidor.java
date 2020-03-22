package exercicio.pingpong;

import java.net.*;
import java.io.*;

public class Servidor {
	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(2001);
		
		while (true) {
			System.out.println("Esperando conexão.");
			Socket conexao = s.accept();
			DataInputStream entrada = new DataInputStream(conexao.getInputStream());
			DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Esperando mensagem.");
			String linha = "";
			while (linha != null && !(linha.trim().equals("SAIR"))) {
				try {
					linha = entrada.readUTF();					
				} catch (Exception e) {
					break;
				}
				System.out.println("Mensagem recebida: " + linha);
				System.out.print("Digite a resposta: ");
				linha = teclado.readLine();
				saida.writeUTF(linha);			
				System.out.printf("Resposta enviada.%n");
			}
			System.out.println("Conexão encerrada.");
			conexao.close();
		}
	}
}
