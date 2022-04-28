import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class CreateReg {

    public static String ReadFile(String FilePath) throws IOException {
        File f = new File(FilePath);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        return new String(keyBytes);
    }

    public static void CheckPassword(String decryptedPrivateKey, FileWriter LogFileWriter) throws IOException {
        int length = decryptedPrivateKey.length();
        int length2 = "This is the private key file".length();
        String split = decryptedPrivateKey.substring(length - length2);
        if (split.equals("This is the private key file")) {
            System.out.println("True password!");
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String write = timestamp + " : Wrong password attempt!\n";
            LogFileWriter.write(write);
            LogFileWriter.close();
            System.out.println("Wrong password attempt!");
            System.exit(0);
        }
    }

    public static String DecryptAES(String input, String key, FileWriter LogFileWriter) throws IOException {
        byte[] output = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String write = timestamp + " : Wrong password attempt!\n";
            LogFileWriter.write(write);
            LogFileWriter.close();
            System.out.println("Wrong password attempt!");
            System.exit(0);
        }
        return new String(output);
    }

    public static File CreateFile(String Path) throws IOException {
        File file = new File(Path);
        file.getParentFile().mkdirs();
        return file;
    }

    public static void CreateLogFile(File[] listOfFiles, FileWriter logFileWriter, String regFilePath, String SourcePath) throws IOException {
        int i = 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String write = timestamp + " : Registry file is created at " + regFilePath + "\n";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                timestamp = new Timestamp(System.currentTimeMillis());
                String path = SourcePath + "/" + file.getName();
                String line = timestamp + " : " + path + " is added to registry.\n";
                logFileWriter.write(line);
                i++;
            }
        }

        timestamp = new Timestamp(System.currentTimeMillis());
        String last = timestamp + " : " + i + " files are added to the registry and registry creation is finished!\n";
        logFileWriter.write(last);
        logFileWriter.close();
    }

    public static String CreateRegFile(File[] listOfFiles, FileWriter regFileWriter, String sourcePath, String RegFilePath, String HashMode, String privateKeyContent) throws Exception {
        assert listOfFiles != null;
        String WholeFile = "";
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String path = sourcePath + "/" + file.getName();
                if (HashMode.equals("MD5")) {
                    String input = ReadFile(path);

                    path = path + " " + getMd5(input) + "\n";
                    WholeFile = WholeFile + path;
                    regFileWriter.write(path);
                } else if (HashMode.equals("SHA-256")) {
                    String input = ReadFile(path);
                    path = path + " " + toHexString(getSHA(input)) + "\n";
                    WholeFile = WholeFile + path;
                    regFileWriter.write(path);
                }
            }
        }
        return WholeFile;
    }

    public static void checkIntegrityOfDirectory(File[] listOfFiles, String sourcePath, String HashMode, String regfile, FileWriter logwriter) throws IOException, NoSuchAlgorithmException {
        boolean notChanged=true;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String path = sourcePath + "/" + file.getName();
                if (HashMode.equals("MD5")) {
                    String input = ReadFile(path);


                    if (regfile.contains(path) && !regfile.contains(getMd5(input).toLowerCase())) {
                        notChanged=false;
                        String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                        logwriter.write(s + " " + path + " altered \n");
                        logwriter.flush();
                    } else if (!regfile.contains(path)) {
                        String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                        ArrayList<String> l = new ArrayList<String>(Arrays.asList(regfile.split(" ")));

                        if (regfile.contains(path) || l.contains(path)) {
                            notChanged=false;
                            logwriter.write(s + " " + path + " deleted \n");
                            logwriter.flush();
                        } else {
                            notChanged=false;
                            logwriter.write(s + " " + path + " created \n");
                            logwriter.flush();
                        }
                    }

                } else if (HashMode.equals("SHA-256")) {
                    String input = ReadFile(path);
                    if (regfile.contains(path) && !regfile.contains(DatatypeConverter.printHexBinary(getSHA(input)).toLowerCase())) {
                        notChanged=false;
                        String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                        logwriter.write(s + " " + path + " altered \n");
                        logwriter.flush();
                    }
                    else if (!regfile.contains(path)) {
                        String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                        ArrayList<String> l = new ArrayList<String>(Arrays.asList(regfile.split(" ")));
                        if (l.contains(path)) {
                            notChanged=false;
                            logwriter.write(s + " " + path + " deleted \n");
                            logwriter.flush();
                        } else {
                            notChanged=false;
                            logwriter.write(s + " " + path + " created \n");
                            logwriter.flush();
                        }
                    }
                }
            }
        }
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\\w+.txt")
                .matcher(regfile);
        while (m.find()) {
            allMatches.add(m.group());
        }

        for (String e : allMatches) {

            if (e.contains(".txt")) {
                ArrayList<String> files = new ArrayList<String>();
                for (File file : listOfFiles) {
                    files.add(file.getName());
                }
                if (!files.contains(e)) {
                    notChanged=false;
                    String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                    logwriter.write(s + " " + sourcePath + "/" + e + " deleted \n");
                    logwriter.flush();
                }
            }
        }
        if(notChanged){
            String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
            logwriter.write(s + " The directory is checked and no change is detected!\n");
            logwriter.flush();
        }
    }

    public static byte[] Sign(String wholereg, String hashmode, String priPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, InterruptedException {
        Commands commands = new Commands();
        String a = commands.ReadPrivateFile(priPath);
        a = a.replace("\n", "");
        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(a);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        Signature sign = null;
        if (hashmode.equals("SHA-256")) {
            sign = Signature.getInstance("SHA256withRSA");
        } else {
            sign = Signature.getInstance("MD5withRSA");
        }
        sign.initSign(privKey);

        byte[] bytes = wholereg.trim().getBytes();
        sign.update(bytes);
        return sign.sign();
    }

    public static boolean verifySignature(String wholereg, byte[] signed, String cert, String hashmode) throws FileNotFoundException, CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        FileInputStream fin = new FileInputStream(cert);
        Signature signS = Signature.getInstance("SHA256withRSA");
        Signature signM = Signature.getInstance("MD5withRSA");
        X509Certificate certificate = X509Certificate.getInstance(fin);
        PublicKey pk = certificate.getPublicKey();
        boolean bool;
        if (hashmode.equals("SHA-256")) {
            signS.initVerify(pk);
            signS.update(wholereg.trim().getBytes());
            bool = signS.verify(signed);
        } else {

            signM.initVerify(pk);
            signM.update(wholereg.trim().getBytes());
            bool = signM.verify(signed);
        }

        return bool;
    }

    public static String getMd5byte(String text) throws NoSuchAlgorithmException {


        byte[] hash = MessageDigest.getInstance("MD5").digest(text.getBytes());
        String actual = DatatypeConverter.printHexBinary(hash);
        return actual;

    }

    public static String getMd5(String input) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes());
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }


    public static void CreateKeyPem() throws InterruptedException, IOException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -v -export -file E:\\ideaprojects\\untitled8\\a\\cert.cer -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks");

        } else {
            builder.command("sh", "-c", "openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in private_key.pem -out pkcs8.key\n");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        Thread.sleep(1000);
    }


    public static PrivateKey getPrivateKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

}