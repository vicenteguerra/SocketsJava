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

		try{
			ObjectInput in = new ObjectInputStream(new FileInputStream("key.ser"));
			llave = (Key)in.readObject();
			System.out.println( "llave=" + llave );
			in.close();
		}catch(IOException e){
			System.out.println( "Generando la llave en el Servidor..." );
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56);
			llave = keyGen.generateKey();
			System.out.println( "llave=" + llave );
			System.out.println( "Llave generada!" );

			ObjectOutput out = new ObjectOutputStream(new FileOutputStream("key.ser"));
			out.writeObject( llave );
			out.close();
		}

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

				int byteLength = dis.readInt(); // now I know how many bytes to read
				byte[] theBytes = new byte[byteLength];
				dis.readFully(theBytes);
				System.out.println( "Descifrando el mensaje ..." );

				Cipher cifrar = Cipher.getInstance("DES");
				cifrar.init(Cipher.DECRYPT_MODE, llave);
				byte[] newPlainText = cifrar.doFinal( theBytes );
				System.out.println( new String(newPlainText, "UTF8") );
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
