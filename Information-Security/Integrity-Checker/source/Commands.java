import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Commands {


    public void GenerateKey(String privateKeyPath) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();

        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -genkey -alias alias -keyalg RSA -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -keysize 2048");
        } else {
            builder.command("sh", "-c", "keytool -genkey -keyalg RSA -keystore "+privateKeyPath+" -keysize 2048");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        final PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        writer.print("123456\n");
        writer.print("123456\n");
        writer.print("software developers\n");
        writer.print("San Francisco\n");
        writer.print("California\n");
        writer.print("US\n");
        writer.print("yes\n");
        writer.print("yes\n");
        writer.print("yes\n");
        writer.print("123456\n"); // These and other writer.print(...) statements
        writer.print("123456\n");
        writer.close();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        Thread.sleep(1000);

    }

    public void GenerateCert(String privateKeyPath,String certificatePath) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -v -export -file E:\\ideaprojects\\untitled8\\a\\cert.cer -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks");

        } else {
            String dirpri = privateKeyPath.substring(privateKeyPath.indexOf("/")+1);
            String dircert = certificatePath.substring(certificatePath.lastIndexOf("/")+1);
            builder.command("sh", "-c", "keytool -export -keystore "+certificatePath+" -rfc -file "+privateKeyPath+" -storepass 123456");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        final PrintWriter writer= new PrintWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        // writer.print("123456\n"); // These and other writer.print(...) statements
        // writer.print("123456\n");
        writer.close();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        Thread.sleep(1000);
    }

    public void ImportKey(String privateKeyPath) throws InterruptedException, IOException {
        String dir = privateKeyPath.substring(0,privateKeyPath.lastIndexOf("/")+1);
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -v -export -file E:\\ideaprojects\\untitled8\\a\\cert.cer -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks");

        } else {

            builder.command("sh", "-c", "keytool -importkeystore -srckeystore "+privateKeyPath+" -srcstorepass 123456 -destkeystore "+dir+"identity.p12 -deststoretype PKCS12 -deststorepass 123456 -destkeypass 123456");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        Thread.sleep(1000);
    }

    public void CreateKeyPem(String privateKeyPath) throws InterruptedException, IOException {
        String dir = privateKeyPath.substring(0,privateKeyPath.lastIndexOf("/")+1);
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -v -export -file E:\\ideaprojects\\untitled8\\a\\cert.cer -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks");

        } else {
            builder.command("sh", "-c", "openssl pkcs12 -in "+dir+"identity.p12 -nodes -nocerts -out "+dir+"private_key.pem -password pass:123456");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process= builder.start();
        final PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        writer.print("123456\n");
        writer.print("123456\n");
        writer.close();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

        Thread.sleep(1000);
    }

    public String ReadPrivateFile(String prikeypath) throws InterruptedException, IOException {
        Thread.sleep(3000);

        String dir = prikeypath.substring(0,prikeypath.indexOf("/")+1);
        File f = new File("private_key.pem");
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String key = new String(keyBytes);
        int a = key.indexOf("-");
        key = key.substring(a+"-----BEGIN PRIVATE KEY-----\n".length());
        key = key.replace("-----END PRIVATE KEY-----","").trim();
        return key;
    }

    public String GetPass() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter password");

        String password = myObj.nextLine();  // Read user input
        System.out.println("Your password is: " + password);  // Output user input
        return password;
    }

    public String GetMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String EncryptAES(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

        return new String(encoder.encodeToString(crypted));
    }

    public static String DecryptAES(String input, String key) {
        byte[] output = null;
        try {
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }

    public void CreatePriKeyFile(String priKeyPath, String encrypted) throws IOException {
        File file = new File(priKeyPath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write(encrypted);
        writer.close();
    }

    public void CreatePubKeyCertificateFile(String pubKeyCertificatePath) throws IOException {
        File f = new File("public.crt");
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        String certificate = new String(keyBytes);
        File file = new File(pubKeyCertificatePath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write(certificate);
        writer.close();
    }
}