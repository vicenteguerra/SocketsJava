import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Server
{
	public static void main(String a[]) throws Exception
	{
		ServerSocket serverSocket = null;
		Socket socket = null;
		// Peticion es lo que envia el Cliente
		byte peticion[] = new byte[1000];
		Key llave = null;

		Transmission transmission= new Transmission();
		transmission.createkey();

		try
		{
			System.out.println("Escuchando por el puerto 8000");
			serverSocket = new ServerSocket(8000);
		}
		catch(IOException e)
		{
			System.out.println("java.io.IOException generada");
			e.printStackTrace();
		}

		System.out.println("Esperando a que los clientes se conecten...");
		while(true)
		{
			try
			{
				socket = serverSocket.accept();
				System.out.println("Se conecto un cliente: " + socket.getInetAddress().getHostName());
				// Como ya hay socket, obtengo los flujos asociados a este
				DataInputStream dis = new DataInputStream( socket.getInputStream() );
				DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
				// Despues de la conexion, Servidor y Cliente deben ponerse de acuerdo
				// para ver quien escribe primero y entonces el otro debe leer
				BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
				// Como el Cliente escribe, yo debo leer
				String msg = "";

				while (!msg.equals("exit")){
					byte[] theBytes =  transmission.receiveMessage(dis);
					msg = transmission.decrypt(theBytes);
					System.out.println(msg);
					String response = "Lalala";
					byte[] encrypted = transmission.encrypt(response);
					transmission.sendMessage(dos, encrypted);
				}
				dos.close();
				dis.close();
				socket.close();

			}
			catch(IOException e)
			{
				System.out.println("java.io.IOException generada");
				e.printStackTrace();
			}


		}
	}

}
