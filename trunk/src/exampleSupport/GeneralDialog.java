package exampleSupport;



import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.JButton;
import info.clearthought.layout.SingleFiledLayout;
import info.clearthought.layout.TableLayout;



/**
 * GeneralDialog is a general purpose dialog.
 *
 * @author  Daniel E. Barbalace
 */

public class GeneralDialog extends Dialog implements
    java.awt.event.ActionListener,
    java.awt.event.WindowListener
{



/** If there is no panel inside the dialog and the space required for the
    buttons is small, the text will be wrapped to DEFAULT_TEXT_WIDTH */
protected static final int DEFAULT_TEXT_WIDTH = 300;

/** Default font used for text if dialog's parent frame does not have a font */
protected static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);



/** Constructor used to create buttons or null to use JButton */
protected static Constructor buttonConstructor;



/** Array of buttons that can be used to dismiss the dialog */
protected JButton button[];

/** Answer that the user gave to the prompt */
protected String answer;

/** Panel that implements double buffering */
protected BufferedPanel panelBuffer;

/** Panel that holds the background image */
protected ImagePanel panelImage;

/** Panel that holds the buttons which dismiss the dialog */
protected BlankPanel panelButton;

/** Layout manager used by this dialog */
protected TableLayout tableLayout;



//******************************************************************************
//*** Static property methods                                                ***
//******************************************************************************



/**
 * Sets the class used to create buttons.
 *
 * @param template    an instance of the class to be used
 */

public static void setButtonClass (JButton template)
{
    try
    {
        // Get the class for java.lang.String
        Class parameterList[] = {null};
        parameterList[0] = Class.forName("java.lang.String");

        // Get the template's class
        Class classButton = template.getClass();

        // Get the constructor for the class
        buttonConstructor = classButton.getConstructor(parameterList);
    }
    catch (Exception error)
    {
        buttonConstructor = null;
    }
}



//******************************************************************************
//*** Constructors                                                           ***
//******************************************************************************



/**
 * Constructs an instance of GeneralDialog.
 *
 * @param parent        owner of the dialog
 * @param title         dialog's title
 * @param text          text to display in dialog
 * @param label         list of button labels
 * @param background    background image
 */

public GeneralDialog
    (Frame parent, String title, String text, String label[], Image background)
{
    this (parent, title, text, label, background, null);
}



/**
 * Constructs an instance of GeneralDialog.
 *
 * @param parent        owner of the dialog.  parent cannot be null.
 * @param title         dialog's title
 * @param text          text to display in dialog
 * @param label         list of button labels
 * @param background    background image
 * @param component     a component to add after the text
 */

public GeneralDialog
    (Frame parent, String title, String text, String label[], Image background,
     Component component)
{
    // Call parent constructor
    super (parent, title, true);

    // Check parameter parent
    if (parent == null)
        throw new IllegalArgumentException("Parameter parent cannot be null.");

    // Listen to window events so that dialog close button will work
    addWindowListener (this);

    // Construct basic components that are in all general dialogs
    constructComponent (text, label, background, component);

    // Set size and location using default criteria
    useDefaultBoundCriteria();
}



/**
 * Constructs an instance of GeneralDialog.  This constructor is intended to be
 * used by subclasses.  Subclasses should call the constructComponent method
 * after calling this constructor.  Unless a subclass is setting the dialog's
 * bounds, the subclass should call the useDefaultBoundCriteria method after
 * calling constructComponent to set the dialog's initial bounds.
 *
 * @param parent        owner of the dialog.  parent cannot be null.
 * @param title         dialog's title
 */

protected GeneralDialog (Frame parent, String title)
{
    // Call parent constructor
    super (parent, title, true);

    // Check parameter parent
    if (parent == null)
        throw new IllegalArgumentException("Parameter parent cannot be null.");

    // Listen to window events so that dialog close button will work
    addWindowListener (this);
}



//******************************************************************************
//*** Methods that support constructors and subclass constructors            ***
//******************************************************************************



/**
 * Construct basic components that are in all general dialogs.
 *
 * @param text          text to display in dialog
 * @param label         list of button labels
 * @param background    background image
 * @param component     a component to add after the text
 */

