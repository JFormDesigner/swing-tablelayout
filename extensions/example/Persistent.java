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

package example;



import java.awt.*;
import java.awt.event.*;
import info.clearthought.layout.*;



public class Persistent extends WindowAdapter implements WindowListener
{



public static void main (String args[])
{
    Frame frame = new Frame("reshow");
    frame.setBounds (100, 100, 300, 300);

    double size[][] =
       {{-2.0, 10.0, 50.0, -1.0, 10.0},  // Columns
       {-2.0, 10.0, 0.25, -1.0, 10.0}}; // Rows

    frame.setLayout (new TableLayout(size));

    Button button;
    button = new Button("3, 3, R, C");
    frame.add (button, "3, 3, R, C");
    button = new Button("3, 3, L, T");
    frame.add (button, "3, 3, L, T");
    button = new Button("2, 3, C, T");
    frame.add (button, "2, 3, C, T");
    button = new Button("3, 2, L, C");
    frame.add (button, "3, 2, L, C");
    button = new Button("2, 2, F, F");
    frame.add (button, "2, 2, F, F");
    button = new Button("3, 3, C, C");
    frame.add (button, "3, 3, C, C");
    frame.addWindowListener(new Persistent());
    frame.show();
}



public void windowClosing (WindowEvent e)
{
    try
    {
        Frame window = (Frame) e.getSource();

        if (window.getTitle().equals("reshow"))
        {
            TableLayoutPersistenceDelegate.writeContainer("Test.xml", window);
            window.dispose();

            window = (Frame)
                TableLayoutPersistenceDelegate.readWindow("Test.xml");
            window.setTitle("don't reshow");
            window.addWindowListener(new Persistent());
            window.setVisible(true);
            window.toFront();
        }
        else
            System.exit(0);
    }
    catch (Exception exception)
    {
        exception.printStackTrace();
    }
}
	

    
}
