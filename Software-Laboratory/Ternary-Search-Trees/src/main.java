import java.io.*;
import java.util.*;

public class main {
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("legitimatetrain.txt"), "UTF-8"));

        BufferedReader ln = new BufferedReader(new InputStreamReader(new FileInputStream("phishingtrain.txt"), "UTF-8"));

        BufferedReader lt = new BufferedReader(new InputStreamReader(new FileInputStream("legitimatetest.txt"), "UTF-8"));

        BufferedReader pt = new BufferedReader(new InputStreamReader(new FileInputStream("phishingtest.txt"), "UTF-8"));


        String strLine;
        TST tst = new TST();
        int FEATURE_SIZE = 5000;																			// define FEATURE_SIZE
        int n = 4;                                                                                   	    // define "n"

        ArrayList<String> legitimate_nGrams = new ArrayList<>();                                            // stores the Ngrams in the legitimatetrain file
        ArrayList<String> phishing_nGrams = new ArrayList<>();												// stores the Ngrams in the phishingtrain file
        ArrayList<String> legitimate_test = new ArrayList<>();												// stores the lines in the legitimatetest file
        ArrayList<String> phishing_test = new ArrayList<>();												// stores the lines in the phishingtest file

        ArrayList<String> lineGram = new ArrayList<>();

        int legTrainSize = 0; 																				// equal number of nGrams in the legitimatetrain
        int phisTrainSize = 0;																				// equal number of nGrams in the phishingtrain file
        
        while ((strLine = br.readLine()) != null) {
            legTrainSize ++;
            lineGram = N_GramGeneration(strLine, n);														// getting nGrams from legitimatetrain file
            legitimate_nGrams.addAll(lineGram);
            lineGram.clear();
        }

        while ((strLine = ln.readLine()) != null) {
            phisTrainSize ++;
            lineGram = N_GramGeneration(strLine, n);														// getting nGrams from phishingtrain file
            phishing_nGrams.addAll(lineGram);
            lineGram.clear();
        }

        int sizeLegTest = 0;
        while ((strLine = lt.readLine()) != null) {
            sizeLegTest ++;
            legitimate_test.add(strLine);																	// adding line from Legitimatetest file
        }


        int sizePhishTest = 0;
        while ((strLine = pt.readLine()) != null) {
            sizePhishTest ++;																				// adding line from phishingtest file
            phishing_test.add(strLine);
        }


        HashMap<String, Integer> LegKeys = new HashMap<>();													// will contain legitimate nGrams to be sorted
        HashMap<String, Integer> PhishKeys = new HashMap<>();												// will contain phishing nGrams to be sorted
        HashMap<String, Float> WeightKeys = new HashMap<>();												// will contain all nGrams weight to be sorted

        ArrayList<String> StrongLegitimate = new ArrayList<>();
        ArrayList<String> StrongPhishing = new ArrayList<>();

        addTST(tst, legitimate_nGrams, "legitimate");														// adding nGrams from legitimate file to tst
        addTST(tst, phishing_nGrams, "phishing");															// adding nGrams from phishing file to tst


        LegKeys = CreateArray(tst, LegKeys, "legitimate");													// contain legitimate nGrams to be sorted
        PhishKeys = CreateArray(tst, PhishKeys, "phishing");												// contain phishing nGrams to be sorted

        StrongLegitimate = sortKeys(LegKeys, FEATURE_SIZE,"legitimate");									// sorting nGrams by freq and generating "strong_legitimate_features.txt" file
        StrongPhishing = sortKeys(PhishKeys, FEATURE_SIZE,"phishing");										// sorting nGrams by freq and generating "strong_phishing_features.txt" file

        CalculateWeight(tst);																				// calculates weight all nGrams
        WeightKeys = CreateWeightArray(tst, WeightKeys);
        sortWeights(WeightKeys);																		    // sorting nGrams by weight and generating "all_feature_weights.txt" file

        StrongLegitimate.addAll(StrongPhishing);
        int insignificant  = DeleteNode(tst, StrongLegitimate);												// deleting insignificant nGrams in tst

        int TstSize = tst.size(tst);
        
        Console_out(legTrainSize, phisTrainSize, sizeLegTest, sizePhishTest, FEATURE_SIZE, insignificant, TstSize);
        CalculateAccuracy(legitimate_test, phishing_test, tst, n);
        
    }


    private static void Console_out(int legTrainSize, int phisTrainSize, int sizeLegTest, int sizePhishTest, int feature_size, int insignificant, int tstSize) {
        System.out.println("n-gram based phishing detection via TST");
        System.out.println("Legitimate training file has been loaded with ["+ legTrainSize + "] instances");
        System.out.println("Legitimate test file has been loaded with [" + sizeLegTest +"] instances");
        System.out.println("Phishing training file has been loaded with ["+ phisTrainSize + "] instances");
        System.out.println("Phishing test file has been loaded with [" + sizePhishTest +"] instances");
        System.out.println("TST has been loaded with " + legTrainSize + " n-grams");
        System.out.println("TST has been loaded with " + phisTrainSize + " n-grams");
        System.out.println(feature_size +" strong phishing n-grams have been saved to the file\"strong_phishing_features.txt\"");
        System.out.println(feature_size +" strong legitimate n-grams have been saved to the file\"strong_legitmate_features.txt\"");
        System.out.println(tstSize +" n-grams + weights have been saved to the file \"all_feature_weights.txt\"");
        System.out.println(insignificant + " insignificant n-grams have been removed from the TST");
    }

    private static void CalculateAccuracy(ArrayList<String> legitimate, ArrayList<String> phishing, TST tst, int n ) {			// increases metrics based on total_score of urls in test files.
        ArrayList<String> lineGram = new ArrayList<>();
        float totalScore;
        
        int TP = 0, FN = 0, UP = 0;
        for (int i = 0; i < phishing.size(); i++){
            String line = phishing.get(i);
            lineGram = N_GramGeneration(line, n);										// generating url's nGrams from phishingtest file
            totalScore = totalScore(tst, lineGram);										// calculating url's total_score
            if (totalScore > 0.0f) TP ++;
            else if ( totalScore < 0.0f) FN ++;
            else if ( totalScore == 0.0f)  UP ++;
        }

        int TN = 0, FP = 0, UL = 0;
        for (int i = 0; i < legitimate.size(); i++){
            String line = legitimate.get(i);
            lineGram = N_GramGeneration(line, n);										// generating url's nGrams from legitimatetest file
            totalScore = totalScore(tst, lineGram);										// calculating url's total_score
            if (totalScore > 0.0f) FP ++;
            else if ( totalScore < 0.0f) TN ++;
            else if ( totalScore == 0.0f)  UL ++;
        }

        System.out.println("TP:" + TP + " FN:" + FN + " TN:" + TN + " FP:" + FP + " Unpredictable Phishing:" + UP + " Unpredictable Legitimate:" + UL);
        float Accuracy = (float)(TP + TN) / (TP + TN + FP + FN + UP + UL);			 	// calculating accuracy value
        System.out.println("Accuracy: " + Accuracy);
    }

    private static float totalScore(TST tst, ArrayList<String> test) {
        float totalScore = 0.0f;
        for(int i = 0; i < test.size(); i ++){
            String n_gram = test.get(i);
            if(!tst.contains(n_gram)){
                test.remove(n_gram);
            }
            else if(tst.contains(n_gram)){												// if nGram is present in tst
                totalScore = totalScore + tst.get(n_gram);								// the weight of nGram is added to total_score
            }
        }
        return totalScore;
    }

    private static int DeleteNode(TST tst, ArrayList<String> strongs) {
        int j = 0;
        for (String key : tst.keys()) {
            if(!strongs.contains(key)){													// delete nGram in tst, if nGram is not significant
                j ++;
                tst.delete(key);
            }
        }
        return j;
    }

    private static void CalculateWeight(TST tst) {
        for (String key : tst.keys()) {
            TST.Node node = tst.getNode(key);											// visiting all nGrams in tst
            Float weight = Weight(node);												// and calculate weight
            node.setWeight(weight);														// assigning calculated weight value to nGram's weight
        }
    }

    private static Float Weight(TST.Node node) {										// calculating weights value
        Float PO = Float.valueOf(node.getPhish());
        Float LO = Float.valueOf(node.getLegitimate());
        Float weight = 0.0f;
        if (PO>0 && LO==0){
            weight = 1.0f;
        }
        else if (PO ==0 && LO>0){
            weight = -1.0f;
        }
        else if (PO >0 && LO>0) {
            if (PO>LO){
                weight = min(PO,LO)/max(PO,LO);
            }
            else if(PO<LO){
                weight = -min(PO,LO)/max(PO,LO) ;
            }
            else{
                weight = 0.0f;
            }
        }
        return weight;
    }

    private static Float max(Float po, Float lo) {										// finding max value
        if (po>= lo) return po;
        else return lo;
    }

    private static Float min(Float po, Float lo) {										// finding min value
        if(po <= lo) return po;
        else return lo;
    }

    private static ArrayList<String> sortKeys(HashMap<String, Integer> Keys, int FEATURE_SIZE, String type) throws FileNotFoundException, UnsupportedEncodingException {
        ArrayList<String> Strongs = new ArrayList<>();
        
        //sorting hashmap
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        Keys.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        PrintWriter writer = null;
        //strong files are created and printed
        if(type.equals("legitimate")){
             writer = new PrintWriter("strong_legitimate_features.txt", "UTF-8");
             writer.println("Most important legitimate n_grams");
        }
        else if (type.equals("phishing")){
             writer = new PrintWriter("strong_phishing_features.txt", "UTF-8");
             writer.println("Most important phishing n_grams");
        }
        int i = 1;
        for (Map.Entry<String, Integer> en : reverseSortedMap.entrySet()) {
            if(i <= FEATURE_SIZE ){
                Strongs.add(en.getKey());
                writer.write(i++ + ". "  + en.getKey() + " - freq: " + en.getValue()+ "\n");
            }
        }
        writer.close();
        return Strongs;
    }

    private static void sortWeights(HashMap<String, Float> Keys) throws FileNotFoundException, UnsupportedEncodingException {
        LinkedHashMap<String, Float> reverseSortedMap = new LinkedHashMap<>();
        //sorting hashmap
        Keys.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        //all_feature_weights file are created and printed
        PrintWriter writer = new PrintWriter("all_feature_weights.txt", "UTF-8");
        writer.println("All N-Gram Weights");
        for (Map.Entry<String, Float> en : reverseSortedMap.entrySet()) {
                writer.write(en.getKey() + " - weight: " + en.getValue()+ "\n");
        }
        writer.close();
        return;
    }

    private static HashMap<String, Integer> CreateArray(TST tst, HashMap<String, Integer> Keys, String type) {
        for (String key : tst.keys()) {
        	//creating hashmap with the name and frequency values of nGrams
            if(type.equals("legitimate")){
                Keys.put(key, tst.getLeg(key));
            }
            else if(type.equals("phishing")){
                Keys.put(key, tst.getPhish(key));
            }
        }
        return Keys;
    }

    private static HashMap<String, Float> CreateWeightArray(TST tst, HashMap<String, Float> Keys) {
    	// creating hashmap with the name and weight values of nGrams
        for (String key : tst.keys()) {
            Keys.put(key, tst.get(key));
        }
        return Keys;
    }

    private static void addTST(TST tst, ArrayList<String> keys, String type) {
        for(int i = 0; i < keys.size(); i++){
            String key = keys.get(i);
            if(NodeControl(tst, key, type)){										//if nGram has not been added to tst before, it is added.
                tst.put(key, 1, type);
            }
        }
    }

    private static boolean NodeControl(TST tst, String key, String type) {			// checking nGram is added to tst
        if(tst.getNode(key) == null){
            return true;
        }
        else{
            increaseFrequency(tst,key, type);										// if added before, increasing legitimate or phishing frequency
            return false;
        }
    }

    private static void increaseFrequency(TST tst, String key, String type) {		//increases frequency according to which file it comes from
        TST.Node node = tst.getNode(key);
        if(type.equals("legitimate")){
            int leg = node.getLegitimate();
            node.setLegitimate( leg + 1);
        }
        else if (type.equals("phishing")){
            int phish = node.getPhish();
            node.setPhish( phish + 1);
        }
    }

    private static ArrayList<String> N_GramGeneration(String strLine, int n) {
        ArrayList<String> n_Grams = new ArrayList<>();
        if(strLine.contains("https") || strLine.contains("http") || strLine.contains("www")){
            strLine = strLine.replace("https","");
            strLine = strLine.replace("http","");
            strLine = strLine.replace("www","");
            strLine = lowercase(strLine);
        }
        strLine = lowercase(strLine);

        for(int i = 0; i < (strLine.length() - (n-1)); i ++){						// creating nGrams based on "n" value
            String substring = strLine.substring(i, i + n);
            n_Grams.add(substring);
        }
        return n_Grams;
    }

    private static String lowercase(String strLine) {
        int i ;
        for(i = 0; i < strLine.length(); i ++){
            char x = strLine.charAt(i);
            if(Character.isLetter(x)){
                char y = Character.toLowerCase(x);
                strLine = strLine.replace(x, y);
            }
        }
        return strLine;
    }
}