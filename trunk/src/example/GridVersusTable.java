package example;



import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import info.clearthought.layout.TableLayout;



public class GridVersusTable {



    protected static Frame showGridWindow ()
    {
        // Create frame
        Frame frame = new Frame("GridLayout");
        frame.setFont (new Font("Helvetica", Font.PLAIN, 14));
        frame.setLayout (new GridLayout(2, 0));

        // Create and add buttons
        frame.add (new JButton("One"));
        frame.add (new JButton("Two"));
        frame.add (new JButton("Three"));
        frame.add (new JButton("Four"));

        // Show frame
        frame.pack();
        frame.setLocation (0, 10);
		frame.setVisible(true);
		frame.toFront();

        return frame;
    }



    protected static Frame showTableWindow ()
    {
        // Create frame
        Frame frame = new Frame("TableLayout");
        frame.setFont (new Font("Helvetica", Font.PLAIN, 14));

        // Set layout
        double f = TableLayout.FILL;
        double size[][] = {{f, f}, {f, f}};
        frame.setLayout (new TableLayout(size));

        // Create and add buttons
        frame.add (new JButton("One"),   "0, 0");
        frame.add (new JButton("Two"),   "1, 0");
        frame.add (new JButton("Three"), "0, 1");
        frame.add (new JButton("Four"),  "1, 1");

        // Show frame
        frame.pack();
        frame.setLocation (200, 10);
		frame.setVisible(true);
		frame.toFront();

        return frame;
    }



    protected static Frame showTableWindow2 ()
    {
        // Create frame
        Frame frame = new Frame("TableLayout");
        frame.setFont (new Font("Helvetica", Font.PLAIN, 14));

        // Set layout
        double f = TableLayout.FILL;
        double size[][] = {{f, f}, {f, f}};
        frame.setLayout (new TableLayout(size));

        // Create and add buttons
        frame.add (new JButton("One"),   "0, 0");
        frame.add (new JButton("Two"),   "1, 1");

        // Show frame
        frame.pack();
        frame.setLocation (400, 10);
		frame.setVisible(true);
		frame.toFront();

        return frame;
    }



    public static void main (String args[])
    {
        WindowListener listener =
            (new WindowAdapter()
                {
                    public void windowClosing (WindowEvent e)
                    {
                        System.exit (0);
                    }
                }
            );

        Frame frame = showGridWindow();
        frame.addWindowListener(listener);

        frame = showTableWindow();
        frame.addWindowListener(listener);

        frame = showTableWindow2();
        frame.addWindowListener(listener);
    }
}
