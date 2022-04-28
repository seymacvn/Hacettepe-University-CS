

import java.util.ArrayList;

public class Player extends People {
	private int dice;
	private int new_pos;
	private String location;
	private ArrayList owned;
	private ArrayList owned_kind;
	private int counter;

	public Player(String name, int money, int dice,int new_pos, String location, ArrayList owned, ArrayList owned_kind, int counter) {
		super(name, money);
		this.dice = dice;
		this.new_pos = new_pos;
		this.location = location;
		this.owned = owned;
		this.owned_kind = owned_kind;
		this.counter = counter;
	}
	public int getDice() {
		return dice;
	}
	public void setDice(int dice) {
		this.dice = dice;
	}
	public int getNew_pos() {
		return new_pos;
	}
	public void setNew_pos(int new_pos) {
		this.new_pos = new_pos;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ArrayList getOwned() {
		return owned;
	}	
	public void setOwned(ArrayList owned) {
		this.owned = owned;
	}
	public ArrayList getOwned_kind() {
		return owned_kind;
	}
	public void setOwned_kind(ArrayList owned_kind) {
		this.owned_kind = owned_kind;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
}
