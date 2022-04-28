import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class Server extends Thread {

    private static final int PORT = 7520;
    private ServerSocket listener = null;
    private static ArrayList<PrintWriter> printWriterList;
    private static ArrayList<String> nameList;
    BufferedReader input;
    PrintWriter output;
    Writer log_file;
    public Cryptor cryptor;
    DataOutputStream dos;
    DataInputStream dis;

    Server() throws IOException {
        log_file = new BufferedWriter(new FileWriter("log.txt"));
        listener = new ServerSocket(PORT);
        nameList = new ArrayList<String>();
        printWriterList = new ArrayList<PrintWriter>();
    }

    public void run() {
        System.out.println("Server Listening on port : " + listener.getLocalPort());
        try {
            this.cryptor = new Cryptor();
            String aesKey = Base64.getEncoder().encodeToString(cryptor.aesKey.getEncoded());
            String desKey = Base64.getEncoder().encodeToString(cryptor.desKey.getEncoded());
            String aesIV = Base64.getEncoder().encodeToString(cryptor.aesIV);
            String desIV = Base64.getEncoder().encodeToString(cryptor.desIV);
            log_file.write("AES KEY : " + aesKey + "\n");
            log_file.write("DES KEY : " + desKey + "\n");
            log_file.write("AES IV : " + aesIV + "\n");
            log_file.write("DES IV : " + desIV + "\n");
            log_file.flush();

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket cliListener = listener.accept();
                dis = new DataInputStream(cliListener.getInputStream());
                dos = new DataOutputStream(cliListener.getOutputStream());
                input = new BufferedReader(new InputStreamReader(cliListener.getInputStream()));
                output = new PrintWriter(cliListener.getOutputStream(), true);
                new Read(input, output, cryptor, dis, dos).start();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Thread thread = new Server();
        thread.start();
    }

    public void broadcast(String message, PrintWriter out) {
        for (PrintWriter pw : printWriterList) {
            if (pw != out) {
                pw.println(message);
            }
        }
    }

    class Read extends Thread {
        String name;
        BufferedReader inp;
        PrintWriter out;
        public Cryptor cryptor;
        DataInputStream dis;
        DataOutputStream dos;

        Read(BufferedReader inp, PrintWriter out, Cryptor cryptor, DataInputStream dis, DataOutputStream dos) {
            this.inp = inp;
            this.out = out;
            this.cryptor = cryptor;
            this.dis = dis;
            this.dos = dos;
        }
        public void run() {
            try {
                String s;
                name = inp.readLine();
                String authResult = "added";
                out.println(authResult);
                printWriterList.add(output);
                nameList.add(name);
                System.out.println(name + " joined.");
                dos.writeUTF(Base64.getEncoder().encodeToString(cryptor.aesIV));
                dos.flush();
                dos.writeUTF(Base64.getEncoder().encodeToString(cryptor.desIV));
                dos.flush();
                dos.writeUTF(Base64.getEncoder().encodeToString(cryptor.aesKey.getEncoded()));
                dos.flush();
                dos.writeUTF(Base64.getEncoder().encodeToString(cryptor.desKey.getEncoded()));
                dos.flush();
                broadcast(name + " joined.", out);
                s = inp.readLine().trim();
                while (!s.equals("8ylVQeGptt/GeeyA0X3UoA==^")) {
                    while (s.equals("")) {
                        s = inp.readLine();
                    }
                    if (s.indexOf("~<N!_/_!D>~") != -1 && s.indexOf(" left") != -1 && s.indexOf(":") == -1) {
                        break;
                    }
                    System.out.println(name + " : " + s);
                    broadcast(s, out);
                    if (!s.contains("<"+name+">")){
                        log_file.write("<"+name+"> : "+s+"\n");
                        log_file.flush();
                    }
                    s = inp.readLine().trim();
                }
                System.out.println(name + " left");
                broadcast(name + " left", out);
                for (int i = 0; i != printWriterList.size(); i++) {
                    if (printWriterList.get(i).equals(out)) {
                        printWriterList.remove(i);
                        nameList.remove(i);
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
