import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Transmission
{
  private static Key key = null;
  private static Cipher cifrar = null;

  public Transmission()throws Exception{
    System.out.println("conectando ...");
    this.cifrar = Cipher.getInstance("DES");
  }

public void createkey() throws Exception{
    try{
      ObjectInput in = new ObjectInputStream(new FileInputStream("key.ser"));
      this.key = (Key)in.readObject();
      in.close();
    }catch(IOException e){
      System.out.println( "Generando la llave en el Servidor..." );
      KeyGenerator keyGen = KeyGenerator.getInstance("DES");
      keyGen.init(56);
      this.key = keyGen.generateKey();
      System.out.println( "llave=" + this.key );
      System.out.println( "Llave generada!" );

      ObjectOutput out = new ObjectOutputStream(new FileOutputStream("key.ser"));
      out.writeObject( this.key );
      out.close();
  }
}

public void readkey()throws Exception{

  try{
    ObjectInput in = new ObjectInputStream(new FileInputStream("key.ser"));
    this.key= (Key)in.readObject();
    System.out.println( "llave=" + this.key );
    in.close();
  }catch(IOException e){
    System.out.println( "Error al leer la llave " );

  }
}

public byte[] encrypt(String request){
  byte[] arrayRequest = request.getBytes();
  try{
    this.cifrar.init(Cipher.ENCRYPT_MODE, this.key);
    byte[] cipherText = this.cifrar.doFinal( arrayRequest );
    return cipherText;
  }catch(Exception e){
    System.out.println("Encrypt Error");
    return null;
  }
}

  public String decrypt(byte[] theBytes){
    try{
      this.cifrar.init(Cipher.DECRYPT_MODE, this.key);
      byte[] newPlainText = this.cifrar.doFinal( theBytes );
    return new String(newPlainText, "UTF8") ;
    }catch(Exception e){
      System.out.println(e);
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
