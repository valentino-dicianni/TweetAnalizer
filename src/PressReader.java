import Interfaces.TextReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class PressReader implements TextReader {
    private HashSet<String> pressCorpus = new HashSet<>();

    // ogni riga contiene un articolo di giornale

    @Override
    public HashSet<String> parseFile(String path) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while(line!=null) {
                pressCorpus.add(line);
                line = reader.readLine();

            }
            System.out.println("Done Processing press");
            return pressCorpus;

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }
}
