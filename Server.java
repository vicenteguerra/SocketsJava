import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;
import java.util.*;

public class Server
{
	public static void main(String a[]) throws Exception
	{
		ServerSocket serverSocket = null;
		Socket socket = null;
		// Peticion es lo que envia el Cliente
		byte peticion[] = new byte[1000];
		Key llave = null;
		String word = null;
		boolean exit = false;
		String response = "";

		Transmission transmission= new Transmission();
		transmission.createkey();

		CuentaHabiente clientAccount = new CuentaHabiente(123);
		clientAccount.setName("Zamitis");
		clientAccount.save();

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

			try
			{
				socket = serverSocket.accept();
				System.out.println("Se conecto un cliente: " + socket.getInetAddress().getHostName());
				// Como ya hay socket, obtengo los flujos asociados a este
				DataInputStream dis = new DataInputStream( socket.getInputStream() );
				DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
				// Despues de la conexion, Servidor y Cliente deben ponerse de acuerdo
				BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
				// Como el Cliente escribe, yo debo leer
				String msg = "";

				while (!exit){
					byte[] theBytes =  transmission.receiveMessage(dis);
					msg = transmission.decrypt(theBytes);
					System.out.println(msg);

					StringTokenizer st = new StringTokenizer(msg);
					while(st.hasMoreTokens()){
						word = st.nextToken().toUpperCase();
						switch(word){
							case "CONSULTAR":
								response = "Saldo: " + clientAccount.getBalance();
							break;
							case "DEPOSITAR":
								word = st.nextToken();
								clientAccount.deposit(Float.parseFloat(word));
								response = "Depositado: " + word;
							break;
							case "RETIRAR":
								word = st.nextToken();
								clientAccount.withdraw(Float.parseFloat(word));
								response = "Retirado: " + word;
							break;
							case "SALIR":
								exit = true;
							break;
							default:
								response = "Hola, puedes decirlo de otra forma por favor";
							break;
						}
					}
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
