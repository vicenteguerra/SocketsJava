import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.*;

public class Cliente {

    public static void main(String[] args) throws Exception{

        FileInputStream arc_cliente = null;
        ObjectInputStream entrada = null;

        try {
            //ObjectInput in = new ObjectInputStream(new FileInputStream("key.ser"));
            arc_cliente = new FileInputStream("6.dat");
            entrada = new ObjectInputStream(arc_cliente);
            CuentaHabiente ch = (CuentaHabiente) entrada.readObject();
            //chbte = (CHBT)entrada.readObject();
            System.out.println( "CuentaHabiente=" + ch.getBalance() );
            //System.out.println(ch.getName() + " " + ch.getAccount() + " " + ch.getBalance());
        } catch (FileNotFoundException e) {
            CuentaHabiente ch = new CuentaHabiente();
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream("6.dat"));
            ch.setBalance(198);
            //out.writeObject( ch );
            out.close();
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (arc_cliente != null) {
                    arc_cliente.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
