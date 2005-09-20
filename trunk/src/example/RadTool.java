package example;



import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import exampleSupport.GeneralDialog;



public class RadTool extends Frame implements ActionListener
{



    private JTextField textfieldColumnNumber;
    private JTextField textfieldColumnSize;
    private JTextField textfieldRowNumber;
    private JTextField textfieldRowSize;
    private JButton buttonAddColumn;
    private JButton buttonRemoveColumn;
    private JButton buttonResizeColumn;
    private JButton buttonAddRow;
    private JButton buttonRemoveRow;
    private JButton buttonResizeRow;
    private JButton buttonShowLayout;
    private JButton buttonGenerateCode;
    private TextArea textArea;
    private JPanel panel;
    private TableLayout layout;
    private ArrayList columnHeader;
    private ArrayList rowHeader;
    private ArrayList boxList;



    public static void main (String args[])
    {
        new RadTool();
    }



    /**
     * Constructs the user interface.
     */

    public RadTool()
    {
        // Create frame
        super("Example of Dynamic Rows and Columns");

        // Create a TableLayout for the frame
        double b = 10;
        double s = 5;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double size[][] =
            {{b, p, s, p, s, p, s, p, s, p, s, p, s, p, f, b},
             {f, s, p, s, p, s, 100, s, p, b}};

        setLayout (new TableLayout(size));

        // Create static controls
        JLabel labelColumn = new JLabel("Column");
        JLabel labelColumnSize = new JLabel("Size");
        textfieldColumnNumber = new JTextField(2);
        textfieldColumnSize = new JTextField(2);
        buttonAddColumn = new JButton("Add");
        buttonRemoveColumn = new JButton("Remove");
        buttonResizeColumn = new JButton("Resize");

        JLabel labelRow = new JLabel("Row");
        JLabel labelRowSize = new JLabel("Size");
        textfieldRowNumber = new JTextField(2);
        textfieldRowSize = new JTextField(5);
        buttonAddRow = new JButton("Add");
        buttonRemoveRow = new JButton("Remove");
        buttonResizeRow = new JButton("Resize");

        textArea = new TextArea();
        columnHeader = new ArrayList();
        rowHeader = new ArrayList();
        boxList = new ArrayList();
        buttonShowLayout = new JButton("Show Layout");
        buttonGenerateCode = new JButton("Generate Code");

        // Add static controls
        add (labelColumn,           " 1, 2");
        add (textfieldColumnNumber, " 3, 2");
        add (labelColumnSize,       " 5, 2");
        add (textfieldColumnSize,   " 7, 2");
        add (buttonAddColumn,       " 9, 2");
        add (buttonRemoveColumn,    "11, 2");
        add (buttonResizeColumn,    "13, 2");

        add (labelRow,              " 1, 4");
        add (textfieldRowNumber,    " 3, 4");
        add (labelRowSize,          " 5, 4");
        add (textfieldRowSize,      " 7, 4");
        add (buttonAddRow,          " 9, 4");
        add (buttonRemoveRow,       "11, 4");
        add (buttonResizeRow,       "13, 4");

        add (textArea,              "1, 6, 14, 6");
        add (buttonShowLayout,      "1, 8,  7, 6");
        add (buttonGenerateCode,    "9, 8, 12, 8");

        // Listen for button events
        buttonAddColumn.addActionListener (this);
        buttonRemoveColumn.addActionListener (this);
        buttonResizeColumn.addActionListener (this);
        buttonAddRow.addActionListener (this);
        buttonRemoveRow.addActionListener (this);
        buttonResizeRow.addActionListener (this);
        buttonShowLayout.addActionListener (this);
        buttonGenerateCode.addActionListener (this);

        // Add a panel for RadTool controls
        panel = new JPanel();
        panel.setBackground (Color.white);
        add (panel, "0, 0, 15, 0");

        // Create the layout manager for the panel
        double size2[][] = {{p, -1}, {p, -1}};
        layout = new TableLayout(size2);
        panel.setLayout (layout);
        updateHeader();
        updateBox();

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
        setBackground (Color.lightGray);
        setBounds (100, 100, 500, 400);
		setVisible(true);
		toFront();
    }



    /**
     * Handles all action events.
     */

