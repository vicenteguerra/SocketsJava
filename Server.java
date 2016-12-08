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
		int port = 8000;

		Transmission transmission= new Transmission();
		transmission.createkey();

		try
		{
			System.out.println("Escuchando por el puerto "+ Integer.toString(port));
			serverSocket = new ServerSocket(port);
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
				System.out.println("Se conectó el cliente: "+ socket.getInetAddress().getHostName());
				// Como ya hay socket, obtengo los flujos asociados a este

				DataInputStream dis = new DataInputStream( socket.getInputStream() );
				DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
				// Despues de la conexion, Servidor y Cliente deben ponerse de acuerdo
				BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
				// Como el Cliente escribe, yo debo leer
				String msg = "";

				byte[] theBytes =  transmission.receiveMessage(dis);
				msg = transmission.decrypt(theBytes);
				CuentaHabiente clientAccount = new CuentaHabiente(Integer.parseInt(msg));
				if(clientAccount.getName() == null){
					byte[] encrypted = transmission.encrypt("FAIL");
					transmission.sendMessage(dos, encrypted);
					theBytes =  transmission.receiveMessage(dis);
					msg = transmission.decrypt(theBytes);
					clientAccount.setName(msg);
					encrypted = transmission.encrypt("Bienvenido " + msg);
					transmission.sendMessage(dos, encrypted);
				}else{
					byte[] encrypted = transmission.encrypt("Bienvenido " + clientAccount.getName());
					transmission.sendMessage(dos, encrypted);
				}

				while (!exit){
					theBytes =  transmission.receiveMessage(dis);
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
