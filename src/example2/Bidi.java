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
import java.awt.event.*;
import javax.swing.*;
import info.clearthought.layout.TableLayout;



/**
 * This example shows how to use component orientation and the corresponding
 * justifications.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, Jun 27, 2005
 */

public class Bidi implements ActionListener
{
    
    
    /** Application's frame */
    private JFrame frame;
    
    /** Menu to select left to right component orientation */
    private JMenuItem menuLtr;
    
    /** Menu to select right to left component orientation */
    private JMenuItem menuRtl;
    
    /** Menu to select the unknown component orientation */
    private JMenuItem menuUnknown;
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
        new Bidi();
    }



    /**
     * Creates the application's GUI.
     */
    
    public Bidi()
    {
        // Create frame
        frame = new JFrame("Component Orientation and Bidirectional Support");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();

        // Create menu
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Component Orientation");
        menuBar.add(menu);
        menuLtr = new JCheckBoxMenuItem("Left to right");
        menuRtl = new JCheckBoxMenuItem("Right to left");
        menuUnknown = new JCheckBoxMenuItem("Unknown");
        menu.add(menuLtr);
        menu.add(menuRtl);
        menu.add(menuUnknown);
        menuLtr.addActionListener(this);
        menuRtl.addActionListener(this);
        menuUnknown.addActionListener(this);
        menuLtr.setSelected(true);

        // Create all controls
        JLabel labelName    = new JLabel("Name");
        JLabel labelAddress = new JLabel("Address");
        JLabel labelCity    = new JLabel("City");
        JLabel labelState   = new JLabel("State");
        JLabel labelZip     = new JLabel("Zip");

        JTextField textfieldName    = new JTextField(10);
        JTextField textfieldAddress = new JTextField(20);
        JTextField textfieldCity    = new JTextField(10);
        JTextField textfieldState   = new JTextField(2);
        JTextField textfieldZip     = new JTextField(5);

        JButton buttonOk = new JButton("OK");
        JButton buttonCancel = new JButton("Cancel");
        JPanel panelButton = new JPanel();
        panelButton.add(buttonOk);
        panelButton.add(buttonCancel);

        // Create and set layout
        // b - border
        // f - FILL
        // p - PREFERRED
        // vs - vertical space between labels and text fields
        // vg - vertical gap between form elements
        // hg - horizontal gap between form elements

        double b = 10;
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double vs = 5;
        double vg = 10;
        double hg = 10;

        double size[][] =
            {{b, f, hg, p, hg, p, b},
             {b, p, vs, p, vg, p, vs, p, vg, p, vs, p, vg, f, p, b}};

        TableLayout layout = new TableLayout(size);
        pane.setLayout (layout);

        // Add all controls
        pane.add(labelName,        "1,  1, 5, 1, LEADING, BOTTOM");
        pane.add(textfieldName,    "1,  3, 5, 3");
        pane.add(labelAddress,     "1,  5, 5, 5, LEADING, BOTTOM");
        pane.add(textfieldAddress, "1,  7, 5, 7");
        pane.add(labelCity,        "1,  9, LEADING, BOTTOM");
        pane.add(textfieldCity,    "1, 11");
        pane.add(labelState,       "3,  9, LEADING, BOTTOM");
        pane.add(textfieldState,   "3, 11, LEADING, BOTTOM");
        pane.add(labelZip,         "5,  9, LEADING, BOTTOM");
        pane.add(textfieldZip,     "5, 11");
        pane.add(panelButton,      "1, 14, 5, 14");

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }



    /**
     * Invoked when one of the menu items is selected.
     */
    
    public void actionPerformed (ActionEvent e)
    {
        Object source = e.getSource();
        
        if (source == menuLtr)
        {
            frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            menuLtr.setSelected(true);
            menuRtl.setSelected(false);
            menuUnknown.setSelected(false);
        }
        else if (source == menuRtl)
        {
            frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            menuLtr.setSelected(false);
            menuRtl.setSelected(true);
            menuUnknown.setSelected(false);
        }
        else if (source == menuUnknown)
        {
            frame.applyComponentOrientation(ComponentOrientation.UNKNOWN);
            menuLtr.setSelected(false);
            menuRtl.setSelected(false);
            menuUnknown.setSelected(true);
        }
        
        frame.invalidate();
        frame.validate();
    }
    
    
    
}
