import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
public class GUI extends JFrame {
	private final int WINDOW_WIDTH;
	private final int WINDOW_HEIGHT;
	private final int GRID_SIZE;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private JButton[][] grid;
	private JButton selectedButton;
	private Color previous;
	private JLabel display;
	private JButton next;
	private JButton run;
	private JButton reset;
	private JButton start;
	private JButton startGrid;
	private boolean startFlag;
	private JButton goal;
	private JButton goalGrid;
	private boolean goalFlag;

	public GUI(){
		super("A*");
		GRID_SIZE = 15;
		WINDOW_WIDTH = 500;
		WINDOW_HEIGHT = 500;
		selectedButton = new JButton();
		previous = new JButton().getBackground();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		display = new JLabel("Start node not set.");
		next = new JButton("Next");
		run = new JButton("Run");
		reset = new JButton("Reset");
		start = new JButton("Set Start");
		startGrid = new JButton();
		startFlag = false;
		goal = new JButton("Set Goal");
		goalGrid = new JButton();
		goalFlag = false;
		this.buildGUI();
	}
	public GUI (int gridSize)
	{
		super("A*");
		GRID_SIZE = gridSize;
		WINDOW_WIDTH = 500;
		WINDOW_HEIGHT = 500;
		previous = new JButton().getBackground();
		previous = null;
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		display = new JLabel("Start node not set.");
		next = new JButton("Next");
		run = new JButton("Run");
		reset = new JButton("Reset");
		start = new JButton("Set Start");
		startGrid = new JButton();
		startFlag = false;
		goal = new JButton("Set Goal");
		goalGrid = new JButton();
		goalFlag = false;
		this.buildGUI();
	}
	
	//getters setters
	public int getGridSize()
	{
		return this.GRID_SIZE;
	}
	public JButton[][] getGrid()
	{
		return this.grid;
	}
	public JButton getReset()
	{
		return this.reset;
	}
	public JButton getNext()
	{
		return this.next;
	}
	public JButton getRun()
	{
		return this.run;
	}
	public JLabel getDisplay()
	{
		return this.display;
	}
	public JButton getSelected()
	{
		return this.selectedButton;
	}
	public void setSelected(JButton selectedButton)
	{
		this.selectedButton = selectedButton;
	}
	public Color getPrevious()
	{
		return this.previous;
	}
	public void setPrevious(Color previous)
	{
		this.previous = previous;
	}
	public JButton getStartGrid() {
		return startGrid;
	}
	public void setStartGrid(JButton startGrid) {
		this.startGrid = startGrid;
	}
	public boolean getStartFlag() {
		return startFlag;
	}
	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}
	public JButton getGoalGrid() {
		return goalGrid;
	}
	public void setGoalGrid(JButton goalGrid) {
		this.goalGrid = goalGrid;
	}
	public boolean getGoalFlag() {
		return goalFlag;
	}
	public void setGoalFlag(boolean goalFlag) {
		this.goalFlag = goalFlag;
	}

	//builds the JFrame
	private void buildGUI()
	{
		grid = new JButton[this.GRID_SIZE][this.GRID_SIZE];
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(gbl);
		this.setSize(this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
		this.display.setOpaque(true);
		this.buildButtons();
		this.setVisible(true);
	}
	//builds the JButtons and sets them in the JFrame
	private void buildButtons()
	{
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = .5;
		for(int i = 0; i < grid.length; i++)
		{
			gbc.ipady = this.WINDOW_HEIGHT/(this.GRID_SIZE * 3);
			for(int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = new JButton();
				grid[i][j].setOpaque(true);
				grid[i][j].setBorder(LineBorder.createBlackLineBorder());
				grid[i][j].setText(i*this.GRID_SIZE+j+"");
				gbc.gridx = i;
				gbc.gridy = j+1;
				this.add(grid[i][j], gbc);
			}
		}
		
		gbc.weighty = .5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.ipady = this.WINDOW_HEIGHT/(this.GRID_SIZE + 2);
		gbc.gridwidth = this.WINDOW_WIDTH;
		gbc.insets = new Insets(0,10,0,0);
		this.add(display, gbc);

		gbc.gridy = this.GRID_SIZE+1;
		gbc.gridwidth = this.GRID_SIZE/5;
		gbc.anchor = GridBagConstraints.PAGE_END;
		start.setBackground(Color.MAGENTA);
		start.setOpaque(true);
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!startFlag)
					startFlag = !startFlag;
			}
		});
		this.add(start, gbc);

		gbc.gridx = this.GRID_SIZE/5;
		goal.setBackground(Color.CYAN);
		goal.setOpaque(true);
		goal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!goalFlag)
					goalFlag = !goalFlag;
			}
		});
		this.add(goal, gbc);

		gbc.gridx = this.GRID_SIZE*2/5;
		this.add(next, gbc);
		
		gbc.gridx = this.GRID_SIZE*3/5;
		this.add(run, gbc);

		gbc.gridx = this.GRID_SIZE*4/5;
		this.add(reset, gbc);
	}
}