protected void constructComponent
    (String text, String label[], Image background, Component component)
{
    // Check parameters
    if (text == null)
        text = "";

    if (label == null)
        label = new String[0];

    // Add a buffered panel to prevent flickering
    setLayout (new GridLayout(1, 0));
    panelBuffer = new BufferedPanel();
    add (panelBuffer);

    // Add an image background
    panelBuffer.setLayout (new GridLayout(1, 0));
    panelImage = new ImagePanel(background);
    panelBuffer.add (panelImage);

    // Create layout of image panel
    double border = 20;
    double space = 10;
    double f = TableLayout.FILL;
    double p = TableLayout.PREFERRED;

    double size[][] = {{border, f, border}, {border, p, space, p, border}};

    tableLayout = new TableLayout(size);
    panelImage.setLayout (tableLayout);

    // Note: Since the text formatting depends, in part, on the button panel's
    // size, the button panel must be constructed first.

    // Add a panel for the buttons
    panelButton = new BlankPanel();
    panelImage.add (panelButton, "1, 3");
    int flowGap = 20;
    panelButton.setLayout (new FlowLayout(FlowLayout.CENTER, flowGap, 0));

    // By default, buttons are JButtons with black foregrounds regardless
    // of what this GeneralDialog's foreground is
    panelButton.setForeground (Color.black);

    // Create an array of buttons
    button = new JButton[label.length];

    for (int i = 0; i < button.length; i++)
    {
        // Create button
        button[i] = createJButton(label[i]);

        // Add button to button panel
        panelButton.add (button[i]);

        // Dismiss dialog when buttons are pressed
        button[i].addActionListener (this);
    }

    // Get preferred size of the button panel
    Dimension sizeButtonPanel = panelButton.getPreferredSize();

    // Add a panel for the text
    BlankPanel panelText = new BlankPanel();
    panelImage.add (panelText, "1, 1");
    panelText.setLayout (new SingleFiledLayout
        (SingleFiledLayout.COLUMN, SingleFiledLayout.LEFT, 0));

    // Get font.  Use default if parent frame has no font.  This can happen if
    // the parent frame has not been shown yet.
    Font font = getFont();

    if (font == null)
    {
        font = DEFAULT_FONT;
        setFont (font);
    }

    // Format the text
    Vector textLine = formatText(text, font, sizeButtonPanel.width);

    // Add text to text panel
    for (int i = 0; i < textLine.size(); i++)
    {
        BlankLabel blankLabel = new BlankLabel((String) textLine.elementAt(i));
        blankLabel.setFont (font);
        blankLabel.setForeground (null);
        panelText.add (blankLabel);
    }

    // If there is no text, don't put a space between the empty text panel and
    // the button panel
    if (textLine.size() == 0)
        tableLayout.setRow (2, 0);

    // Add optional component
    if (component != null)
    {
        tableLayout.insertRow (3, TableLayout.FILL);
        tableLayout.insertRow (4, 10);
        panelImage.add (component, "1, 3");
    }
}



/**
 * Sets the size and location of this dialog based on the dialog's preferred
 * size, the screen resolution, and the bounds of the dialog's parent frame.
 */

protected void useDefaultBoundCriteria ()
{
    // Use minimum size
    pack();

    // Get parent window
    Component parent = getParent();

    // Set location to center of parent window
    Dimension d = getSize();
    Dimension e = parent.getSize();
    Dimension r = getToolkit().getScreenSize();

    Point p = parent.getLocation();
    Point q = new Point();

    q.x = p.x + ((e.width - d.width) >> 1);
    q.y = p.y + ((e.height - d.height) >> 1);

    // Make sure window is on screen
    if ((q.x + d.width) > r.width)
        q.x = r.width - d.width;

    if ((q.y + d.height) > r.height)
        q.y = r.height - d.height;

    if (q.x < 0)
        q.x = 0;

    if (q.y < 0)
        q.y = 0;

    // Position dialog
    setLocation (q);

    // Make dialog non-resizable
    setResizable (false);

    // The following line is necessary to avoid a problem with IBM's high
    // performance compiler, HPJ.  HPJ will sometimes resize the dialog when
    // the constructor returns unless the following line is present.  It appears
    // to be a timing problem and a side effect of the code between this pack
    // and the prior pack.
    pack();
}



/**
 * Creates an JButton.  This method will attempt to use the subclass of
 * JButton specified.  If no subclass was specified or an error occurs
 * creating the instance of the subclass, this method will create an instance
 * of JButton.
 *
 * @param label    button label
 *
 * @return an instance of JButton or a subclass of JButton
 */

protected JButton createJButton (String label)
{
    JButton button = null;

    try
    {
        // No constructor specified, use an JButton
        if (buttonConstructor == null)
            button = new JButton(label);
        // Constructor specified, use it
        else
        {
            // Attempt to construct an instance of the subclass of
            // JButton
            Object parameter[] = {label};
            button = (JButton) buttonConstructor.newInstance(parameter);
        }
    }
    catch (Exception error)
    {
        // Something went wrong creating the instance with the constructor.
        // Use an JButton instead.
        button = new JButton(label);
    }

    return button;
}



/**
 * Formats text in an attempt to satisfy several conditions.  First, use all
 * of the minimum width.  Second, maintain as close to a 4:3 aspect ratio as
 * possible.
 *
 * @param text     text to be formatted
 * @param font     font to use
 * @param width    minimum width to use
 *
 * @return a vector of strings, one for each line
 */

