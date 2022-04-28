import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class BBMcrypt {

    public static void main(String[] args) throws IOException {

        Feistel feistel = new Feistel();
        boolean isEnc = args[0].equals("enc"); // true for encryption false for decryption
        String keyPath="";
        String inputPath="";
        String outputPath="";
        String mode="";
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-K":
                    keyPath = args[i + 1];
                    break;
                case "-I":
                    inputPath = args[i + 1];
                    break;
                case "-O":
                    outputPath = args[i + 1];
                    break;
                case "-M":
                    mode = args[i + 1];
                    break;
            }        
	}
	
	System.setOut(new PrintStream(outputPath));                                                          // creates output file
        FileWriter writer = new FileWriter(outputPath);

        File inputfile = new File(inputPath);
        File keyfile = new File(keyPath);
        BufferedReader key_br = new BufferedReader(new FileReader(keyfile));
        BufferedReader input_br = new BufferedReader(new FileReader(inputfile));
	String input, key;
        
	try {
            input = input_br.readLine();
            key = key_br.readLine();
		
            switch (mode) {
                case "ECB":
                    System.out.print(feistel.feistel_EBC(input, key, isEnc));
                    break;
                case "CBC":
                    System.out.print(feistel.feistel_CBC(input, key, isEnc));
                    break;
                case "OFB":
                    System.out.print(feistel.feistel_OFB(input, key, isEnc));
                    break;
            }

        }


        catch (IOException ex) {
            // Handle exception
            System.out.println("error!");
        }
    }


}