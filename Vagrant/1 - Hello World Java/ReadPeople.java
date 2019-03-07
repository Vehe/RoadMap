import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ReadPeople 
{

    private static final String FILENAME = "/vagrant/userdata.txt";

    public static void main(String[] args) 
    {
        
        System.out.println();
        System.out.println("Datos actuales de los usuarios ->");
        System.out.println();

        BufferedReader br = null;

        try {

            String strCurrentLine;
            br = new BufferedReader(new FileReader(FILENAME));
            while ((strCurrentLine = br.readLine()) != null) {    
                System.out.println(strCurrentLine);  
            } 

        } catch(FileNotFoundException exception) {

            System.out.println("No se dispone de datos de usuario actualmente.");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close(); 

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
        
        System.out.println();
        
    }

}