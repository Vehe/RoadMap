import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class People 
{

    private static final String FILENAME = "/vagrant/userdata.txt";

    public static void main(String[] args) 
    {
        Scanner kb = new Scanner(System.in);
        
        System.out.println();
        System.out.println("Introduce los siguientes datos sobre un nuevo usuario ->");
        System.out.println();

        
        System.out.print("Nombre: ");
        String usrNom = kb.nextLine();

        System.out.print("Apellidos: ");
        String usrApe = kb.nextLine();

        System.out.print("Edad: ");
        String usrEdad = kb.nextLine();

        System.out.print("Sexo (H/M): ");
        String usrSexo = kb.nextLine();

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            String content = String.format("Nombre -> %s\n Apellidos -> %s\n Edad -> %s\n Sexo -> %s\n\n", usrNom, usrApe, usrEdad, usrSexo);

            fw = new FileWriter(FILENAME, true);
            bw = new BufferedWriter(fw);
            bw.write(content);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
        
        System.out.println();
        
    }

}