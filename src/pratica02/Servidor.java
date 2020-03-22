package pratica02;
import java.net.*;
import java.io.*;

public class Servidor {
	public static void main(String[] args) throws IOException {
		InetAddress endereco_remoto;
		int porta_remota;
		ServerSocket s = new ServerSocket(2000);
		System.out.println("Esperando conexão...");
		Socket conexao = s.accept();
		System.out.println("Conexão aceita, esperando envio de dados...");
		endereco_remoto = conexao.getInetAddress();
		porta_remota = conexao.getPort();
		
		System.out.println("Nome da maquina remota: " + endereco_remoto.getHostName());
		System.out.println("IP da maquina remota: " + endereco_remoto.getHostAddress());
		System.out.println("Porta maquina remota: " + porta_remota);
	}
}
