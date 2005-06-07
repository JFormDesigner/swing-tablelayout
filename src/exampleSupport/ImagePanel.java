package exampleSupport;



import java.awt.*;
import java.awt.event.*;



/**
 * An ImagePanel is a panel that has a background image.  The background can
 * be tiled, stretched, scaled, centered, or placed in the upper left hand
 * corner of the panel.
 *
 * @author  Daniel E. Barbalace
 * @version 5.1, January 5, 1998
 */

public class ImagePanel extends BlankPanel
{



/** Indicates that the background image should be tiled. */
public static final int IMAGE_TILED = 0;

/** Indicates that the background image should be resized to fit the container
    while maintaining the aspect ratio.  Blank space will be add as necessary
    to the background.  The blank space will use the background color. */
public static final int IMAGE_SCALED = 1;

/** Indicates that the background image should be resized to fit the container
    without maintaining the aspect ratio. */
public static final int IMAGE_STRETCHED = 2;

/** Indicates that the background image should be centered.  Blank space in the
    background color may be used.  The background image may be cropped. */
public static final int IMAGE_CENTERED = 3;

/** Indicates that the background image should be placed in the upper left hand
    corner of the panel.  Blank space in the background color may be used.  The
    background image may be cropped. */
public static final int IMAGE_UNCHANGED = 4;



/** Default algorithm used to scale the background image */
protected static int defaultScalingAlgorithm = Image.SCALE_DEFAULT;



/** One of the following methods of generating the background: IMAGE_TILED,
    IMAGE_SCALED, IMAGE_STRETCHED, IMAGE_CENTERED, IMAGE_UNCHANGED */
protected int backgroundType;

/** Algorithm used to scale background */
protected int scalingAlgorithm;

/** Width of this panel in pixels */
protected int panelWidth;

/** Height of this panel in pixels */
protected int panelHeight;

/** Width of source image in pixels */
protected int sourceWidth;

/** Height of source image in pixels */
protected int sourceHeight;

/** Image used for background */
protected Image source;

/** Scaled or tiled image used for background */
protected Image background;



/**
 * Sets the default algorithm used to scale the background of ImagePanels.
 * After this method is called, all ImagePanels created will use the algorithm
 * specified here unless <code>setScalingAlgorithm</code> is called.  If this
 * method is never called, the default scaling algorithm is <code>
 * Image.SCALE_DEFAULT</code>.
 *
 * @param scalingAlgorithm    scaling algorithm to use.  This should be on of
 *                            the scaling algorithm constants specified in
 *                            java.awt.Image.
 *
 * @see setScalingAlgorithm
 */

public static void setDefaultScalingAlgorithm (int scalingAlgorithm)
{
    defaultScalingAlgorithm = scalingAlgorithm;
}



/**
 * Sets the algorithm used to scale the background of this ImagePanel.
 *
 * @param scalingAlgorithm    scaling algorithm to use.  This should be on of
 *                            the scaling algorithm constants specified in
 *                            java.awt.Image.
 *
 * @see setDefaultScalingAlgorithm
 */

public void setScalingAlgorithm (int scalingAlgorithm)
{
    this.scalingAlgorithm = scalingAlgorithm;
}



/**
 * Constructs an instance of ImagePanel that has no background.
 */

public ImagePanel ()
{
    this (null);
}



/**
 * Constructs an instance of ImagePanel with a given background.  The background
 * will be tiled.
 *
 * @param background    image use for the background.  If <code>background
 *                      </code> is null, this panel will have no background.
 */

public ImagePanel (Image background)
{
    this (background, IMAGE_TILED);
}



/**
 * Constructs an instance of ImagePanel with a given background and background
 * type.
 *
 * @param background        image use for the background.  If <code>background
 *                          </code> is null, this panel will have no background.
 *
 * @param backgroundType    one of the following methods of generating the
 *                          background: <code>IMAGE_TILED, IMAGE_SCALED,
 *                          IMAGE_STRETCHED, IMAGE_CENTERED, IMAGE_UNCHANGED
 *                          </code>.
 */

public ImagePanel (Image background, int backgroundType)
{
    // Initialize members
    this.background = null;
    this.backgroundType = backgroundType;
    scalingAlgorithm = defaultScalingAlgorithm;

    // This class generates its background and when resized
    addComponentListener (this);

    // Set the background image
    setBackground (background);
}



/**
 * Sets background type.  This method does nothing if <code>backgroundType
 * </code> is invalid.
 *
 * @param backgroundType    one of the following methods of generating the
 *                          background: <code>IMAGE_TILED, IMAGE_SCALED,
 *                          IMAGE_STRETCHED, IMAGE_CENTERED, IMAGE_UNCHANGED
 *                          </code>.
 */

public void setBackgroundType (int backgroundType)
{
    // Make sure backgroundType is valid
    if ((backgroundType >= IMAGE_TILED) && (backgroundType <= IMAGE_UNCHANGED))
    {
        // Update the background type
        this.backgroundType = backgroundType;

        // Recreate the background
        generateBackground();

        // Repaint panel to display new background
        repaint();
    }
}



/**
 * Sets the background image.  This method is obsolete and will be removed
 * shortly.  Use setBackground instead.
 */

public void setImage (Image background)
{
    setBackground (background);
}



/**
 * Sets the image used for this ImagePanel's background.
 *
 * @param background    image use for the background.  If <code>background
 *                      </code> is null, this panel will have no background.
 */

public void setBackground (Image background)
{
    // Get the image to display
    source = background;

    if (source != null)
    {
        // Determine the source image's width and height
        sourceWidth = source.getWidth(null);
        sourceHeight = source.getHeight(null);
    }

    // Generate the background image
    generateBackground();

    // Repaint panel to display new background
    repaint();
}



/**
 * Creates the background image used by this ImagePanel.
 */

protected void generateBackground ()
{
    // Remove previous background image
    if (background != null)
    {
        // GC
        background.flush();
        background = null;
        System.gc();
    }
    
    // Make sure both width and height are positive to avoid a non-fatal
    // exception when creating an image for double buffering
    if ((panelWidth <= 0) || (panelHeight <= 0))
        // Abort function, there is no space on which to draw
        return;

    // Create the background image
    background = createImage(panelWidth, panelHeight);

    // Get the background's graphics context
    Graphics h = background.getGraphics();

    // Was a source image given
    if (source == null)
    {
        // Fill with background color
        h.setColor (getBackground());
        h.fillRect (0, 0, panelWidth, panelHeight);
    }
    else
        // Draw the background image
        switch (backgroundType)
        {
            case IMAGE_TILED     : generateTiledImage(h);     break;
            case IMAGE_SCALED    : generateScaledImage(h);    break;
            case IMAGE_STRETCHED : generateStretchedImage(h); break;
            case IMAGE_CENTERED  : generateCenteredImage(h);  break;
            case IMAGE_UNCHANGED : generateUnchangedImage(h); break;
        }

    // Free resources used by graphics context
    h.dispose();
}



/**
 * Creates the background image by tiling the source image.
 *
 * @param h    graphics context of background image
 */

protected void generateTiledImage (Graphics h)
{
    // The following code has been commented out since tiling images is handled
    // in the paint method for efficiency.

    /*
    // Fill with background image
    for (int y = 0; y < panelHeight; y += sourceHeight)
        for (int x = 0; x < panelWidth; x += sourceWidth)
            h.drawImage (source, x, y, null);
    */
}



/**
 * Creates the background image by scaling the source image while maintaining
 * the aspect ratio.
 *
 * @param h    graphics context of background image
 */

protected void generateScaledImage (Graphics h)
{
    // Create a media tracker to determine when image has been scaled
    MediaTracker mediaTracker = new MediaTracker(this);

    // Calculate new image size
    Dimension container = new Dimension(panelWidth, panelHeight);
    Dimension image = new Dimension(sourceWidth, sourceHeight);
    Dimension desired = Misc.scaleUpOrDown(container, image);

    // Scale the image
    Image scaledImage = source.getScaledInstance(desired.width, desired.height,
        scalingAlgorithm);

    try
    {
        // Wait for the image to be scaled
        mediaTracker.addImage (scaledImage, 0);
        mediaTracker.waitForAll();
    }
    catch (InterruptedException error) {}

    // Fill background with the background color
    h.setColor (getBackground());
    h.fillRect (0, 0, panelWidth, panelHeight);

    // Put the image in the center of the background
    h.drawImage (scaledImage, (panelWidth - desired.width) / 2,
        (panelHeight - desired.height) / 2, null);
}



/**
 * Creates the background image by scaling the source image without maintaining
 * the aspect ratio.
 *
 * @param h    graphics context of background image
 */

protected void generateStretchedImage (Graphics h)
{
    // Create a media tracker to determine when image has been scaled
    MediaTracker mediaTracker = new MediaTracker(this);

    // Scale the image
    background = source.getScaledInstance(panelWidth, panelHeight,
        scalingAlgorithm);

    try
    {
        // Wait for the image to be scaled
        mediaTracker.addImage (background, 0);
        mediaTracker.waitForAll();
    }
    catch (InterruptedException error) {}
}



/**
 * Creates the background image by centering the source image on the background.
 * This method may cause the source image to be cropped or blank space to be
 * shown in the panel.  The blank space will be the background color.
 *
 * @param h    graphics context of background image
 */

protected void generateCenteredImage (Graphics h)
{
    // Fill background with the background color
    h.setColor (getBackground());
    h.fillRect (0, 0, panelWidth, panelHeight);

    // Put the image in the center of the background
    h.drawImage (source, (panelWidth - sourceWidth) / 2,
        (panelHeight - sourceHeight) / 2, null);
}



/**
 * Creates the background image by placing the source image in the upper left
 * hand corner of the background.  This method may cause the source image to be
 * cropped or blank space to be shown in the panel.  The blank space will be the
 * background color.
 *
 * @param h    graphics context of background image
 */

protected void generateUnchangedImage (Graphics h)
{
    // Fill background with the background color
    h.setColor (getBackground());
    h.fillRect (0, 0, panelWidth, panelHeight);

    // Put the image in the center of the background
    h.drawImage (source, 0, 0, null);
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
 * Draws the panel by filling it with the background image and painting all
 * subcomponents.
 *
 * @param g    Graphics canvas
 */

public void paint (Graphics g)
{
    Dimension d = getSize();

    // For efficiency, handle tiled image seperately
    if (backgroundType == IMAGE_TILED)
    {
        // Make sure background is not null
        if (source == null)
        {
            // Just fill with background
            g.setColor (getBackground());
            g.fillRect (0, 0, d.width, d.height);
        }
        else
            // Just title source, since doing that is more efficient
            for (int i = 0; sourceWidth * i < d.width; i++)
                for (int j = 0; sourceHeight * j < d.height; j++)
                    g.drawImage
                        (source, i * sourceWidth, j * sourceHeight, null);
    }
    // Handle everything else
    else
    {
        // Make sure background is not null
        if (background == null)
        {
            // Just fill with background
            g.setColor (getBackground());
            g.fillRect (0, 0, d.width, d.height);
        }
        else
            // Fill with background image
            g.drawImage (background, 0, 0, null);
    }

    // Call parent's paint
    super.paint (g);
}



//******************************************************************************
//*** java.awt.event.ComponentListener methods                               ***
//******************************************************************************



/**
 * Invoked when component has been resized.
 */

public void componentResized (ComponentEvent e)
{
    // Get the size of this panel
    Dimension panelSize = getSize();

    // NOTE: The following line exists because two resize messages are sent for
    //       every resize.

    // Make sure the panel's size has really changed
    if ((panelSize.width != panelWidth) || (panelSize.height != panelHeight))
    {
        // Remember panel's new size
        panelWidth = panelSize.width;
        panelHeight = panelSize.height;

        // Generate the background to fit new size
        generateBackground();
    }
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