    public void actionPerformed (ActionEvent e)
    {
        // Get row and column information from text fields
        int row = getInt(textfieldRowNumber);
        int col = getInt(textfieldColumnNumber);
        double rowSize = getDouble(textfieldRowSize);
        double colSize = getDouble(textfieldColumnSize);

        // Get source of the event
        Object source = e.getSource();

        try
        {
            // Update layout
            if (source == buttonAddColumn)
                layout.insertColumn (col, colSize);
            else if (source == buttonRemoveColumn)
                layout.deleteColumn (col);
            else if (source == buttonResizeColumn)
                layout.setColumn (col, colSize);
            else if (source == buttonAddRow)
                layout.insertRow (row, rowSize);
            else if (source == buttonRemoveRow)
                layout.deleteRow (row);
            else if (source == buttonResizeRow)
                layout.setRow (row, rowSize);

            // Update headers, etc. to reflect layout's change
            updateHeader();
            updateBox();

            // Layout and repaint panel since the layout has changed
            panel.doLayout();
            panel.repaint();

            // Update layout's description
            textArea.setText(layout.toString());

            // Generate code if desired
            if (source == buttonGenerateCode)
                generateCode();
        }
        catch (Throwable error)
        {
            error.printStackTrace();
            textArea.setText (error.toString());
        }
    }



    /**
     * Converts the text in a text field to an integer.
     */

    private int getInt (JTextField field)
    {
        int value = 0;

        try
        {
            value = Integer.parseInt(field.getText());
        }
        catch (NumberFormatException e) {}

        return value;
    }



    /**
     * Converts the text in a text field to a double.
     */

    private double getDouble (JTextField field)
    {
        double value = -1.0;

        try
        {
            value = Double.parseDouble(field.getText());
        }
        catch (NumberFormatException e) {}

        return value;
    }



    /**
     * Updates all the row and columns headers by bruce force.  The headers
     * are removed and then recreated based on the current layout configuration.
     */

    private void updateHeader ()
    {
        TableLayoutConstraints c = new TableLayoutConstraints
            (0, 0, 0, 0, TableLayout.FULL, TableLayout.FULL);

        double size[] = layout.getColumn();

        for (int i = columnHeader.size() - 1; i >= 0;  i--)
        {
            JButton header = (JButton) columnHeader.remove(i);
            panel.remove(header);
        }

        for (int i = 0; i < size.length; i++)
        {
            c.col1 = c.col2 = i;
            JButton header = new JButton("" + i + ": " + size[i]);
            columnHeader.add(i, header);
            panel.add (header, c);
        }

        c.col1 = c.col2 = 0;
        size = layout.getRow();

        for (int i = rowHeader.size() - 1; i >= 0;  i--)
        {
            JButton header = (JButton) rowHeader.remove(i);
            panel.remove(header);
        }

        for (int i = 0; i < size.length; i++)
        {
            c.row1 = c.row2 = i;
            JButton header = new JButton("" + i + ": " + size[i]);
            rowHeader.add(i, header);
            panel.add (header, c);
        }
    }



    /**
     * Updates the boxes used to illustrate cells.  This is a brute force,
     * unoptimized method.
     */

    private void updateBox ()
    {
        for (int i = boxList.size() - 1; i >= 0;  i--)
        {
            Box box = (Box) boxList.remove(i);
            panel.remove(box);
        }

        TableLayoutConstraints constraint = new TableLayoutConstraints
            (0, 0, 0, 0, TableLayout.FULL, TableLayout.FULL);

        double col[] = layout.getColumn();
        double row[] = layout.getRow();

        for (int c = 1; c < col.length; c++)
            for (int r = 1; r < row.length; r++)
            {
                constraint.col1 = constraint.col2 = c;
                constraint.row1 = constraint.row2 = r;
                Box box = new Box();
                panel.add (box, constraint);
            }
    }



    /**
     * Generates code based on the current layout and Smiley controls.
     */

