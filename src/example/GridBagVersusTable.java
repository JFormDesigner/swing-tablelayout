package example;



import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import info.clearthought.layout.TableLayout;



public class GridBagVersusTable
{



    protected static void makeButton
        (Frame frame, String name, GridBagLayout gridbag, GridBagConstraints c)
    {
        JButton button = new JButton(name);
        gridbag.setConstraints(button, c);
        frame.add(button);
    }



    protected static Frame showGridBagWindow ()
    {
        // Create layout and contraints object
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        // Create frame
        Frame frame = new Frame("GridBagLayout");
        frame.setFont (new Font("Helvetica", Font.PLAIN, 14));
        frame.setLayout (gridbag);

        // Create buttons, add buttons, and apply constraints
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        makeButton (frame, "Button1", gridbag, c);
        makeButton (frame, "Button2", gridbag, c);
        makeButton (frame, "Button3", gridbag, c);

        c.gridwidth = GridBagConstraints.REMAINDER;  //end of row
        makeButton (frame, "Button4", gridbag, c);

        c.weightx = 0.0;                             //reset to the default
        makeButton (frame, "Button5", gridbag, c);   //another row

        c.gridwidth = GridBagConstraints.RELATIVE;   //next-to-last in row
        makeButton (frame, "Button6", gridbag, c);

        c.gridwidth = GridBagConstraints.REMAINDER;  //end of row
        makeButton (frame, "Button7", gridbag, c);

        c.gridwidth = 1;                            //reset to the default
        c.gridheight = 2;
        c.weighty = 1.0;
        makeButton (frame, "Button8", gridbag, c);

        c.weighty = 0.0;                            //reset to the default
        c.gridwidth = GridBagConstraints.REMAINDER; //end of row
        c.gridheight = 1;                           //reset to the default
        makeButton (frame, "Button9", gridbag, c);
        makeButton (frame, "Button10", gridbag, c);

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
        frame.setFont(new Font("Helvetica", Font.PLAIN, 14));

        // Set layout
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double size[][] = {{f, f, f, f}, {p, p, p, p, f}};

        TableLayout layout = new TableLayout(size);
        frame.setLayout (layout);

        // Create buttons labeled Button1 to Button10
        int numButton = 10;
        JButton button[] = new JButton[numButton + 1];

        for (int i = 1; i <= numButton; i++)
            button[i] = new JButton("Button" + i);

        // Add buttons
        frame.add (button[1], "0, 0");
        frame.add (button[2], "1, 0");
        frame.add (button[3], "2, 0");
        frame.add (button[4], "3, 0");
        frame.add (button[5], "0, 1, 3, 1");
        frame.add (button[6], "0, 2, 2, 2");
        frame.add (button[7], "3, 2, 3, 2");
        frame.add (button[8], "0, 3, 0, 4");
        frame.add (button[9], "1, 3, 3, 3");
        frame.add (button[10], "1, 4, 3, 4");

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

        Frame frame = showGridBagWindow();
        frame.addWindowListener (listener);

        frame = showTableWindow();
        frame.addWindowListener (listener);
    }
}
