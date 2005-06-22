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
import javax.swing.*;
import info.clearthought.layout.TableLayout;



/**
 * Example of components spanning cells.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, June 21, 2005
 */

public class Scrolling1 implements Runnable
{
	
	
	
	/** Delay between updates in milliseconds */
	private static final int DELAY = 1000 / 60;
	
	/** Space devoted to left/right bouncing */
	private static final int BOUNCE_SPACE = 200;
	
	/** Half of the above space */
	private static final int BOUNCE_SPACE_2 = BOUNCE_SPACE / 2;
	
	/** Instance of TableLayout we are using */
	private TableLayout layout;
	
	/** Container being laid out */
	private Container container;
	
	/** Thread used to scroll top button */
	private Thread threadTop;
	
	/** Thread used to scroll bottom buttom */
	private Thread threadBottom;
	
	
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
    	new Scrolling1();
    }
    
    
    
    /**
     * Creates the app.
     */
    
    public Scrolling1()
    {
        // Create frame
        JFrame frame = new JFrame("Example of scrolling components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create controls
        JButton buttonTop = new JButton("Scrolling");
        JButton buttonBottom = new JButton("By your command");
        
        // Create and set layout
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double [] columnSize = {0, p, BOUNCE_SPACE};
        double [] rowSize = {0, f, p};
        layout = new TableLayout(columnSize, rowSize);
        container = frame.getContentPane();
        container.setLayout(layout);
        
        // Add controls
        container.add(buttonTop, "0, 0, 2, 0");
        container.add(buttonBottom, "1, 2");
        
        // Show frame
        Dimension d = frame.getPreferredSize();
        frame.setSize(d.width, 200);
		frame.setVisible(true);
        frame.toFront();
        
        // Start the scrolling effects
        threadTop = new Thread(this);
        threadBottom = new Thread(this);
        threadTop.start();
        threadBottom.start();
    }
    
    
    
    /**
     * Routes threads.
     */
    
    public void run()
    {
    	Thread thread = Thread.currentThread();
    	
    	if (thread == threadTop)
    		runTop();
    	else if (thread == threadBottom)
    		runBottom();
    }
    
    
    
    /**
     * Scrolls the top button into and out of existence.
     */
    
    private void runTop()
    {
    	double size = 0;
    	double delta = 1;
    	
    	while (true)
    	{
    		size += delta;
    		layout.setRow(0, size);
    		container.invalidate();
    		container.validate();
    		
    		if (size == 0)
    			delta = 1;
    		else if (size == 50)
    			delta = -1;
    		
    		try {Thread.sleep(DELAY);} catch (InterruptedException e) {}
    	}
    }
    
    
    
    /**
     * Scrolls the bottom button left and right.
     */
    
    private void runBottom()
    {
    	int counter = 0;
    	
    	while (true)
    	{
    		int offset = BOUNCE_SPACE_2 + (int)
    			(BOUNCE_SPACE_2 * Math.sin(0.01 * Math.PI * counter++));
    		
    		layout.setColumn(0, offset);
    		layout.setColumn(2, BOUNCE_SPACE - offset);
    		container.invalidate();
    		container.validate();
    		
    		try {Thread.sleep(DELAY);} catch (InterruptedException e) {}
    	}
    }
    
    
    
}
