import java.io.*;
import java.lang.*;

public class CuentaHabiente implements Serializable{
    private String name;
    private int account;
    private float balance;
    private String country;
    private String postalcode;

    public CuentaHabiente() {
    }

    public CuentaHabiente(int account) throws Exception{
        try {
            FileInputStream arc_cliente = new FileInputStream(Integer.toString(account) + ".dat");
            ObjectInputStream entrada = new ObjectInputStream(arc_cliente);
            CuentaHabiente clientAccount = (CuentaHabiente) entrada.readObject();
            this.name = clientAccount.name;
            this.account = clientAccount.account;
            this.balance = clientAccount.balance;
            this.country = clientAccount.country;
            this.postalcode = clientAccount.postalcode;
        } catch (FileNotFoundException e) {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(Integer.toString(account) + ".dat"));
            this.account = account;
            this.name = null;
            out.writeObject( this );
            out.close();
        }
    }

    public String getName() {
        return name;
    }

    public int getAccount() {
        return account;
    }

    public float getBalance() {
        return balance;
    }

    public String getCountry() {
        return country;
    }

    public String getPostal() {
        return postalcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostal(String postalcode) {
        this.postalcode = postalcode;
    }

    public void deposit(Float amount)throws Exception{
      this.balance += amount;
      this.save();
    }

    public void withdraw(Float amount)throws Exception{
      this.balance -= amount;
      this.save();
    }

    public void save()throws Exception{
      try{
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(Integer.toString(this.account) + ".dat"));
        out.writeObject( this );
        out.close();
      }catch(FileNotFoundException e){
        System.out.println("Error al escribir el archivo");
      }
    }
}