    private void generateCode ()
    {
        String code =
            "import java.awt.*;\n" +
            "import java.awt.event.*;\n" +
            "import layout.TableLayout;\n\n" +

            "public class MyClass\n" +
            "{\n\n" +

            "    public static void main (String args[])\n" +
            "    {\n" +
            "        Frame frame = new Frame(\"MyTitle\");\n" +
            "        frame.setBounds (100, 100, 300, 300);\n\n" +

            "       double size[][] =\n" +
            "           {{";

        double size[] = layout.getColumn();

        if (size.length > 0)
            code += size[0];

        for (int i = 1; i < size.length; i++)
            code += ", " + size[i];

        code += "},  // Columns\n" +
            "           {";

        size = layout.getRow();

        if (size.length > 0)
            code += size[0];

        for (int i = 1; i < size.length; i++)
            code += ", " + size[i];

        code += "}}; // Rows\n\n" +
            "        frame.setLayout (new TableLayout(size));\n\n" +
            "        Button button;\n";

        Component component[] = panel.getComponents();

        for (int i = 0; i < component.length; i++)
        {
            if (component[i] instanceof Smiley)
            {
                TableLayoutConstraints c = layout.getConstraints(component[i]);
                String constraint = "" +  c.col1 + ", " + c.row1 + ", ";

                if ((c.col1 == c.col2) && (c.row1 == c.row2))
                {
                    String h[] = {"L", "C", "F", "R"};
                    String v[] = {"T", "C", "F", "B"};
                    constraint += h[c.hAlign] + ", " + v[c.vAlign];
                }
                else
                    constraint += c.col2 + ", " + c.row2;

                code +=
                    "        button = new Button(\"" + constraint + "\");\n" +
                    "        frame.add (button, \"" + constraint + "\");\n";
            }
        }

        code +=
            "\n" +
            "        frame.addWindowListener\n" +
            "            (new WindowAdapter()\n" +
            "                {\n" +
            "                    public void windowClosing (WindowEvent e)\n" +
            "                    {\n" +
            "                        System.exit (0);\n" +
            "                    }\n" +
            "                }\n" +
            "            );\n\n" +
            "        frame.show();\n" +
            "    }\n" +
            "}\n";

        textArea.setText (code);
    }



    /**
     * This inner class is a component that looks like a box.
     */

    public class Box extends Component implements MouseListener
    {
        public Box ()
        {
            super();
            addMouseListener(this);
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
        }

        public void mouseExited(java.awt.event.MouseEvent mouseEvent) {}
        public void mousePressed(java.awt.event.MouseEvent mouseEvent) {}
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {}
        public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {}

        public void mouseReleased(java.awt.event.MouseEvent mouseEvent)
        {
            TableLayoutConstraints c = layout.getConstraints(this);
            Smiley smiley = new Smiley();
            Container container = getParent();
            container.add(smiley, c, 0);
            container.doLayout();
        }
    }



    /**
     * This inner class is a component that looks like a smiley face.
     */

    public class Smiley extends Component implements MouseListener
    {
        public Smiley ()
        {
            super();
            addMouseListener (this);
        }

        public Dimension getPreferredSize ()
        {
            return new Dimension(64, 64);
        }

        public void update (Graphics g)
        {
            paint (g);
        }

        public void paint (Graphics g)
        {
            Dimension d = getSize();
            int width_1_8 = d.width >> 3;
            int width_1_4 = d.width >> 2;
            int width_3_4 = d.width - width_1_4;
            int width_5_8 = width_3_4 - width_1_8;
            int width_1_2 = d.width >> 1;
            int height_1_8 = d.height >> 3;
            int height_7_8 = d.height - height_1_8;
            int height_1_4 = d.height >> 2;
            int height_5_8 = height_7_8 - height_1_4;

            g.setColor (Color.yellow);
            g.fillArc (0, 0, d.width - 1, d.height - 1, 0, 360);
            g.setColor (Color.blue);
            g.fillArc (width_1_4, height_1_4, width_1_8, height_1_8, 0, 360);
            g.fillArc (width_5_8, height_1_4, width_1_8, height_1_8, 0, 360);
            g.setColor (Color.red);
            g.fillArc (width_1_4, height_5_8, width_1_2, height_1_4, 180, 180);
        }

        public void mouseExited(java.awt.event.MouseEvent mouseEvent) {}
        public void mousePressed(java.awt.event.MouseEvent mouseEvent) {}
        public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {}
        public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {}

