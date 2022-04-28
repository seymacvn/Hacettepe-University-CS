
public class Square {
	int cost;
	String name;
	int id;
	String kind;
	String status;
	public Square(int cost, String name, int id, String kind, 	String status) {
		super();
		this.cost = cost;
		this.name = name;
		this.id = id;
		this.kind = kind;
		this.status = status;

	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
