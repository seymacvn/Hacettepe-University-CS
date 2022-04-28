import java.security.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

class Cryptor {
    public SecretKey aesKey;
    public SecretKey desKey;
    public byte[] desIV;
    public byte[] aesIV;


    public Cryptor() throws NoSuchAlgorithmException {
        this.aesKey =getAESKey();
        this.desKey =getDESKey();
        this.desIV=getRandomIV(8);
        this.aesIV=getRandomIV(16);
    }

    public static byte[] getRandomIV(int ivSize) {
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    public static SecretKey getAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }

    public static SecretKey getDESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56, SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }

    public static String encrypting_message(String message, String method, String mode, byte[] IV, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        String chpr = method + "/" + mode + "/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(chpr);
        IvParameterSpec ivParams = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, method), ivParams);
        byte[] encrypted = cipher.doFinal(message.getBytes());

        byte[] encryptedIVAndText = new byte[IV.length + encrypted.length];
        System.arraycopy(IV, 0, encryptedIVAndText, 0, IV.length);
        System.arraycopy(encrypted, 0, encryptedIVAndText, IV.length, encrypted.length);
        String encoded = Base64.getEncoder().encodeToString(encryptedIVAndText);
        return encoded;
    }
}