protected Vector formatText (String text, Font font, int width)
{
    // Check parameters
    if (text == null)
        throw new IllegalArgumentException("Parameter text cannot be null");
    else if (font == null)
        throw new IllegalArgumentException("Parameter font cannot be null");
    else if (width < 0)
        throw new IllegalArgumentException
            ("Parameter width cannot be negative");

    // If text is the empty string, return an empty vector.  There will be
    // zero lines of text.
    if (text.length() == 0)
        return new Vector();

    // Use greater of default text width and requested width
    int maxWidth = (width > DEFAULT_TEXT_WIDTH) ? width : DEFAULT_TEXT_WIDTH;

    // Get font metrics
    FontMetrics fm = getToolkit().getFontMetrics(font);

    // Create an empty vector
    Vector vector = new Vector();

    // Start with an empty line
    String line = "";
    int length = text.length();

    // Keep adding characters to vector until all characters are used
    for (int i = 0; i < length; i++)
    {
        // Get next character
        char ch = text.charAt(i);

        // New lines terminate a line
        if (ch == '\n')
        {
            vector.addElement (line);
            line = "";
        }
        // Check for word wrap at whitespaces
        else if ((ch == ' ') || (ch == '\t'))
        {
            // Check line width
            int currentWidth = fm.stringWidth(line);

            // Wrap at word if minimum width is satisfied
            if (currentWidth >= maxWidth)
            {
                // Find prior whitespace
                int j = line.length();
                char priorChar = '.';

                while ((j > 0) && (priorChar != ' ') && (priorChar != '\t'))
                {
                    // Go to previous character
                    j--;
                    priorChar = line.charAt(j);
                }

                // If a prior whitespace was found, wrap line at that character
                if (j > 0)
                {
                    // Add all characters up to the whitespace
                    i = i - line.length() + j;
                    line = line.substring(0, j);

                    // Skip all whitespaces till next space (not tab)
                    while ((i < length - 1) && (text.charAt(i + 1) == ' '))
                        i++;
                }

                // Add line
                vector.addElement (line);
                line = "";
            }
            // Otherwise, add whitespace to line
            else
                line += ch;
        }
        // Add all non-whitespace characters to the line
        else
            line += ch;
    }

    // Add last line if not blank
    if (line.length() > 0)
        vector.addElement (line);

    return vector;
}



//******************************************************************************
//*** Public methods for users of this class and subclasses                  ***
//******************************************************************************



/**
 * Prompts the user.  If the caller wants to position the dialog, make it
 * resizable, make it non-modal, etc., the caller should do this before calling
 * promptUser.
 *
 * @return the label of the button pressed, or "" if the dialog close button
 *         was pressed
 */

public String promptUser ()
{
    // Assume that the user will press the dialog close button
    answer = "";

    // Set focus to first JButton unless another control requested
    // focus (possible if caller had an embeded control request focus) or
    // there are no buttons.
    if ((getFocusOwner() == null) && (button.length > 0))
        button[0].requestFocus();

    // Show dialog.  The current thread will block until the dialog is disposed.
    // Note: Never call promptUser with the AWT-EventThread0 thread!  Spawn a
    // worker thread if you want to bring up a dialog as a result of an action
    // or other AWT event.
    setVisible(true);
    toFront();

    return answer;
}



/**
 * Updates this component.  This method only does one thing: calls <code>paint
 * </code>.
 *
 * @param g    Graphics context
 */

public void update (Graphics g)
{
    // To prevent flickering, call paint without drawing background
    paint (g);
}



//******************************************************************************
//*** java.awt.event.ActionListener methods                                  ***
//******************************************************************************



/**
 * Invoked when an action occurs.
 */

public void actionPerformed (ActionEvent e)
{
    // Get source
    Object source = e.getSource();

    // Check for dismiss buttons
    for (int i = 0; i < button.length; i++)
    {
        // Was the ith button pressed
        if (source == button[i])
        {
            // The user's response is the button's label
            answer = button[i].getText();

            // Dismiss dialog
            dispose();

            // Stop searching
            return;
        }
    }
}



//******************************************************************************
//** java.awt.event.WindowListener methods                                   ***
//******************************************************************************



/**
* Invoked when a window has been opened.
*/

public void windowOpened (WindowEvent e)
{
}



/**
 * Invoked when a window is in the process of being closed. This method will
 * dispose of the dialog box and allow execution to continue for the thread
 * that invoked promptUser.
 */

public void windowClosing (WindowEvent e)
{
    // Close dialog
    dispose();
}



/**
 * Invoked when a window has been closed.
 */

public void windowClosed (WindowEvent e)
{
}



/**
 * Invoked when a window is iconified.
 */

public void windowIconified (WindowEvent e)
{
}



/**
 * Invoked when a window is de-iconified.
 */

public void windowDeiconified (WindowEvent e)
{
}



/**
 * Invoked when a window is activated.
 */

public void windowActivated (WindowEvent e)
{
}



/**
 * Invoked when a window is de-activated.
 */

public void windowDeactivated (WindowEvent e)
{
}



}
