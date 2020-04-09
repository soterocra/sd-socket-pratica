package pratica05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Cliente {
	public static void main(String[] args) throws IOException {
		DatagramSocket s = new DatagramSocket();
		InetAddress dest = InetAddress.getByName("localhost");

		String envio;
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("> ");
		envio = teclado.readLine();

		while (!envio.equalsIgnoreCase("")) {
			byte[] buffer = envio.getBytes();

			DatagramPacket msg = new DatagramPacket(buffer, buffer.length, dest, 4545);

			s.send(msg);

			DatagramPacket resposta = new DatagramPacket(new byte[512], 512);

			s.receive(resposta);

			for (int i = 0; i < resposta.getLength(); i++) {
				System.out.print((char) resposta.getData()[i]);
			}
			System.out.println();

			System.out.print("> ");
			envio = teclado.readLine();
		}
	}
}