        public void mouseReleased(java.awt.event.MouseEvent mouseEvent)
        {
            // Get constraints applied to this Smiley
            TableLayoutConstraints c = layout.getConstraints(this);

            // Create controls
            Panel panel = new Panel();

            int numRow = layout.getNumRow();
            int numColumn = layout.getNumColumn();

            Choice choiceCol1 = new Choice();
            Choice choiceCol2 = new Choice();
            Choice choiceRow1 = new Choice();
            Choice choiceRow2 = new Choice();

            for (int i = 1; i < numColumn; i++)
            {
                choiceCol1.add ("" + i);
                choiceCol2.add ("" + i);
            }

            for (int i = 1; i < numRow; i++)
            {
                choiceRow1.add ("" + i);
                choiceRow2.add ("" + i);
            }

            choiceCol1.select (c.col1 - 1);
            choiceCol2.select (c.col2 - 1);
            choiceRow1.select (c.row1 - 1);
            choiceRow2.select (c.row2 - 1);

            Choice choiceAlignH = new Choice();
            choiceAlignH.add ("Left");
            choiceAlignH.add ("Center");
            choiceAlignH.add ("Full");
            choiceAlignH.add ("Right");
            choiceAlignH.select (c.hAlign);

            Choice choiceAlignV = new Choice();
            choiceAlignV.add ("Top");
            choiceAlignV.add ("Center");
            choiceAlignV.add ("Full");
            choiceAlignV.add ("Bottom");
            choiceAlignV.select (c.vAlign);

            JLabel labelCol1 = new JLabel("Column 1");
            JLabel labelCol2 = new JLabel("Column 2");
            JLabel labelRow1 = new JLabel("Row 1");
            JLabel labelRow2 = new JLabel("Row 2");
            JLabel labelAlignH = new JLabel("Horizontal Justification");
            JLabel labelAlignV = new JLabel("Vertical Justification");
            labelAlignH.setHorizontalAlignment (JLabel.RIGHT);
            labelAlignV.setHorizontalAlignment (JLabel.RIGHT);

            // Create layout
            double f = TableLayout.FILL;
            double p = TableLayout.PREFERRED;
            double s = 10;

            double size[][] =
                {{f, p, s, p, s, p, s, p, f},
                 {p, s, p, s, p, s, p, s, p, s, p}};

            TableLayout panelLayout = new TableLayout(size);
            panel.setLayout (panelLayout);

            // Add controls
            panel.add (labelCol1,    "1, 0, R, B");
            panel.add (choiceCol1,   "3, 0, L, B");
            panel.add (labelRow1,    "5, 0, R, B");
            panel.add (choiceRow1,   "7, 0, L, B");
            panel.add (labelCol2,    "1, 2, R, B");
            panel.add (choiceCol2,   "3, 2, L, B");
            panel.add (labelRow2,    "5, 2, R, B");
            panel.add (choiceRow2,   "7, 2, L, B");
            panel.add (labelAlignH,  "0, 4, 3, 4");
            panel.add (choiceAlignH, "5, 4, 7, 4");
            panel.add (labelAlignV,  "0, 6, 3, 6");
            panel.add (choiceAlignV, "5, 6, 7, 6");

            // Prompt user
            String button[] = {"Update Smiley", "Remove Smiley", "Cancel"};

            GeneralDialog dialog = new GeneralDialog
                (RadTool.this, "Update Smiley", "", button, null, panel);

            String answer = dialog.promptUser();

            // Update constraints applied to this Smiley
            if (answer.equals("Update Smiley"))
            {
                // Get columns
                int col1 = choiceCol1.getSelectedIndex() + 1;
                int col2 = choiceCol2.getSelectedIndex() + 1;
                int row1 = choiceRow1.getSelectedIndex() + 1;
                int row2 = choiceRow2.getSelectedIndex() + 1;

                // Make sure col1 < col2
                if (col1 > col2)
                {
                    int temp = col1;
                    col1 = col2;
                    col2 = temp;
                }

                // Make sure row1 < row2
                if (row1 > row2)
                {
                    int temp = row1;
                    row1 = row2;
                    row2 = temp;
                }

                // Apply new constraints
                c.col1 = col1;
                c.col2 = col2;
                c.row1 = row1;
                c.row2 = row2;
                c.hAlign = choiceAlignH.getSelectedIndex();
                c.vAlign = choiceAlignV.getSelectedIndex();
                layout.setConstraints (this, c);

                // Repaint and layout container since layout has changed
                Container container = getParent();
                container.doLayout();
                container.repaint();
            }
            // Remove Smiley
            else if (answer.equals("Remove Smiley"))
            {
                Container container = getParent();
                container.remove (this);
                container.doLayout();
                container.repaint();
            }
        }
    }



}
