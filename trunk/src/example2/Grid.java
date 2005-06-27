/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 1.0
 *
 * Copyright (c) 2001 Daniel Barbalace.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. The original software may not be altered.  However, the classes
 *    provided may be subclasses as long as the subclasses are not
 *    packaged in the info.clearthought package or any subpackage of
 *    info.clearthought.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR, AFFILATED BUSINESSES,
 * OR ANYONE ELSE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */



package example2;



import java.awt.*;
import java.lang.reflect.*;
import javax.swing.*;
import info.clearthought.layout.*;



/**
 * This class can show a grid on top of any container that uses TableLayout.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, Jun 14, 2005
 */

public final class Grid extends Component
{
    
    
    
    private static Color gridColor = Color.BLACK;
    private static Color fillColor = null;
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
        setFillColor(Color.BLUE);
        String className = "example2.MulticellJustify";
        
        if (args.length == 1)
            className = args[0];
        
        try
        {
            Class cls = Class.forName(className);
            Class [] parameterType = {args.getClass()};
            Object [] parameter = {new String[0]};
            Method method = cls.getMethod("main", parameterType);
            method.invoke(null, parameter);
            
            Frame [] frame = Frame.getFrames();
            
            if (frame.length == 1)
            {
                if (frame[0] instanceof JFrame)
                    showGrid(((JFrame) frame[0]).getContentPane());
                else
                    showGrid(frame[0]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Shows a grid on a given container provided that the container uses
     * TableLayout.
     * 
     * @param container    container using TableLayout.  May not be null.
     */
    
    public static void showGrid (Container container)
    {
        if (container == null)
            throw new IllegalArgumentException("Parameter container cannot be null");
        
        LayoutManager l = container.getLayout();
        
        if (!(l instanceof TableLayout))
            throw new IllegalArgumentException("Container is not using TableLayout");
        
        TableLayout layout = (TableLayout) l;
        
        showGrid(container, layout);
        
        container.invalidate();
        container.validate();
        container.repaint();
    }
    
    
    
    /**
     * Shows a grid on a given container provided that the container uses
     * TableLayout.
     * 
     * @param container    container using TableLayout.  May not be null.
     * @param layout       TableLayout instance used by container.  May not be null.
     */
    
    private static void showGrid (Container container, TableLayout layout)
    {
        int numRow = layout.getNumRow();
        int numCol = layout.getNumColumn();
        
        for (int y = 0; y < numRow; y++)
            for (int x = 0; x < numCol; x++)
            {
                Grid g = new Grid();
                container.add(g, new TableLayoutConstraints(x, y), 0);
            }
    }
    
    
    
    /**
     * Gets the grid color.
     * 
     * @return the grid color
     */
    
    public static Color getGridColor()
    {
        return gridColor;
    }
    
    
    
    /**
     * Sets the grid color.
     * 
     * @param color    new grid color
     */
    
    public static void setGridColor (Color color)
    {
        gridColor = color;
    }
    
    
    
    /**
     * Gets the fill color.
     * 
     * @return the fill color
     */
    
    public static Color getFillColor()
    {
        return fillColor;
    }
    
    
    
    /**
     * Sets the fill color.
     * 
     * @param color    new fill color
     */
    
    public static void setFillColor (Color color)
    {
        fillColor = color;
    }
    
    
    
    /**
     * Prevent creation of instances outside of this class.
     */
    
    private Grid()
    {
    }
    
    
    
    /**
     * Renders the component.
     * 
     * @param g    graphics canvas
     */
    
    public void paint (Graphics g)
    {
        Dimension d = getSize();
        
        if (gridColor != null)
        {
            g.setColor(gridColor);
            g.drawRect(0, 0, d.width - 1, d.height - 1);
        }
        
        if (fillColor != null)
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite
                (AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2d.setPaint(fillColor);
            g2d.fill(new Rectangle(1, 1, d.width - 1, d.height - 1));
        }
    }
    
    
    
}
