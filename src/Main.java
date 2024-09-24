import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    protected static File wd = new File(System.getProperty("user.dir"));
    public static void handle(String  s) {
        String[] cmd = s.split(" ");
        try {
            if (cmd[0].equals("exit")) {
                System.exit(0);
            } else if (cmd[0].equals("ls")) {
                ls(cmd);
            } else if (cmd[0].equals("pwd")) {
                pwd(cmd);
            } else if (cmd[0].equals("cd")) {
                cd(cmd);
            } else if (cmd[0].equals("rm")) {
                rm(cmd);
            } else if (cmd[0].equals("cp")) {
                cp(cmd);
            } else if (cmd[0].equals("mv")) {
                mv(cmd);
            } else if (cmd[0].equals("mkdir")) {
                mkdir(cmd);
            } else if (cmd[0].equals("cat")) {
                cat(cmd);
            } else if (cmd[0].equals("length")) {
                length(cmd);
            } else if (cmd[0].equals("head")) {
                head(cmd);
            } else if (cmd[0].equals("tail")) {
                tail(cmd);
            } else if (cmd[0].equals("wc")) {
                wc(cmd);
            } else if (cmd[0].equals("grep")) {
                grep(cmd);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Not a valid file.");
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong argument(s) for command " + cmd[0] + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void grep(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 3) throw new IllegalArgumentException();
        File f = new File(wd, cmd[2]);
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            loop: while (true) {
                String line = reader.readLine();
                if (line == null) break loop;
                if (line.matches(cmd[1])) System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void wc(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 2) throw new IllegalArgumentException();
        File f = new File(wd, cmd[1]);
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        int lineNum = 0;
        int wordNum = 0;
        int charNum = 0;
        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                String[] words = line.split(" ");
                wordNum += words.length;
                charNum += line.length();
            }
            System.out.println(f + ": " + lineNum + " lines, " + wordNum + " words, " + charNum + " characters.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void tail (String[] cmd) throws IllegalArgumentException, FileNotFoundException  {
        // Either we expect 1 argument (the file), or 3 (<file>, -n, <num>), so cmd is either length 2 or 4
        if (!(cmd.length == 2 || cmd.length == 4)) throw new IllegalArgumentException();
        if (cmd.length == 4 && !cmd[2].equals("-n")) throw new IllegalArgumentException();
        int numLines = 10;
        if (cmd.length == 4) {
            try {
                numLines = Integer.parseInt(cmd[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }
        File f = new File(wd, cmd[1]); // make sure to make the folder relative to wd
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        try (Scanner scanner = new Scanner(f)) {
            LinkedList<String> lst1 = new LinkedList<>();
            while (scanner.hasNextLine()) {
                lst1.addFirst(scanner.nextLine());  // store all the strings in a linked list, in reverse order
            }
            LinkedList<String> lst2 = new LinkedList<>();
            while (numLines > 0 && !lst1.isEmpty()) {
                numLines--;
                // read the first numLines line in reverse order. Since they were reversed, they will be ordered right again.
                lst2.addFirst(lst1.pop());
            }
            for (String s: lst2) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void head (String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        // Either we expect 1 argument (the file), or 3 (<file>, -n, <num>), so cmd is either length 2 or 4
        if (!(cmd.length == 2 || cmd.length == 4)) throw new IllegalArgumentException();
        if (cmd.length == 4 && !cmd[2].equals("-n")) throw new IllegalArgumentException();
        int numLines = 10;
        if (cmd.length == 4) {
            try {
                numLines = Integer.parseInt(cmd[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }
        File f = new File(wd, cmd[1]); // make sure to make the folder relative to wd
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine() && numLines > 0 ) {
                numLines--;
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void length(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 2) throw new IllegalArgumentException();
        File f = new File(wd, cmd[1]); // make sure to make the folder relative to wd
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        System.out.println(f.length());
    }
    protected static void cat(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 2) throw new IllegalArgumentException();
        File f = new File(wd, cmd[1]); // make sure to make the folder relative to wd
        if (!f.exists() || !f.isFile()) throw new FileNotFoundException();
        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void mkdir(String[] cmd) throws IllegalArgumentException {
        if (cmd.length != 2) throw new IllegalArgumentException();
        File folder = new File(wd, cmd[1]);  // make sure to make the folder relative to wd
        if (!folder.mkdir()) {
            System.out.println("Folder cannot be created.");
        }
    }
    protected static void mv(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 3) throw new IllegalArgumentException();
        File originalFile = new File(wd, cmd[1]);  // make sure the file is relative to wd
        File newFile = new File(wd, cmd[2]); // make sure the file is relative to wd
        if (!originalFile.exists() | !originalFile.isFile()) throw new FileNotFoundException();
        if (!originalFile.renameTo(newFile)) {
            System.out.println("File cannot be renamed.");
        }
    }
    protected static void cp(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 3) throw new IllegalArgumentException();
        File sourceFile = new File(wd, cmd[1]); // make sure the file is relative to wd
        File targetFile = new File(wd, cmd[2]); // make sure the file is relative to wd
        if (!sourceFile.exists() | !sourceFile.isFile()) throw new FileNotFoundException();
        try (FileInputStream read = new FileInputStream(sourceFile);
             FileOutputStream write = new FileOutputStream(targetFile)) {
            while (read.available() != 0) {
                write.write(read.read());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean recursiveDelete(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {  // directories must be cleared out recursively before deleting
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file: files) {
                        recursiveDelete(file);
                    }
                }
            }
            return dir.delete();
        }
        return false;
    }
    protected static void rm(String[] cmd) throws IllegalArgumentException, FileNotFoundException {
        if (cmd.length != 2) throw new IllegalArgumentException();
        File f = new File(wd, cmd[1]); // make sure the path is relative to wd
        if (!f.exists()) throw new FileNotFoundException();
        if (!recursiveDelete(f)) {
            System.out.println("File/Directory cannot be deleted.");
        }
    }
    protected static void cd(String[] cmd) throws IOException, IllegalArgumentException{
        if (cmd.length != 2) throw new IllegalArgumentException();
        String newFile;
        if (cmd[1].startsWith("/")) {  // absolute path: do not concatenate it to wd
            newFile = cmd[1];
        } else {
            newFile = wd.toString() + "/" +cmd[1];  // concatenate to wd
        }
        File f = new File(newFile);  // newFile contains an absolute path now
        if (f.exists() && f.isDirectory()) {
            wd = f.getCanonicalFile();
        } else {
            System.out.println("Invalid location.");
        }
    }

    protected static void pwd(String[] cmd) throws IOException{
        //System.out.println("pwd: " + wd + ", items: " + wd.listFiles().length);
        System.out.println(wd.getCanonicalPath());

    }
    protected static void ls(String[] cmd) {
        for (File f: wd.listFiles()) {
            String s = f.toString();
            // intentional && instead of &, make use of short circuit
            if (cmd.length > 1 && cmd[1].equals("-l") && f.exists()) {
                if (f.isDirectory()) {
                    s = s + " d";
                } else if (f.isFile()) {
                    s = s + " f";
                }
            }
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        int len = args.length;
        try (BufferedReader reader = len > 0
                ? new BufferedReader(new FileReader(args[0]))
                : new BufferedReader(new InputStreamReader(System.in))){
            loop: while (true) {
                //System.out.print("\n" + wd + ">");
                String line = reader.readLine();
                if (line == null) {
                    break loop;
                } else {
                    handle(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}