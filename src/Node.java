public class Node implements Comparable<Node>{
	
	private int row, col, f, g, h, type;
	private Node parent;

	public Node(){
		row = -1;
		col = -1;
		type = 0;
		f = -1;
		g = -1;
		h = -1;
		parent = null;
		//type 0 is traverseable, 1 is not
	}
	/**
	 * 
	 * @param r
	 * @param c
	 * @param t type 0 is traverseable, 1 is not
	 */
	public Node(int r, int c, int t){
		row = r;
		col = c;
		type = t;
		f = -1;
		g = -1;
		h = -1;
		parent = null;
		//type 0 is traverseable, 1 is not
	}
	public Node(Node other){
		this.row = other.row;
		this.col = other.col;
		this.f = other.f;
		this.g = other.g;
		this.h = other.h;
		this.type = other.type;
		this.parent = other.parent;
	}
	
	//mutator methods to set values
	public void setF(){
		f = g + h;
	}
	public void setG(int value){
		g = value;
	}
	public void setH(int value){
		h = value;
	}
	public void setType(int value){
		type = value;
	}
	public void setParent(Node n){
		parent = n;
	}
	
	//accessor methods to get values
	public int getF(){
		return f;
	}
	public int getG(){
		return g;
	}
	public int getH(){
		return h;
	}
	public Node getParent(){
		return parent;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public int getType(){
		return type;
	}
	
	
	public boolean isSet()
	{
		return this.row != -1 && this.col != -1;
	}
	public boolean equals(Node in)
	{
		return row == in.getRow() && col == in.getCol();
	}
	public int compareTo(Node other)
	{
		return this.f - other.f;
	}
	public String toString(){
		String result = "";
		result = "X: " + this.row + " Y: " + this.col 
				+ " F: " + this.f + " G: " + this.g + " H: " + this.h
				+ " Type: " + this.type;
		if(this.parent != null)
			result += " Parent: " + this.parent.row + ", " + this.parent.col;
		return result;
	}
	
}
