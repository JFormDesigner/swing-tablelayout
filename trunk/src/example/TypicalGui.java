package example;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;



public class TypicalGui extends JFrame
{



    public static void main (String args[])
    {
        new TypicalGui();
    }



    public TypicalGui ()
    {
        super ("A Typical GUI");

        Container pane = getContentPane();

        double b = 10;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double size[][] = {{b, f, 5, p, 5, p, b}, {p, b, f, 10, p, b}};
        TableLayout layout = new TableLayout(size);
        pane.setLayout (layout);

        addMenu(pane);
        addCommandButtons(pane, layout);
        addColorBoxes(pane, layout);
        addTextArea(pane, layout);

        allowClosing();
        setSize (640, 480);
        setVisible(true);
        toFront();
    }



    public void addMenu (Container pane)
    {
        JMenuBar menuBar = new JMenuBar();

        String menuText[] = {"File", "Edit", "View", "Help"};
        String itemText[][] =
            {{"New", "Open", "Save", "Print", "Exit"},
             {"Cut", "Copy", "Paste"},
             {"Zoom in", "Zoom out"},
             {"About", "Index", "Search"}};

        for (int i = 0; i < menuText.length; i++)
        {
            JMenu menu = new JMenu(menuText[i]);
            menuBar.add (menu);

            for (int j = 0; j < itemText[i].length; j++)
            {
                JMenuItem item = new JMenuItem(itemText[i][j]);
                menu.add (item);
            }
        }

        pane.add (menuBar, "0, 0, 6, 0");
    }



    public void addCommandButtons (Container pane, TableLayout layout)
    {
        JPanel buttonPanel = new JPanel();
        pane.add (buttonPanel, "1, 4, 5, 4");

        for (int i = 1; i <= 5; i++)
        {
            JButton button = new JButton("Button " + i);
            buttonPanel.add (button);
        }
    }



    public void addColorBoxes (Container pane, TableLayout layout)
    {
        Color color[][] =
            {{Color.white, Color.black},
             {Color.green, Color.blue},
             {Color.red, Color.yellow},
             {Color.pink, Color.orange},
             {Color.magenta, Color.cyan}};

        for (int i = 0; i < color.length; i++)
        {
            // Add a row for spacing and a row for the color boxes
            layout.insertRow (2, TableLayout.PREFERRED);
            layout.insertRow (2, 5);

            // Add color boxes
            pane.add (new ColorBox(color[i][0]), "3, 3");
            pane.add (new ColorBox(color[i][1]), "5, 3");
        }

        // Remove the unnecessary leading space
        layout.deleteRow (2);
    }



    public void addTextArea (Container pane, TableLayout layout)
    {
        int numRow = layout.getRow().length;
        JTextPane textArea = new JTextPane();
        pane.add (textArea, "1, 2, 1, " + (numRow - 4));
    }



    public void allowClosing ()
    {
        addWindowListener
            (new WindowAdapter()
                {
                    public void windowClosing (WindowEvent e)
                    {
                        System.exit (0);
                    }
                }
            );
    }



    //**************************************************************************
    //*** Inner classes                                                      ***
    //**************************************************************************



    protected class ColorBox extends Component
    {
        protected Color color;

        protected ColorBox (Color color)
        {
            this.color = color;
        }

        public void update (Graphics g)
        {
            paint (g);
        }

        public void paint (Graphics g)
        {
            Dimension d = getSize();
            g.setColor (Color.black);
            g.drawRect (0, 0, d.width - 1, d.height - 1);

            g.setColor (color);
            g.fillRect (1, 1, d.width - 1, d.height - 1);
        }

        public Dimension getPreferredSize ()
        {
            return new Dimension(40, 20);
        }
    }



}
