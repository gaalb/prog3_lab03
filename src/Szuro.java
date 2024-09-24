import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Szuro {
    public static void main(String[] args) {
        String input = null;
        String output = null;
        String pattern = "";
        boolean gi = false;
        boolean go = false;
        for (int i = 0; i < args.length; i++) {
            if ((i+1 < args.length) && args[i].equals("-i")) {
                i++;
                input = args[i];
            } else if ((i+1 < args.length) && args[i].equals("-o")) {
                i++;
                output = args[i];
            } else if ((i+1 < args.length) && args[i].equals("-p")) {
                i++;
                pattern = args[i];
            } else if (args[i].equals("-go")) {
                go = true;
            } else if (args[i].equals("-gi")) {
                gi = true;
            }
        }
        try (BufferedReader reader = gi
                ? new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(input))))
                : new BufferedReader(new FileReader(input));
             BufferedWriter writer = go
                ? new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(output))))
                : new BufferedWriter(new FileWriter(input))) {
            while (true) {
                String line = reader.readLine();
                if (line==null) break;
                if (line.matches(pattern)) writer.write(line+System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
