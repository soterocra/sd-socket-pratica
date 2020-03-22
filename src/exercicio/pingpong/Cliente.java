package exercicio.pingpong;

import java.net.*;
import java.io.*;

public class Cliente {
	public static void main(String[] args) throws IOException {
		Socket conexao = new Socket("127.0.0.1", 2001);
		DataInputStream entrada = new DataInputStream(conexao.getInputStream());
		DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

		String linha = "";
		while (linha != null && !(linha.trim().equals("SAIR"))) {
			System.out.print("Digite a mensagem: ");
			linha = teclado.readLine();
			if (linha.equals("SAIR")) {
				continue;
			}
			saida.writeUTF(linha);			
			System.out.printf("Mensagem enviada.%nEsperando resposta.%n");
			linha = entrada.readUTF();
			System.out.println("Resposta recebida: " + linha);
		}
		System.out.println("Desconectando.");
		conexao.close();
	}
}
