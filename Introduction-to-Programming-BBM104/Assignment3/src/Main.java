


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Main { 
	public static void main(String[] args) throws IOException {
		JSONParser parser = new JSONParser();
		ArrayList<String> chanceList = new ArrayList<String>();
        ArrayList<String> chestList = new ArrayList<String>();   
    	Square square_list[] = new Square[40];
        Banker banker_list[] = new Banker[1];
        Player player_list[] = new Player[2];
        try {
        	System.setOut(new PrintStream("output.txt"));
            FileWriter writer = new FileWriter("output.txt");
            Object obj = parser.parse(new FileReader("property.json"));
            JSONObject jsonObject = (JSONObject) obj;   
            //System.out.println(jsonObject);           
   		 	JSONArray land_1 = (JSONArray) jsonObject.get("1");
   		 	JSONArray road_1 = (JSONArray) jsonObject.get("2");
   		 	JSONArray comp_1 = (JSONArray) jsonObject.get("3");

            //System.out.println(land_1);
            land_1.forEach( land -> parseLand( (JSONObject) land, square_list) );
            road_1.forEach( road -> parseRoad( (JSONObject) road, square_list ) );
            comp_1.forEach( comp -> parseComp( (JSONObject) comp, square_list ) );
            Square square1 = new Square (0, "GO", 1, "GO", "empty");
            seyma.addSquare(square1, square_list);
            int[] com_chest = {3, 18, 34};
            int[] chance = {8, 23, 37};
            for( int i = 0; i < 3; i++) {
            	Square square2 = new Square (0, "Community Chest", com_chest[i], "Community Chest", "empty");
            	Square square3 = new Square (0, "Chance",chance[i], "Chance", "empty");
                seyma.addSquare(square2, square_list);
                seyma.addSquare(square3, square_list);
            }
            Square square = new Square (0, "Free Parking", 21, "Free Parking", "empty");
            seyma.addSquare(square, square_list);
            Square square3 = new Square (0, "Jail",11, "Jail", "empty");
            seyma.addSquare(square3, square_list);
            Square square2 = new Square (0, "Go to Jail", 31, "Go to Jail", "empty");
            seyma.addSquare(square2, square_list);
            Square square4 = new Square (0, "Income Tax", 5, "Income Tax", "empty");
            seyma.addSquare(square4, square_list);
            Square square5 = new Square (0, "Super Tax", 39, "Super Tax", "empty");
            seyma.addSquare(square5, square_list);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONParser parser1 = new JSONParser();

        try {

            Object obj = parser1.parse(new FileReader("list.json"));
            JSONObject jsonObject = (JSONObject) obj;
           
            JSONArray chance = (JSONArray) jsonObject.get("chanceList");
            JSONArray chest = (JSONArray) jsonObject.get("communityChestList");
            Iterator<JSONObject> iterator = chance.iterator();
            Iterator<JSONObject> iterator1 = chest.iterator();    
            while (iterator.hasNext()) {
            	String item2 = iterator.next().toString();
                String item1 = item2.split(":")[1];
                String item = item1.split("}")[0];
                chanceList.add(item);
            }
            while (iterator1.hasNext()) {
            	String item4 = iterator1.next().toString();
                String item3 = item4.split(":")[1];
                String item5 = item3.split("}")[0];
                chestList.add(item5);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0 ; i< chanceList.size(); i ++) {
        	//System.out.println(chanceList.get(i));
        }
        for (int i = 0 ; i< chestList.size(); i ++) {
        	//System.out.println(chestList.get(i));
        }
        
       String[] a = {"Player 1" , "Player 2"};
       People banker1 = new Banker("Banker",100000);
       seyma.addBanker(banker1, banker_list);
 	   for (int i = 0; i < 2; i++) {
    	   ArrayList<String> owned = new ArrayList<String>();
    	   ArrayList<String> owned_kind = new ArrayList<String>();
 		   String name = a[i];
 		   People player1 = new Player(name, 15000, 0, 1, "GO", owned, owned_kind,0);
 		   seyma.addPlayer(player1, player_list);
 	   }
        try (
        	   
        	   FileReader reader = new FileReader(args[0]);
               BufferedReader br = new BufferedReader(reader)) {
               String line;
               while (((line = br.readLine()) != null)) {
            	   //System.out.println(line);
            	   if(! line.equals("show()")){
                   String player_id = line.split(";")[0];
                   int dice = Integer.parseInt(line.split(";")[1]);
                   seyma.game(player_id, dice, square_list, player_list, banker_list, chestList, chanceList);
            	   }
            	   else if(line.equals("show()")) {
            		   seyma.show(square_list, player_list, banker_list);
            	   }
               }
    		   seyma.show(square_list, player_list, banker_list);
           } catch (IOException e) {
               System.err.format("IOException: %s%n", e);
           }
        
	}
	static Setup seyma = new Setup();

	private static void parseLand(JSONObject land, Square square_list[]){
        String cost1 = (String) land.get("cost");
        int cost = Integer.parseInt(cost1);

        String name = (String) land.get("name");   
        
        String id1 = (String) land.get("id"); 
        int id = Integer.parseInt(id1);
        
        Square square1 = new Square (cost, name, id, "land", "empty");
        seyma.addSquare(square1, square_list);
    }
	private static void parseRoad(JSONObject road, Square square_list[]){
        String cost1 = (String) road.get("cost");
        int cost = Integer.parseInt(cost1);

        String name = (String) road.get("name");   
        
        String id1 = (String) road.get("id"); 
        int id = Integer.parseInt(id1);
        
        Square square1 = new Square (cost, name, id, "railroad", "empty");
        seyma.addSquare(square1, square_list);
    }
	private static void parseComp(JSONObject comp, Square square_list[]){
        String cost1 = (String) comp.get("cost");
        int cost = Integer.parseInt(cost1);

        String name = (String) comp.get("name");   
        
        String id1 = (String) comp.get("id"); 
        int id = Integer.parseInt(id1);
        Square square1 = new Square (cost, name, id, "company", "empty");
        seyma.addSquare(square1, square_list);
    }	
} 


