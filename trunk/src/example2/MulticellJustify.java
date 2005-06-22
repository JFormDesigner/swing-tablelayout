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
import java.util.*;
import info.clearthought.layout.TableLayout;



/**
 * Example of components spanning cells and using justification.
 * 
 * @author  Daniel E. Barbalace
 * @version 1.0, June 14, 2005
 */

public class MulticellJustify
{
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
        // Create frame
        JFrame frame = new JFrame("Local Area Connection - Primary Properties");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Items to display in list control
        Vector listElement = new Vector();
        listElement.add("Client for Microsoft Networks");
        listElement.add("File and Print Sharing for Microsoft Networks");
        listElement.add("QoS Packet Scheduler");
        listElement.add("Microsoft TCP/IP version 6");
        listElement.add("Internet Protocol (TCP/IP");
        
        // Create controls
        JLabel labelConnect = new JLabel("Connect using:");
        JTextField textfieldConnect = new JTextField("Intel(R) PRO/100 VE Network Connection");
        JButton buttonConfigure = new JButton("Configure...");
        JLabel labelUse = new JLabel("This connection uses the following items:");
        JList list = new JList(listElement);
        JScrollPane scrollPane = new JScrollPane(list);
        JButton buttonInstall = new JButton("Install");
        JButton buttonUninstall = new JButton("Uninstall");
        JButton buttonProperties = new JButton("Properties");
        JCheckBox checkboxShowIcon = new JCheckBox("Show icon in notification area when connected");
        JCheckBox checkboxNotify = new JCheckBox("Notify me when this connection has limited or no connectivity");
        JButton buttonOK = new JButton("OK");
        JButton buttonCancel = new JButton("Cancel");
        
        // Give buttons a definitive preferred size independent of their labels
        Dimension preferredSize = new Dimension(123, 26);
        buttonConfigure.setPreferredSize(preferredSize);
        buttonInstall.setPreferredSize(preferredSize);
        buttonUninstall.setPreferredSize(preferredSize);
        buttonProperties.setPreferredSize(preferredSize);
        buttonOK.setPreferredSize(preferredSize);
        buttonCancel.setPreferredSize(preferredSize);
        
        // Create and set layout
        double p = TableLayout.PREFERRED;
        double border = 10; 
        double emptySpace = 10;
        double [] columnSize = {border, 1.0 / 3.0, TableLayout.FILL, 1.0 / 3.0, border};
        double [] rowSize = {border, border};
        TableLayout layout = new TableLayout(columnSize, rowSize);
        layout.setVGap(2);
        layout.setHGap(5);
        Container container = frame.getContentPane();
        container.setLayout(layout);
        
        // Place OK and cancel buttons in a panel so that they can be laid out
        // independently of all other controls
        JPanel panelButton = new JPanel();
        TableLayout layoutPanel = new TableLayout(new double [][] {{p, p}, {p}});
        layoutPanel.setHGap(5);
        panelButton.setLayout(layoutPanel);
        panelButton.add(buttonOK, "0, 0");
        panelButton.add(buttonCancel, "1, 0");
        
        // Add controls
        layout.insertRow(1, p);
        container.add(labelConnect, "1, 1, 3, 1");
        layout.insertRow(2, p);
        container.add(textfieldConnect, "1, 2, 2, 2");
        container.add(buttonConfigure, "3, 2");
        layout.insertRow(3, emptySpace);
        layout.insertRow(4, p);
        container.add(labelUse, "1, 4, 3, 4");
        layout.insertRow(5, TableLayout.FILL);
        container.add(scrollPane, "1, 5, 3, 5");
        layout.insertRow(6, p);
        container.add(buttonInstall, "1, 6, LEFT, TOP");
        container.add(buttonUninstall, "2, 6, CENTER, TOP");
        container.add(buttonProperties, "3, 6, RIGHT, TOP");
        layout.insertRow(7, emptySpace);
        layout.insertRow(8, p);
        container.add(checkboxShowIcon, "1, 8, 3, 8");
        layout.insertRow(9, p);
        container.add(checkboxNotify, "1, 9, 3, 9");
        layout.insertRow(10, emptySpace);
        layout.insertRow(11, p);
        container.add(panelButton, "1, 11, 3, 11, RIGHT, TOP");
        
        // Show frame
        frame.pack();
        frame.setVisible(true);
        frame.toFront();
    }
    
    
    
}
