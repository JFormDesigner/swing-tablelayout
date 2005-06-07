package exampleSupport;



import java.awt.*;
import java.awt.event.*;



/**
 * This class displays static text like the java.awt.Label class.  However,
 * unlike Label, BlankLabel is a lightweight component with a transparent
 * background.  Thus, it can placed on top of a background image without
 * covering the image.
 *
 * @author  Daniel E. Barbalace
 */

public class BlankLabel extends Component implements
    java.awt.event.ComponentListener
{



// The following constants are used to justify the label.
/** Indicates that the label's text should be left justified. */
public static final int ALIGN_LEFT = 0;
/** Indicates that the label's text should be centered justified. */
public static final int ALIGN_CENTER = 1;
/** Indicates that the label's text should be right justified. */
public static final int ALIGN_RIGHT = 2;



/** Font used when BlankLabel is created */
protected static Font defaultFont = null;

/** Text displayed by label */
protected String label;

/** Label actually printed on button */
protected String shownLabel;

/** Graphics context */
protected Graphics graphics = null;

/** Label alignment */
protected int alignment = ALIGN_LEFT;

/** Height (in pixels) of label */
protected int textHeight;

/** Width (in pixels) of label */
protected int textWidth;

/** Horizontal offset to first character of label */
protected int textInsetX;

/** Vertical offset to baseline of label */
protected int textInsetY;



/**
 * To construct a BlankLabel with a specified label.
 *
 * @param label text to be displayed by the label.  If <code>label</code> is
 *              null, no label is displayed.
 */

public BlankLabel (String label)
{
    // Call parent constructor
    super();

    // Set label; if blank, use no label
    if (label == null)
        this.label = "";
    else
        this.label = label;

    // Use the default font if available
    if (defaultFont != null)
        setFont (defaultFont);

    // This component must know when it has been resized
    addComponentListener (this);
}



/**
 * Sets the font used by all BlankLabels that do not have their font explicitly
 * set.  The change only affects new BlankLabels.  If this method is not called
 * or null is specified for <code>defaultFont</code>, the default component font
 * is used.
 *
 * @param defaultFont font to use as the default font for all new BlankLabels
 */

public static void setDefaultFont (Font defaultFont)
{
    BlankLabel.defaultFont = defaultFont;
}



/**
 * Changes the font being used by the label.
 *
 * @param font font to use
 */

public void setFont (Font font)
{
    // Change font
    super.setFont (font);

    // If the font has been changed, the shown label may need adjusting
    updateShownLabel();
}



/**
 * Gets the text of the label.
 *
 * @return the text of the label as a string
 */

public String getText ()
{
    return label;
}



/**
 * Sets the text of the label.
 *
 * @param label new label to display.  If <code>label</code> is null, no label
 *              is displayed.
 *
 * @return the text of the label as a string
 */

public void setText (String label)
{
    // Set the label's text; null means that no label is used
    if (label == null)
        this.label = "";
    else
        this.label = label;

    // If the text has been changed, the shown label may need adjusting
    updateShownLabel();

    // Indicate that container may need to layout out components
    invalidate();

    // Repaint the label
    repaint();
}



/**
  * Sets the alignment of the label.
  *
  * @param alignment one of the following: ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT
 */

public void setAlignment (int alignment)
{
    this.alignment = alignment;
    repaint();
}



/**
 * Updates the control drawing properties.
 */

protected void updateShownLabel ()
{
    // Set font
    Font font = getFont();

    // Get dimensions of this canvas
    Dimension labelSize = getSize();

    // If the component is not yet visible, it will have no size.  This means
    // purely static components, those that do not change size of position, will
    // never know their size until it's too late.  This is a design flaw --
    // er, uh -- feature of the Abstract Window Toolkit.  To prevent this code
    // from setting the shown label text to the empty screen, whenever the
    // component's size is 0 by 0, the entire label is shown -- that is,
    // everything that fits inside a 0x0 area is shown.
    if ((labelSize.width == 0) && (labelSize.height == 0))
    {
        shownLabel = label;
        return;
    }

    // Get font information
    FontMetrics fm = getFontMetrics(font);
    textHeight = fm.getHeight();
    textWidth = fm.stringWidth(label);

    // Make sure label is small enough to fit in button
    shownLabel = label;

    if (textWidth > labelSize.width)
        shownLabel = shownLabel + "...";

    try
    {
        // Keep removing a character from label until it fits or has no more
        // characters
        while (textWidth > labelSize.width)
        {
            shownLabel = shownLabel.substring(0, shownLabel.length() - 4) +
                         "...";

            textWidth = fm.stringWidth(shownLabel);
        }
    }
    catch (StringIndexOutOfBoundsException error)
    {
        // Label has no more characters except "...", so make label empty
        shownLabel = "";
    }

    // Calculate where to put text baseline and first character
    textInsetX = (labelSize.width - textWidth) / 2;
    textInsetY = (labelSize.height - textHeight) / 2;
}



/**
 * Updates this component.  This method only does one thing: calls <code>paint
 * </code>.
 *
 * @param g    Graphics context
 */

public void update (Graphics g)
{
    // Call paint directly, instead of Component.update, to prevent flickering
    paint (g);
}



/**
 * Draws the label.
 *
 * @param g    Graphics canvas
 */

public void paint (Graphics g)
{
    try
    {
        // Set font
        Font font = getFont();
        g.setFont (font);

        // Get font metrics
        FontMetrics fm = g.getFontMetrics();
        int textHeight = fm.getHeight();
        int textDescent = fm.getDescent();
        int textWidth = fm.stringWidth(shownLabel);
        int width = getSize().width;

        // Calculate offset based on alignment and control size
        int offset = 0;

        switch (alignment)
        {
            case ALIGN_LEFT   : offset = 0; break;
            case ALIGN_CENTER : offset = (width - textWidth) / 2; break;
            case ALIGN_RIGHT  : offset = width - textWidth; break;
        }

        // Draw text
        g.setColor (getForeground());
        g.drawString (shownLabel, offset, textHeight - textDescent);
    }
    catch (NullPointerException exception)
    {
        // sun.awt.windows.WFontMetrics craps out if paint is called before
        // the component is visible or if a time critical condition occurs.  If
        // the component is not visible, there is no point in drawing it.  If
        // the time critical condition occurs, a NullPointerException will
        // occur at both
        // sun.awt.windows.WFontMetrics.stringWidth(WFontMetrics.java:167) and
        // sun.awt.windows.WGraphics.drawString(WGraphics.java:199)
        // So this component cannot be rendered.
    }
}



/**
 * Gets the preferred size of this component.  The preferred size is just enough
 * to fit the label.
 *
 * @return a dimension object indicating this component's preferred size.
 */

public Dimension getPreferredSize()
{
    Dimension dimension;

    try
    {
        // Get font metrics
        FontMetrics fm = getFontMetrics(getFont());

        // Set prefered size to be text size
        int textWidth = fm.stringWidth(label);
        int textHeight = fm.getHeight() + fm.getMaxDescent() - fm.getDescent();
        dimension = new Dimension(textWidth, textHeight);
    }
    catch (NullPointerException exception)
    {
        // Stupid AWT givens NullPointerException if getPreferredSize is called
        // before control is visible
        dimension = new Dimension(0, 0);
    }

    return dimension;
}



//******************************************************************************
//*** java.awt.event.ComponentListener methods                               ***
//******************************************************************************



/**
 * Invoked when component has been resized.
 */

public void componentResized (ComponentEvent e)
{
    // If the component has been resized, the shown label may need adjusting
    if (e.getSource() == this)
        updateShownLabel();
}



/**
 * Invoked when component has been moved.
 */

public void componentMoved (ComponentEvent e)
{
}



/**
 * Invoked when component has been shown.
 */

public void componentShown (ComponentEvent e)
{
}



/**
 * Invoked when component has been hidden.
 */

public void componentHidden (ComponentEvent e)
{
}



}
