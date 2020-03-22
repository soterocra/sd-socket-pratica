package pratica01;
import java.net.*;
import java.io.*;

public class Cliente {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 2000);
		DataOutputStream saida = new DataOutputStream(s.getOutputStream());
		DataInputStream entrada = new DataInputStream(s.getInputStream());
		
		for (int i = 0; i < 100000; i++) {
			saida.writeInt(i);
			System.out.println("Enviei: " + i);
			String en = entrada.readUTF();
			System.out.println("Recebi: " + en);
		}
	}
}
