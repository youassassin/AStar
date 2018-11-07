import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
public class Driver {
	public static void main(String[ ] args) {
		
		double unpathablePercentage = .4; //change this value to set the amount of unpathable nodes must be between 0-1
		GUI run = new GUI();
		JFrame tutorial = new JFrame("Information");
		JLabel text = new JLabel("<html><p>This is a grid of selectable JButtons. "
				+ "To set the start/goal node first select the button at the bottom "
				+ "and then select any button on the grid.</p>"
				+ "<br>"
				+ "<p>Button List:</p>"
				+ "<p>[Set Start]: Set the start button</p>"
				+ "<p>[Set Goal]: Set the goal button</p>"
				+ "<p>[Next]: Iterate the agent by one step.</p>"
				+ "<p>[Run]: Run the program.</p>"
				+ "<p>[Reset]: Resets the map.</p>"
				+ "<br>"
				+ "<p>Color Scheme:</p>"
				+ "<p>Black: Non-traversable node</p>"
				+ "<p>Green: Selected Node (shows information of green node)</p>"
				+ "<p>Yellow: The path found</p>"
				+ "<p>Orange: Nodes to be searched next</p>"
				+ "<p>Red: Nodes that have been searched</p></html>");
		tutorial.setBounds(500, 0, 300, 350);
		tutorial.add(text);
		tutorial.setVisible(true);
		AI a = new AI(unpathablePercentage);
		for(int i = 0; i < a.getMap().length; i++)
			for(int j = 0; j < a.getMap()[i].length; j++)
			{
				if(a.getMap()[i][j].getType() == 1)
					run.getGrid()[i][j].setBackground(Color.BLACK);
			}
		
		//builds reset button
		run.getReset().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				a.resetMap();
				refresh(run, a);
			}
		});

		//builds next button
		run.getNext().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(a.hasNext())
					a.next();
				else
					run.getDisplay().setText(a.getMessage());
				Node[] arr = a.getOpenList().toArray(new Node[0]);
				for(int i = 0; i < arr.length; i++)
				{
					if(!(arr[i].equals(a.getStart()) || arr[i].equals(a.getGoal())))
					{
						run.getGrid()[arr[i].getRow()][arr[i].getCol()].setBackground(Color.ORANGE);
					}
				}
				arr = a.getClosedList().toArray(new Node[0]);
				for(int i = 0; i < arr.length; i++)
				{
					if(!(arr[i].equals(a.getStart()) || arr[i].equals(a.getGoal())))
					{
						run.getGrid()[arr[i].getRow()][arr[i].getCol()].setBackground(Color.RED);
					}
				}
			}
		});

		//build run button
		run.getRun().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//this creates a separate thread so the Thread.sleep does not interfere with the 
				//Event Dispatcher Thread (the one that updates the GUI)
				SwingWorker<String, String> sw = new SwingWorker<String, String>(){
					@Override
					protected String doInBackground() throws Exception {
						while(a.hasNext())
						{
							a.next();
							Node[] arr = a.getOpenList().toArray(new Node[0]);
							for(int i = 0; i < arr.length; i++)
							{
								if(!(arr[i].equals(a.getStart()) || arr[i].equals(a.getGoal())))
								{
									run.getGrid()[arr[i].getRow()][arr[i].getCol()].setBackground(Color.ORANGE);
								}
							}
							arr = a.getClosedList().toArray(new Node[0]);
							for(int i = 0; i < arr.length; i++)
							{
								if(!(arr[i].equals(a.getStart()) || arr[i].equals(a.getGoal())))
								{
									run.getGrid()[arr[i].getRow()][arr[i].getCol()].setBackground(Color.RED);
								}
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {}
						}
						run.getDisplay().setText(a.getMessage());
						Node[] arr = a.getPath();
						//System.out.println(a.getGoal());
						for(int i = 0; i < arr.length; i++)
						{
							if(!(arr[i].equals(a.getStart()) || arr[i].equals(a.getGoal())))
								run.getGrid()[arr[i].getRow()][arr[i].getCol()].setBackground(Color.YELLOW);
							Thread.sleep(100);
						}
						return null;
					}
				};
				sw.execute();
			}
		});

		//builds grid buttons
		for(int i = 0; i < run.getGrid().length; i++)
		{
			for(int j = 0; j < run.getGrid()[i].length; j++)
			{
				run.getGrid()[i][j].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(!run.getPrevious().equals(null))
						{
							run.getSelected().setBackground(run.getPrevious());
						}
						else
						{
							run.getSelected().setBackground(null);
						}
						if(!((JButton) e.getSource()).getBackground().equals(null))
						{
							run.setPrevious(((JButton) e.getSource()).getBackground());
						}
						run.setSelected((JButton) e.getSource());
						run.getSelected().setBackground(Color.GREEN);
						run.getDisplay().setText("Selected Node: " +
								a.getMap()[Integer.parseInt(run.getSelected().getText())/run.getGridSize()]
										[Integer.parseInt(run.getSelected().getText())%run.getGridSize()].toString()
								);
						if(run.getStartFlag())
						{
							run.setStartFlag(!run.getStartFlag());
							run.getStartGrid().setBackground(null);
							run.getSelected().setBackground(Color.MAGENTA);
							run.setStartGrid(run.getSelected());
							a.setStart(a.getMap()[Integer.parseInt(run.getStartGrid().getText())/run.getGridSize()]
									[Integer.parseInt(run.getStartGrid().getText())%run.getGridSize()]);
							run.setSelected(new JButton());
							refresh(run, a);
						}
						if(run.getGoalFlag())
						{
							run.setGoalFlag(!run.getGoalFlag());
							run.getGoalGrid().setBackground(null);
							run.getSelected().setBackground(Color.CYAN);
							run.setGoalGrid(run.getSelected());
							a.setGoal(a.getMap()[Integer.parseInt(run.getGoalGrid().getText())/run.getGridSize()]
									[Integer.parseInt(run.getGoalGrid().getText())%run.getGridSize()]);
							run.setSelected(new JButton());
							refresh(run, a);
						}
					}
				});
			}
		}
	}
	/**
	 * This method is used by the main method to refresh the GUI to a clean state.
	 * 
	 * @param run the GUI to be refreshed
	 * @param a the AI
	 */
	private static void refresh(GUI run, AI a)
	{
		for(int i = 0; i < a.getMap().length; i++)
			for(int j = 0; j < a.getMap()[i].length; j++)
			{
				if(a.getMap()[i][j].getType() == 1)
					run.getGrid()[i][j].setBackground(Color.BLACK);
				else
					run.getGrid()[i][j].setBackground(null);
			}
		if(a.getStart().isSet())
			run.getStartGrid().setBackground(Color.MAGENTA);
		if(a.getGoal().isSet())
			run.getGoalGrid().setBackground(Color.CYAN);
        run.revalidate(); run.repaint(); //forces GUI to update
	}
}