import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Client
{

	public static void main(String a[]) throws Exception
	{
		Socket socket = null;
		String request = "";
		String msgServer = "";
		int port = 8000;

		Transmission transmission = new Transmission();

		transmission.readkey();

		try
		{
			System.out.println("Conectando al puerto " + Integer.toString(port));
			socket = new Socket(a[0],port);
			// Como ya hay socket, obtengo los flujos asociados a este
			DataInputStream dis = new DataInputStream( socket.getInputStream() );
			DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
			// Ya que me conecte con el Servidor, debo leer del teclado para despues eso mismo enviarlo al Servidor

			checkAccount(dis, dos, transmission);

			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			while (!request.toUpperCase().equals("SALIR")){
				request = br.readLine();
				byte[] encrypted = transmission.encrypt(request);
				if(encrypted != null){
					transmission.sendMessage(dos, encrypted);
					byte[] theBytes =  transmission.receiveMessage(dis);
					msgServer = transmission.decrypt(theBytes);
					System.out.println("🤖  " + msgServer);
				}else{
					System.out.println("Conexión insegura");
				}
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

	public static void checkAccount(DataInputStream dis, DataOutputStream dos, Transmission transmission) throws Exception {
		System.out.println("Ingresa tu número de cuenta: ");
		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
		String request = "";
		String msgServer = "";
		request = br.readLine();
		byte[] encrypted = transmission.encrypt(request);
		if(encrypted != null){
			transmission.sendMessage(dos, encrypted);
			byte[] theBytes =  transmission.receiveMessage(dis);
			msgServer = transmission.decrypt(theBytes);
			if(msgServer.equals("FAIL")){
				System.out.println("Registro: \nPor favor ingresa tu nombre: ");
				request = br.readLine();
				encrypted = transmission.encrypt(request);
				if(encrypted != null){
					transmission.sendMessage(dos, encrypted);
					theBytes =  transmission.receiveMessage(dis);
					msgServer = transmission.decrypt(theBytes);
					System.out.println(msgServer);
				}else{
					System.out.println("Conexión insegura");
				}
			}else{
				System.out.println(msgServer);
			}
		}else{
			System.out.println("Conexión insegura");
		}
	}

}
