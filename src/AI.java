import java.util.ArrayList;
import java.util.PriorityQueue;
public class AI 
{
	private Node[][] map;
	private final int GRID_SIZE;
	private final double UNPATHABLE_PERCENTAGE;
	private Node start;
	private Node current;
	private Node goal;
	private boolean finished;
	private PriorityQueue<Node> openList;
	private ArrayList<Node> closedList;
	private String message;
	
	public AI()
	{
		this.GRID_SIZE = 15;
		this.UNPATHABLE_PERCENTAGE = .1;
		map = new Node[this.GRID_SIZE][this.GRID_SIZE];
		this.resetMap();
	}
	public AI(int gridSize)
	{
		this.GRID_SIZE = gridSize;
		this.UNPATHABLE_PERCENTAGE = .1;
		map = new Node[this.GRID_SIZE][this.GRID_SIZE];
		this.resetMap();
	}
	public AI(double unpathable)
	{
		this.GRID_SIZE = 15;
		this.UNPATHABLE_PERCENTAGE = unpathable;
		map = new Node[this.GRID_SIZE][this.GRID_SIZE];
		this.resetMap();
	}
	public AI(int gridSize, double unpathable)
	{
		this.GRID_SIZE = gridSize;
		this.UNPATHABLE_PERCENTAGE = unpathable;
		map = new Node[this.GRID_SIZE][this.GRID_SIZE];
		this.resetMap();
	}
	public void resetMap()
	{
		closedList = new ArrayList<Node>();
		openList = new PriorityQueue<Node>();
		finished = false;
		start = new Node();
		current = new Node();
		goal = new Node();
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
			{
				map[i][j] = new Node(i, j, Math.random() < this.UNPATHABLE_PERCENTAGE ? 1 : 0);
			}
		this.setMessage();
	}
	public void next()
	{
		if(this.openList.isEmpty())
		{
			this.setMessage();
			return;
		}
		this.pop();
		if(!this.isGoal(this.current))
		{
			this.getNeighbors();
			this.close(this.current);
		}else
		{
			this.goal.setParent(this.current);
			this.finished = true;
		}
		this.setMessage();
	}
	private void pop()
	{
		if(!this.openList.isEmpty())
			this.current = this.openList.poll();
	}
	private boolean isGoal(Node check)
	{
		return this.goal.equals(check);
	}
	private void getNeighbors()
	{
		int x = this.current.getRow(); //initializing local variables for readability
		int y = this.current.getCol();
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; j < 2; j++)
			{
				if(x+i > -1 && x+i < this.GRID_SIZE && //checks if x is in the grid
						y+j > -1 && y+j < this.GRID_SIZE && //checks if y is in the grid
						!(i == 0 && j ==0) //checks if x & y is the current node
						)
					this.canAdd(this.map[x+i][y+j]);
			}
		}
	}
	private void close(Node check)
	{
		this.closedList.add(check);
	}
	private void canAdd(Node check)
	{
		if(this.closedList.contains(check))
		{
			return;
		}
		if(check.getType() != 1)
		{
			Node temp = new Node(check);
			temp.setParent(this.current);
			temp = this.calc(temp);
			if(!this.openList.contains(check))
			{
				check.setParent(this.current);
				check = this.calc(check);
				this.openList.add(check);
			}else
			{
				Node[] tempArr = this.openList.toArray(new Node[0]);
				for(int i = 0; i < tempArr.length; i++)
				{
					if(tempArr[i].equals(temp) && tempArr[i].compareTo(temp) > 0)
					{
						check.setParent(this.current);
						check = this.calc(check);
						this.openList.remove(tempArr[i]);
						this.openList.add(check);
						return;
					}
				}
			}
		}
	}
	/*
	 *  calculates Using Manhattan Method
	 * F = G + H
	 * G = √((current row - parent col)^2 + (current col - parent col)^2) + parent G 
	 * H = ABS(current row - goal row) + ABS(current col - goal row)
	 */
	private Node calc(Node calc)
	{
		double xsqrd = Math.pow((double)(calc.getCol()-calc.getParent().getCol()) * 10,2);
		double ysqrd = Math.pow((double)(calc.getRow()-calc.getParent().getRow()) * 10,2);
		calc.setG((int) Math.sqrt(xsqrd + ysqrd) + calc.getParent().getG());
		calc.setH((Math.abs(calc.getCol()-goal.getCol())+Math.abs(calc.getRow()-goal.getRow())) * 10);
		calc.setF();
		return calc;
	}
	public Node[][] getMap(){
		return this.map;
	}
	public void setGoal(Node goal)
	{
		this.softReset();
		this.goal = goal;
		if(this.start.isSet())
		{
			start.setG(0);
			start.setH((Math.abs(start.getCol()-goal.getCol())+Math.abs(start.getRow()-goal.getRow())) * 10);
			start.setF();
		}
		this.setMessage();
	}
	public void setStart(Node start)
	{
		this.softReset();
		this.start = start;
		if(this.goal.isSet())
		{
			this.start.setG(0);
			this.start.setH((Math.abs(start.getCol()-goal.getCol())+Math.abs(start.getRow()-goal.getRow())) * 10);
			this.start.setF();
		}
		this.openList.add(this.start);
		this.setMessage();
	}
	private void softReset()
	{
		this.openList.removeAll(this.openList);
		this.closedList.removeAll(this.closedList);
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[i].length; j++)
				map[i][j] = new Node(i, j, map[i][j].getType());

		if(this.start.isSet())
		{
			map[this.start.getRow()][this.start.getCol()] = this.start;
			this.openList.add(this.start);
		}
		if(this.goal.isSet())
			map[this.goal.getRow()][this.goal.getCol()] = this.goal;
	}
	public Node getGoal()
	{
		return this.goal;
	}
	public Node getStart()
	{
		return this.start;
	}
	public PriorityQueue<Node> getOpenList()
	{
		return this.openList;
	}
	public boolean hasNext()
	{
		return !(this.finished || !this.start.isSet() || !this.goal.isSet() || this.openList.isEmpty());
	}
	public ArrayList<Node> getClosedList()
	{
		return this.closedList;
	}
	public String getMessage()
	{
		return this.message;
	}
	public boolean isFinished()
	{
		return this.finished;
	}
	public Node[] getPath()
	{
		Node[] temp = new Node[this.closedList.size()+1];
		Node[] result = null;
		int i = 0;
		if(this.finished)
		{
			this.current = this.goal;
			while(!this.current.getParent().equals(this.start))
			{
				temp[i++] = this.current.getParent();
				this.current = this.current.getParent();
			}
			temp[i++] = this.current.getParent();
			result = new Node[i--];
			for(int k = 0; i > -1; i--, k++)
				result[k] = temp[i];
			
		}
		return result;
	}
	private void setMessage()
	{
		if(this.finished)
			this.message = "Goal found!";
		else if(!this.start.isSet())
			this.message = "Start node not set.";
		else if(!this.goal.isSet())
			this.message = "Goal node not set.";
		else if(this.openList.isEmpty())
			this.message = "No path found.";
	}
	public String toString(){
		String result = "";
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				result += map[i][j] + "\n";
			}
		}
		return result;
	}
}
