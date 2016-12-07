import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Client
{

	private static Key key = null;

	public static void main(String a[]) throws Exception
	{
		Socket socket = null;
		String request = "";

		Client client = new Client();

		ObjectInput in = new ObjectInputStream(new FileInputStream("key.ser"));
		client.key = (Key)in.readObject();

		System.out.println( "llave=" + client.key);
		in.close();

		Transmission transmission = new Transmission();
		transmission.sayHello();

		try
		{
			System.out.println("Me conecto al puerto 8000 del servidor");
			socket = new Socket(a[0],8000);
			// Como ya hay socket, obtengo los flujos asociados a este
			DataInputStream dis = new DataInputStream( socket.getInputStream() );
			DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );
			// Ya que me conecte con el Servidor, debo leer del teclado para despues eso mismo enviarlo al Servidor
			BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
			request = br.readLine();

			byte[] encrypted = client.encrypt(request);
			if(encrypted != null){
				client.sendMessage(dos, encrypted);
			}else{
				System.out.println("Conexión insegura");
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

	public byte[] encrypt(String request){
		System.out.println( "Cifrando mensaje... " );
		byte[] arrayRequest = request.getBytes();
		try{
			Cipher cifrar = Cipher.getInstance("DES");
			cifrar.init(Cipher.ENCRYPT_MODE, this.key);
			byte[] cipherText = cifrar.doFinal( arrayRequest );
			return cipherText;
		}catch(Exception e){
			System.out.println("Encrypt Error");
			return null;
		}
	}

	public String decrypt(byte[] theBytes){
		try{
			Cipher cifrar = Cipher.getInstance("DES");
			cifrar.init(Cipher.DECRYPT_MODE, this.key);
			byte[] newPlainText = cifrar.doFinal( theBytes );
		return new String(newPlainText, "UTF8") ;
		}catch(Exception e){
			System.out.println("Decrypt Error");
			return null;
		}
	}

	public void sendMessage(DataOutputStream dos, byte[] cipherText){
		try{
			String string = new String(cipherText);
			dos.writeInt(cipherText.length);
			dos.write(cipherText);
		}catch(IOException e){
			System.out.println(e);
		}
	}

	public byte[] receiveMessage(DataInputStream dis){
		try{
			int byteLength = dis.readInt(); // now I know how many bytes to read
			byte[] theBytes = new byte[byteLength];
			dis.readFully(theBytes);
			return theBytes;
		}catch(IOException e){
			System.out.println(e);
			return null;
		}
	}

}
