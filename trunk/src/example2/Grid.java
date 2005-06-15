package example2;



import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;
import info.clearthought.layout.*;



/**
 * This class can show a grid on top of any container that uses TableLayout.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, Jun 14, 2005
 */

public final class Grid extends Component
{
	
	
	
	private static Color gridColor = Color.BLACK;
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
		String className = "example2.MulticellJustify";
		
        if (args.length == 1)
			className = args[0];
		
		try
		{
			Class cls = Class.forName(className);
			Class [] parameterType = {args.getClass()};
			Object [] parameter = {new String[0]};
			Method method = cls.getMethod("main", parameterType);
			method.invoke(null, parameter);
			
			Frame [] frame = Frame.getFrames();
			
			if (frame.length == 1)
			{
				if (frame[0] instanceof JFrame)
					showGrid(((JFrame) frame[0]).getContentPane());
				else
					showGrid(frame[0]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    
    
    
    /**
     * Shows a grid on a given container provided that the container uses
     * TableLayout.
     * 
     * @param container    container using TableLayout.  May not be null.
     */
    
    public static void showGrid (Container container)
    {
		if (container == null)
			throw new IllegalArgumentException("Parameter container cannot be null");
		
		LayoutManager l = container.getLayout();
		
		if (!(l instanceof TableLayout))
			throw new IllegalArgumentException("Container is not using TableLayout");
		
		TableLayout layout = (TableLayout) l;
		
		showGrid(container, layout);
		
		container.invalidate();
		container.validate();
		container.repaint();
    }
    
    
    
    /**
     * Shows a grid on a given container provided that the container uses
     * TableLayout.
     * 
     * @param container    container using TableLayout.  May not be null.
     * @param layout       TableLayout instance used by container.  May not be null.
     */
    
    private static void showGrid (Container container, TableLayout layout)
    {
		int numRow = layout.getNumRow();
		int numCol = layout.getNumColumn();
		
		for (int y = 0; y < numRow; y++)
			for (int x = 0; x < numCol; x++)
			{
				Grid g = new Grid();
				container.add(g, new TableLayoutConstraints(x, y), 0);
			}
    }
	
	
	
	/**
	 * Gets the grid color.
	 * 
	 * @return the grid color
	 */
	
	public static Color getGridColor()
	{
		return gridColor;
	}
	
	
	
	/**
	 * Sets the grid color.
	 * 
	 * @param color    new grid color
	 */
	
	public static void setGridColor (Color color)
	{
		gridColor = color;
	}
	
	
	
	/**
	 * Prevent creation of instances outside of this class.
	 */
	
	private Grid()
	{
	}
	
	
	
	/**
	 * Renders the component.
	 * 
	 * @param g    graphics canvas
	 */
	
	public void paint (Graphics g)
	{
		Dimension d = getSize();
		g.setColor(gridColor);
		g.drawRect(0, 0, d.width - 1, d.height - 1);
	}
	
	
	
}
