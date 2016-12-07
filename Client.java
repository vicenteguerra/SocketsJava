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

		Transmission transmission = new Transmission();

		transmission.readkey();

		try
		{
			System.out.println("Me conecto al puerto 8000 del servidor");
			socket = new Socket(a[0],8000);
			// Como ya hay socket, obtengo los flujos asociados a este
			DataInputStream dis = new DataInputStream( socket.getInputStream() );
			DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
			// Ya que me conecte con el Servidor, debo leer del teclado para despues eso mismo enviarlo al Servidor

			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			while (!request.equals("exit")){
				request = br.readLine();
				byte[] encrypted = transmission.encrypt(request);
				if(encrypted != null){
					transmission.sendMessage(dos, encrypted);
					byte[] theBytes =  transmission.receiveMessage(dis);
					msgServer = transmission.decrypt(theBytes);
					System.out.println("🤖" + msgServer);
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


}
