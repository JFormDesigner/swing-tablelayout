package example;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;



/**
 * Test the bidirectional support of TableLayout.
 *
 * @version 1.0 Oct 28, 2003
 * @author Daniel E. Barbalace
 */

public class BidiTest extends JFrame implements ActionListener
{


	private static final long serialVersionUID = 3616453405678186808L;
	
	
	
	private JCheckBox checkboxIsLtr;
	private JCheckBox checkboxIsHzn;
	private JCheckBox checkboxTableLayout;
	private TableLayout tableLayout;
	private FlowLayout flowLayout;



    /**
     * Runes the test
     *
     * @param args    Command line arguments
     */

    public static void main (String [] args)
    {
    	new BidiTest();
    }


    /**
     * Constructs the test GUI.
     */

	public BidiTest ()
	{
		super("Test BiDi Support");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		flowLayout = new FlowLayout();
		double [][] size = {{-1, -1, -1}, {-1}};
		tableLayout = new TableLayout(size);
		Container container = getContentPane();
		container.setLayout(tableLayout);

		checkboxIsLtr = new JCheckBox("Left to right", true);
		checkboxIsHzn = new JCheckBox("Horizontal", true);
		checkboxIsLtr.addActionListener(this);
		checkboxIsHzn.addActionListener(this);
		container.add(checkboxIsLtr, "0, 0, LD, C");
		container.add(checkboxIsHzn, "1, 0, LD, C");

		checkboxTableLayout = new JCheckBox("TableLayout", true);
		container.add(checkboxTableLayout, "2, 0");
		checkboxTableLayout.addActionListener(this);

		int numButton = 3;

		for (int i = 0; i < numButton; i++)
		{
			JButton button = new JButton("Button " + i);
			tableLayout.insertColumn(i, -1);
			container.add(button, "" + i + ", 0");
		}

		setBounds(100, 100, 640, 480);
		setVisible(true);
		toFront();
	}



	/**
	 * Invoked when an action is performed.
	 */

	public void actionPerformed (ActionEvent e)
	{
		Object source = e.getSource();
		Container container = getContentPane();

		if (source == checkboxTableLayout)
		{
			if (checkboxTableLayout.isSelected())
				container.setLayout(tableLayout);
			else
			    container.setLayout(flowLayout);

			container.doLayout();
		}
		else if ((source == checkboxIsLtr) || (source == checkboxIsHzn))
		{
			boolean ltr = checkboxIsLtr.isSelected();
			boolean hzn = checkboxIsHzn.isSelected();

			ComponentOrientation o = createComponentOrientation(ltr, hzn);
			applyComponentOrientation(o);
			container.doLayout();
			repaint();
		}
	}


	/**
	 * Used to create ComponentOrientation objects.
	 */

	public ComponentOrientation createComponentOrientation
	    (boolean ltr, boolean hzn)
	{
		try
		{
			int [] arrayInt =
			{
				0xAC, 0xED, 0x00, 0x05, 0x73, 0x72, 0x00, 0x1D, 0x6A, 0x61,
				0x76, 0x61, 0x2E, 0x61, 0x77, 0x74,
				0x2E, 0x43, 0x6F, 0x6D, 0x70, 0x6F, 0x6E, 0x65, 0x6E, 0x74,
				0x4F, 0x72, 0x69, 0x65, 0x6E, 0x74,
				0x61, 0x74, 0x69, 0x6F, 0x6E, 0xC6, 0xEA, 0xA7, 0x45, 0xA1,
				0x9C, 0x63, 0xCC, 0x02, 0x00, 0x01,
				0x49, 0x00, 0x0B, 0x6F, 0x72, 0x69, 0x65, 0x6E, 0x74, 0x61,
				0x74, 0x69, 0x6F, 0x6E, 0x78, 0x70,
				0x00, 0x00, 0x00, 0x06
			};

			int numByte = arrayInt.length;
			byte [] array = new byte[numByte];

			for (int i = 0; i < numByte; i++)
				array[i] = (byte) arrayInt[i];

			array[numByte - 1] = (byte) ((ltr ? 4 : 0) | (hzn ? 2 : 0));

			ByteArrayInputStream sba = new ByteArrayInputStream(array);
			ObjectInputStream si = new ObjectInputStream(sba);
			ComponentOrientation o = (ComponentOrientation) si.readObject();
			si.close();
			return o;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}



	/**
	 * Used to hack ComponentOrientation.
	 */

	public void serialize ()
	{
		try
		{
			FileOutputStream fo = new FileOutputStream("ltr.ser");
			ObjectOutputStream so = new ObjectOutputStream(fo);
			so.writeObject(ComponentOrientation.LEFT_TO_RIGHT);
			so.flush();
			so.close();

			fo = new FileOutputStream("rtl.ser");
			so = new ObjectOutputStream(fo);
			so.writeObject(ComponentOrientation.RIGHT_TO_LEFT);
			so.flush();
			so.close();

			fo = new FileOutputStream("unk.ser");
			so = new ObjectOutputStream(fo);
			so.writeObject(ComponentOrientation.UNKNOWN);
			so.flush();
			so.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}



}
