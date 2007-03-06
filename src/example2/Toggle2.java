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
import info.clearthought.layout.TableLayoutConstraints;



/**
 * Example of components spanning cells.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, June 20, 2005
 */

public class Toggle2 implements ActionListener
{
	
	
	
	/** Indicates an internal office address */
	private static final String INTERNAL = "Internal";
	
	/** Indicates a domestic (US) address */
	private static final String DOMESTIC = "Domestic";
	
	/** Indicates an international (non-US) address */
	private static final String INTERNATIONAL = "International";
	
	/** Empty space for asthetics */
	private static double EMPTY_SPACE = 10;
	
	/** Instance of TableLayout we are using */
	private TableLayout layout;
	
    /** Container using the layout */
    private Container container;
    
    /** Identifies the type of address */
    private JComboBox comboType;

    // A whole bunch of controls that this application will use.
	JLabel labelOffice = new JLabel("Office");
    JTextField textOffice = new JTextField(5);
    JLabel labelBuilding = new JLabel("Building");
    JComboBox comboBuilding = new JComboBox(new String [] {"A", "B", "C"});
    JLabel labelStreet = new JLabel("Street");
    JLabel labelCity = new JLabel("City");
    JLabel labelState = new JLabel("State");
    JLabel labelZip = new JLabel("Zip Code");
    JLabel labelProvidence = new JLabel("Providence");
    JLabel labelCountry = new JLabel("Country");
    JTextField textStreet = new JTextField(25);
    JTextField textCity = new JTextField(25);
    JTextField textState = new JTextField(2);
    JTextField textZip = new JTextField(5);
    JTextField textProvidence = new JTextField(25);
    JTextField textCountry = new JTextField(25);
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
    	new Toggle2();
    }
    
    
    
    public Toggle2()
    {
        // Create frame
        JFrame frame = new JFrame("Toggling Groups of Rows");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create and set layout
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double border = 10; 
        double [] columnSize = {border, f, border};
        double [] rowSize = {border, border};
        layout = new TableLayout(columnSize, rowSize);
        container = frame.getContentPane();
        container.setLayout(layout);
        
        // Create controls
        JLabel labelType = new JLabel("Type of Address");
        comboType = new JComboBox(new String []
            {INTERNAL, DOMESTIC, INTERNATIONAL});
        comboType.setEditable(false);
        comboType.addActionListener(this);
        comboBuilding.setEditable(false);
        JButton buttonOK = new JButton("OK");
        JButton buttonCancel = new JButton("Cancel");
        JPanel panelButton = new JPanel();
        double p2 = buttonCancel.getPreferredSize().width;
        panelButton.setLayout(new TableLayout(new double [][] {{p2, 5, p2}, {p}}));
        panelButton.add(buttonOK, "0, 0");
        panelButton.add(buttonCancel, "2, 0");
        
        // Add controls
        addControl(labelType);
        addControl(comboType);
        addSpace();
        addControl(labelOffice);
        addControl(textOffice);
        addSpace();
        addControl(labelBuilding);
        addControl(comboBuilding);
        addSpace();
        addControl(labelStreet);
        addControl(textStreet);
        addSpace();
        addControl(labelCity);
        addControl(textCity);
        addSpace();
        addControl(labelState);
        addControl(textState);
        addSpace();
        addControl(labelZip);
        addControl(textZip);
        addSpace();
        addControl(labelProvidence);
        addControl(textProvidence);
        addSpace();
        addControl(labelCountry);
        addControl(textCountry);
        addSpace();
        
        // Add the button panel and then center justify it
        addControl(panelButton);
        TableLayoutConstraints tlc = layout.getConstraints(panelButton);
        tlc.hAlign = TableLayout.CENTER;
        layout.setConstraints(panelButton, tlc);
        
        // Add a filler row above the button panel so that the button panel
        // will stay at the bottom of the window
        layout.insertRow(tlc.row1, TableLayout.FILL);
        
        // Calculate the maximum size the frame would use
        int numItem = comboType.getItemCount();
        Dimension max = new Dimension(0, 0);
		frame.setVisible(true);
        
        for (int i = 0; i < numItem; i++)
        {
        	comboType.setSelectedIndex(i);
        	updateDynamicRows();
        	Dimension d = frame.getPreferredSize();
        	max.width = Math.max(max.width, d.width);
        	max.height = Math.max(max.height, d.height);
        }
        
        comboType.setSelectedIndex(0);
        updateDynamicRows();
        
        // Show frame
        frame.setSize(max.width, max.height);
        frame.toFront();
    }
    
    
    
    /**
     * Adds a row and a control to the content pane.
     */
    
    private void addControl (Component component)
    {
    	int rowNum = layout.getNumRow() - 1;
    	layout.insertRow(rowNum, TableLayout.PREFERRED);
    	container.add(component, new TableLayoutConstraints
    		(1, rowNum, 1, rowNum, TableLayout.LEFT, TableLayout.BOTTOM));
    }
    
    
    
    /**
     * Adds a blank row so that the window looks nice.
     */
    
    private void addSpace()
    {
    	int rowNum = layout.getNumRow() - 1;
    	layout.insertRow(rowNum, EMPTY_SPACE);
    }
    
    
    
    /**
     * Invoked when the checkbox is toggled.
     */
    
    public void actionPerformed (ActionEvent e)
    {
    	updateDynamicRows();
    }
    
    
    
    /**
     * Updates the visibility of dynamic rows.  This method first zero outs all
     * dynamic rows.  Then it resizes the relavent rows to
     * TableLayout.PREFERRED, thereby making their contents visible.  Some
     * rows, like the ones pertaining to the city, are visible for more than
     * one type of address.
     */
    
    private void updateDynamicRows()
    {
    	String type = "" + comboType.getSelectedItem();
    	int rowMin, rowMax, rowStart = -1, rowStop = -1;
    	rowMin = getRow(labelOffice);
    	rowMax = getRow(textCountry);
    	
    	if (type.equals(INTERNAL))
    	{
    		rowStart = getRow(labelOffice);
    		rowStop = getRow(comboBuilding);
    	}
    	else if (type.equals(DOMESTIC))
    	{
    		rowStart = getRow(labelStreet);
    		rowStop = getRow(textZip);
    	}
    	else if (type.equals(INTERNATIONAL))
    	{
    		rowStart = getRow(labelProvidence);
    		rowStop = getRow(textCountry);
    	}
    	
    	if (rowMin >= 0 && rowMax >= 0)
    		for (int y = rowMin; y <= rowMax; y++)
    			layout.setRow(y, 0);
    	
    	if (rowStart >= 0 && rowStop >= 0)
    		for (int y = rowStart; y <= rowStop + 1; y++)
    		{
    			double size = ((y - rowStart) % 3 == 2) ?
    				EMPTY_SPACE : TableLayout.PREFERRED;
    			layout.setRow(y, size);
    		}
    	
    	if (type.equals(INTERNATIONAL))
    	{
    		rowStart = getRow(labelStreet);
    		rowStop = getRow(textCity);
    		
    		if (rowStart >= 0 && rowStop >= 0)
    			for (int y = rowStart; y <= rowStop + 1; y++)
    			{
    				double size = ((y - rowStart) % 3 == 2) ?
    					EMPTY_SPACE : TableLayout.PREFERRED;
    				layout.setRow(y, size);
    			}
    	}
    	
    	container.invalidate();
    	container.validate();
    }
    
    
    
    /**
     * Gets the row associated with a control.
     *
     * @return the row in which a control has been placed or -1 if the component
     *         is not found
     */
    
    private int getRow (Component component)
    {
    	TableLayoutConstraints tlc = layout.getConstraints(component);
    	return (tlc == null) ? -1 : tlc.row1;
    }
    
    
    
}
