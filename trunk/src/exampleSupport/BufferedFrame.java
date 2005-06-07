package exampleSupport;



import java.awt.*;
import java.awt.event.*;



/**
 * This class implements a double buffered window that closes when the user
 * presses the "close window" button.
 *
 * @author  Daniel E. Barbalace
 */

public class BufferedFrame extends Frame implements
    java.awt.event.WindowListener,
    java.awt.event.ComponentListener
{




/** Offscreen buffer used for double buffering */
protected Image offscreen;

/** Offscreen buffer's graphics content */
protected Graphics h;

/** Size of offscreen buffer */
protected Dimension o;

/** Smallest size the window may be */
protected Dimension smallest; 



/**
 * Constructs the frame.
 */

public BufferedFrame ()
{
    this (null);
}



/**
 * Constructs the frame.
 *
 * @param title    text to display in window title area
 */ 

public BufferedFrame (String title)
{
    // Call parent constructor
    super (title);

    // Set minum size to default to 0x0
    smallest = new Dimension(0, 0);

    // Initialize double buffering
    this.offscreen = null;
    this.h = null;

    // This class is its own window listener so it can handle window events
    addWindowListener (this);

    // Prevent going below minimum size and generate offscreen buffer for
    // double buffering
    addComponentListener (this);

    // Create offscreen canvas for double buffering
    createOffscreenBuffer();
}



/**
 * Sets the minimum size of this window.  If the window is resized to be
 * smaller than this size, the window will be enlarged enough to meet this size.
 *
 * @param size    minimum size of this window
 */

public void setMinimumSize (Dimension d)
{
    smallest.width = d.width;
    smallest.height = d.height;
}



/**
 * Sets the minimum size of this window.  If the window is resized to be
 * smaller than this size, the window will be enlarged enough to meet this size.
 *
 * @param width     minimum width of this window
 * @param height    minimum height of this window
 */

public void setMinimumSize (int width, int height)
{
    smallest.width = width;
    smallest.height = height;
}



/**
 * Gets the minimum size of this window.  If the window is resized to be
 * smaller than this size, the window will be enlarged enough to meet this size.
 *
 * @return minimum size of this window
 */

public Dimension getMinimumSize ()
{
    return new Dimension(smallest);
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
    if( h == null )
        // Abort painting, there is no space on which to draw
        return;

    // Set clip area    
    Rectangle clip = g.getClip().getBounds();
    h.setClip (clip);

    // Fill background color
    h.setColor (getBackground());
    Dimension d = getSize();
    h.fillRect (0, 0, d.width, d.height);

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
    // Get size of frame
    o = getToolkit().getScreenSize();

    // Make sure both width and height are positive to avoid a non-fatal
    // exception when creating an image for double buffering
    if( (o.width <= 0) || (o.height <= 0) )
        // Abort function, there is no space on which to draw
        return;

    // Compensate for insets
    o.width += 10;
    o.height += 10;

    // Remove previous background image
    if( offscreen != null )
    {
        // Dispose of offscreen canvas
        offscreen.flush();
        offscreen = null;
        System.gc();
    }

    // Create offscreen canvas
    offscreen = Misc.createImage(o.width, o.height);
    h = offscreen.getGraphics();
}



/**
 * Disposes of this frame and all the resources it uses.
 */

public void dispose ()
{
    // Dispose of offscreen canvas
    if (offscreen != null)
        offscreen.flush();

    // Null all references
    h = null;
    o = null;
    smallest = null;
    offscreen = null;

    // Garbage collection
    System.gc();

    // Call parent dispose
    super.dispose();
}



//******************************************************************************
//** java.awt.event.ComponentListener methods                                ***
//******************************************************************************



/**
 * Invoked when component has been resized.
 */

public void componentResized (ComponentEvent e)
{
    // Assume good size
    boolean resize = false;

    // Get size
    Dimension d = getSize();

    // Make sure width is at least minimum
    if( d.width < smallest.width )
    {
        d.width = smallest.width;
        resize = true;
    }

    // Make sure height is at least minimum
    if( d.height < smallest.height )
    {
        d.height = smallest.height;
        resize = true;
    }

    // Resize if necessary
    if( resize )
        // Set size of this control
        setSize (d);

    // Make sure frame size does not exceed offscreen buffer size.  This
    // could happen if the end user changes resolution at runtime.
    if( (d.width > o.width) || (d.height > o.height) )
        createOffscreenBuffer();

    // Erase previous image on offscreen canvas
    int imageWidth = offscreen.getWidth(null);
    int imageHeight = offscreen.getWidth(null);

    h.setColor (Color.white);
    h.fillRect (d.width, 0, imageWidth - d.width, imageHeight);
    h.fillRect (0, d.height, d.width, imageHeight - d.height);
}



/**
 *Invoked when component has been moved.
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
 * Invoked when a window is in the process of being closed. The close operation
 * can be overridden at this point.  This method does only one thing: terminates
 * the application.  Derived classes want to override this method without
 * calling BufferedFrame.windowClosing if they terminate the application
 * themselves.
 */

public void windowClosing (WindowEvent e)
{
    // Terminate program
    System.exit (0);
}



/**
 * Invoked when a window has been closed.  This method will not be called since
 * windowClosing terminates the application.
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
