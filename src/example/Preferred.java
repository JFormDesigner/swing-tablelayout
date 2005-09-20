package example;



import java.awt.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;



public class Preferred extends JFrame
{



    public static void main (String args[])
    {
        new Preferred();
    }



    public Preferred ()
    {
        super("The Power of Preferred Sizes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();

        // b - border
        // f - FILL
        // p - PREFERRED
        // vs - vertical space between labels and text fields
        // vg - vertical gap between form elements
        // hg - horizontal gap between form elements

        double b = 10;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double vs = 5;
        double vg = 10;
        double hg = 10;

        double size[][] =
            {{b, f, hg, p, hg, p, b},
             {b, p, vs, p, vg, p, vs, p, vg, p, vs, p, vg, p, b}};

        TableLayout layout = new TableLayout(size);
        pane.setLayout (layout);

        // Create all controls
        JLabel labelName    = new JLabel("Name");
        JLabel labelAddress = new JLabel("Address");
        JLabel labelCity    = new JLabel("City");
        JLabel labelState   = new JLabel("State");
        JLabel labelZip     = new JLabel("Zip");

        JTextField textfieldName    = new JTextField(10);
        JTextField textfieldAddress = new JTextField(20);
        JTextField textfieldCity    = new JTextField(10);
        JTextField textfieldState   = new JTextField(2);
        JTextField textfieldZip     = new JTextField(5);

        JButton buttonOk = new JButton("OK");
        JButton buttonCancel = new JButton("Cancel");
        JPanel panelButton = new JPanel();
        panelButton.add(buttonOk);
        panelButton.add(buttonCancel);

        // Add all controls
        pane.add(labelName,        "1,  1, 5, 1");
        pane.add(textfieldName,    "1,  3, 5, 3");
        pane.add(labelAddress,     "1,  5, 5, 5");
        pane.add(textfieldAddress, "1,  7, 5, 7");
        pane.add(labelCity,        "1,  9");
        pane.add(textfieldCity,    "1, 11");
        pane.add(labelState,       "3,  9");
        pane.add(textfieldState,   "3, 11");
        pane.add(labelZip,         "5,  9");
        pane.add(textfieldZip,     "5, 11");
        pane.add(panelButton,      "1, 13, 5, 13");

        pack();
        setResizable(false);
		setVisible(true);
		toFront();
    }



}
