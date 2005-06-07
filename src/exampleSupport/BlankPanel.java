package exampleSupport;



import java.awt.*;
import java.awt.event.*;



/**
 * A BlankPanel is an invisible panel that can be used as a placeholder for
 * layout managers and as containers that do not have backgrounds.
 *
 * @author  Daniel E. Barbalace
 */

public class BlankPanel extends Container
    implements java.awt.event.ComponentListener
{



private final static LayoutManager panelLayout = new FlowLayout();



/**
 * Constructs a BlankPanel with the default layout, FlowLayout.
 */

public BlankPanel ()
{
    // Call parent constructor
    super();

    // Set default layout
    setLayout (panelLayout);

    // This class needs to be notified when it is resized so it can lay out its
    // components.  Otherwise, layouts will lag one resize behind.
    addComponentListener (this);
}



//******************************************************************************
//** java.awt.event.ComponentListener methods                                ***
//******************************************************************************



/**
 * Invoked when component has been resized.
 *
 * <p>Derived classes that override this method must call this method in
 * their version so that components are layed out properly.</p>
 */

public void componentResized (ComponentEvent e)
{
    // Tell the container class (parent) to layout components.  Without this
    // line, the layout manager will lag one resize behind.
    if (e.getSource() == this)
        doLayout();
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



}
