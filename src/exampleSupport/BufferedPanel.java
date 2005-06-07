package exampleSupport;



import java.awt.*;
import java.awt.event.*;



/**
 * BufferedPanel is a doubled buffered BlankPanel.
 *
 * @author  Daniel E. Barbalace
 */

public class BufferedPanel extends BlankPanel
{



/** Offscreen buffer used for double buffering */
private Image offscreen;

/** Offscreen buffer's graphics content */
private Graphics h;

/** Size of offscreen buffer */
private Dimension o;



/**
 * Constructs the panel.
 *
 * @param title    text to display in window title area
 */ 

public BufferedPanel ()
{
    // Call parent constructor
    super();

    // Initialize o
    o = new Dimension(0, 0);
}



/**
 * Updates this component.  This method only does one thing: calls <code>paint
 * </code>.
 *
 * @param g    Graphics context
 */

public void update (Graphics g)
{
    paint (g);
}



/**
 * Renders this frame using double buffering.
 *
 * @param g    graphics canvas
 */

public void paint (Graphics g)
{
    // Make sure offscreen buffer exists
    if (h == null)
        // Abort painting, there is no space on which to draw
        return;

    // Call parent's paint
    super.paint (h);

    // Render canvas
    g.drawImage (offscreen, 0, 0, null);
}



/**
 * Creates the offscreen buffer used for double buffering.
 */

private void createOffscreenBuffer ()
{
    // Get size of panel
    o = getSize();

    // Make sure both width and height are positive to avoid a non-fatal
    // exception when creating an image for double buffering
    if ((o.width <= 0) || (o.height <= 0))
        // Abort function, there is no space on which to draw
        return;

    // Remove previous background image
    if (offscreen != null)
    {
        // GC
        offscreen.flush();
        offscreen = null;
        System.gc();
    }

    // Create offscreen canvas
    offscreen = Misc.createImage(o.width, o.height);
    h = offscreen.getGraphics();
}



/**
 * Disposes of this panel and all the resources it uses.
 */

public void dispose ()
{
    // Dispose of offscreen canvas
    if (offscreen != null)
        offscreen.flush();

    // Null all references
    h = null;
    o = null;
    offscreen = null;

    // Garbage collection
    System.gc();
}



//******************************************************************************
//** java.awt.event.ComponentListener methods                                ***
//******************************************************************************



/**
 * Invoked when component has been resized.
 *
 * <p>Derived classes that override this method must call this method.</p>
 */

public void componentResized (ComponentEvent e)
{
    // Call parent method
    super.componentResized (e);

    // Make sure the offscreen buffer is as large as the panel
    Dimension d = getSize();

    if ((d.width > o.width) || (d.height > o.height))
        createOffscreenBuffer();

    // Erase previous image on offscreen canvas
    int imageWidth = offscreen.getWidth(null);
    int imageHeight = offscreen.getWidth(null);

    h.setColor (Color.white);
    h.fillRect (d.width, 0, imageWidth - d.width, imageHeight);
    h.fillRect (0, d.height, d.width, imageHeight - d.height);
}



}
