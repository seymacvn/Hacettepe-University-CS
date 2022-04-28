import java.util.ArrayList;
import java.util.Base64;

public class Feistel {

    public String Left_Circular_Shift(String key, int number){
        return key.substring(number) + key.substring(0, number);
    }

    public ArrayList<String> Subkey_Generation(String key){

        ArrayList<String> keys = new ArrayList<String>();
        byte[] decodedBytes = Base64.getUrlDecoder().decode(key);
        key = new String(decodedBytes);
        if(key.length() == 96){
            for(int i= 0; i < 10 ; i ++){
                String new_shifted = Left_Circular_Shift(key, i+1);
                String key_i_th = "";
                if(i % 2 == 0){
                    for (int j = 0; j < 96 ; j = j + 2) {
                        key_i_th += new_shifted.charAt(j);
                    }
                }
                else{
                    for (int j = 1; j < 96 ; j = j + 2) {
                        key_i_th += new_shifted.charAt(j);
                    }
                }

                keys.add(key_i_th);
            }
        }
        return keys;
    }

    public String XOR(String round_i, String key_i){
        String result = "";
        for (int i = 0; i < round_i.length(); i++) {
            result += round_i.charAt(i)^key_i.charAt(i);
        }
        return result;
    }

    private int find_index(String[] row, String element) {
        int index = -1;

        int i = 0;
        while(i < row.length) {
            if(row[i].equals(element)) {
                index = i;
            }
            i++;
        }
        return index;
    }

    public String DES_boxes(String divided){
        String outer = "", middle = "";
        outer += divided.charAt(0);
        outer += divided.charAt(5);
        middle = divided.substring(1,5);

        String [] row = {"00", "01", "10", "11"};
        String [] column = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String[][] arr = {{"0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", "0101", "0011", "1111", "1101", "0000", "1110", "1001"},
                {"1110", "1011", "0010", "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", "1001", "1000", "0110"},
                {"0100", "0010", "0001", "1011", "1010", "1101", "0111", "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110"},
                {"1011", "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", "1111", "0000", "1001", "1010", "0100", "0101", "0011"}};
        int row_index = find_index(row, outer);
        int column_index = find_index(column, middle);

        return arr[row_index][column_index];
    }

    public String scramble_function(String round_i, String key_i){
        String result = "";
        String new_plaintext = XOR(round_i, key_i);
        new_plaintext += XOR(new_plaintext.substring(0,6), new_plaintext.substring(6,12));
        new_plaintext += XOR(new_plaintext.substring(12,18), new_plaintext.substring(18,24));
        new_plaintext += XOR(new_plaintext.substring(24,30), new_plaintext.substring(30,36));
        new_plaintext += XOR(new_plaintext.substring(36,42), new_plaintext.substring(42));
        for(int i = 0; i < 72; i = i + 6){
            result += DES_boxes(new_plaintext.substring(i, i+ 6));
        }
        String dummy = "";
        for(int i=0; i <= result.length()-2; i=i+2){
            dummy += result.charAt(i+1);
            dummy += result.charAt(i);
        }
        result = dummy;
        return result;
    }

    public ArrayList<String> input_generator(String input,boolean isEnc){

        while(input.length()%96!=0){
            input+="0";
        }
        ArrayList<String> inputs = new ArrayList<>();
        for(int i=0;i<input.length();i+=96){
            String dummy = input.substring(i,i+96);
            inputs.add(dummy);
        }
        return inputs;
    }

    public String feistel_EBC(String input,String key,boolean isEnc){

        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        //Encription
        if(isEnc){
            for (String plaintext:inputs) {
                String l ="";
                String r ="";
                String l0=plaintext.substring(0,(plaintext.length()/2));
                String r0=plaintext.substring((plaintext.length()/2));
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }
        }

        else{
            for (String ciphertext:inputs){
                String l = "";
                String r = "";
                String l0 = ciphertext.substring(0,(ciphertext.length()/2));
                String r0 = ciphertext.substring((ciphertext.length()/2));
                for(int i=10;i>=1;i--){
                    l = XOR(r0,scramble_function(l0,keys.get(i-1)));
                    r = l0;
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }

        }

        for (String out:func_output) {
            output += out;
        }
        return output;
    }

    public String feistel_CBC(String input,String key,boolean isEnc){
        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        String iv="";
        for (int i=0;i<96;i++) iv+="1";
        func_output.add(iv);

        if(isEnc){
            int counter=0;
            for (String plaintext:inputs) {
                plaintext = XOR(func_output.get(counter),plaintext);
                String l ="";
                String r ="";
                String l0=plaintext.substring(0,(plaintext.length()/2));
                String r0=plaintext.substring((plaintext.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }
        }
        //Decription
        else{
            ArrayList<String> ciphers = new ArrayList<String>();
            ciphers.add(iv);
            int counter=0;
            for (String ciphertext:inputs){
                String l = "";
                String r = "";
                String l0 = ciphertext.substring(0,(ciphertext.length()/2));
                String r0 = ciphertext.substring((ciphertext.length()/2));
                ciphers.add(ciphertext);
                for(int i=10;i>=1;i--){
                    l = XOR(r0,scramble_function(l0,keys.get(i-1)));
                    r = l0;
                    r0 = r;
                    l0 = l;
                }
                func_output.add(XOR(ciphers.get(counter),(l+r)));
                counter++;
            }
        }
        func_output.remove(0);
        for (String out:func_output) {
            output+=out;
        }
        return output;
    }


    public String feistel_OFB(String input,String key,boolean isEnc){
        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        ArrayList<String> func_input = new ArrayList<String>();
        String iv="";
        for (int i=0;i<96;i++) iv+="1";
        func_input.add(iv);

        if(isEnc){
            int counter=0;
            for (String plaintext:inputs) {
                input = func_input.get(counter);
                String l ="";
                String r ="";
                String l0=input.substring(0,(input.length()/2));
                String r0=input.substring((input.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                String feistel_output = l+r;
                func_input.add(feistel_output);
                func_output.add(XOR(feistel_output,plaintext));
            }
        }
        //Decription
        else{
            int counter=0;
            for (String plaintext:inputs) {
                input = func_input.get(counter);
                String l ="";
                String r ="";
                String l0=input.substring(0,(input.length()/2));
                String r0=input.substring((input.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                String feistel_output = l+r;
                func_input.add(feistel_output);
                func_output.add(XOR(feistel_output,plaintext));
            }
        }
        for (String out:func_output) {
            output+=out;
        }
        return output;
    }


}