package model;

public class Transport {
	private int id;
	private String tip;
	public Transport(int id, String tip) {
		super();
		this.id = id;
		this.tip = tip;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	
}
