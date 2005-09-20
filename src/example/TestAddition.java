package example;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;



public class TestAddition extends Frame implements ActionListener
{



    private JCheckBox buttonToggle;
    private Component hello;
    private JCheckBox checkboxRelayout;
    private JCheckBox checkboxTableLayout;
    private TableLayout tableLayout;
    private FlowLayout flowLayout;
    
    

    public static void main (String args[])
    {
        new TestAddition();
    }


    
    public TestAddition ()
    {
        // Create a frame
        super("Test Addition");
        setBounds (100, 100, 500, 300);

        // Create a TableLayout for the frame
        double border = 10;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double size[][] =
            {{border, p, p,  p, border},
             {border, f, p, border}};

        tableLayout = new TableLayout(size);
        tableLayout.setVGap(10);
        tableLayout.setHGap(20);
        flowLayout = new FlowLayout();
        setLayout (tableLayout);
        
        // Create and add controls
        buttonToggle = new JCheckBox("Toggle");
        add(buttonToggle, "1, 2");
        buttonToggle.addActionListener(this);
        
        hello = new JButton("Hello");
        checkboxRelayout = new JCheckBox("Relayout and repaint", true);
        add (checkboxRelayout, "2, 2");
        checkboxTableLayout = new JCheckBox("Use TableLayout", true);
        add (checkboxTableLayout, "3, 2");
        checkboxTableLayout.addActionListener(this);

        // Allow user to close the window to terminate the program
        addWindowListener
            (new WindowAdapter()
                {
                    public void windowClosing (WindowEvent e)
                    {
                        System.exit (0);
                    }
                }
            );

        // Show frame
        setVisible(true);
        toFront();
    }
    
    
    
    public void actionPerformed (ActionEvent e)
    {
        Object source = e.getSource();
        
        if (source == buttonToggle)
        {
            Container parent = hello.getParent();
            
            if (parent == null)
                add(hello, "1, 1, 3, 1");
            else
                parent.remove(hello);

            if (checkboxRelayout.isSelected())
            {
                doLayout();
                repaint();
            }
        }
        else if (source == checkboxTableLayout)
        {
            if (checkboxTableLayout.isSelected())
                setLayout(tableLayout);
            else
                setLayout(flowLayout);

            doLayout();
            repaint();
        }
    }



}