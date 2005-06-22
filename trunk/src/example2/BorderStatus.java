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
import java.io.*;
import java.net.*;
import javax.swing.*;
import info.clearthought.layout.*;



/**
 * BorderStatus expands the BorderSimple demonstration by adding a heading and
 * a status bar.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, Jun 17, 2005
 */

public class BorderStatus implements ActionListener
{
    
    
    
    /** Main frame */
    private JFrame frame;
    
    /** Used to display the path of the file being displayed */
    private JLabel labelFilename; 
    
    /** When selected, files will be displayed as web pages */
    private JRadioButton radioWebPage;
    
    /** When selected, files will be displayed as text */
    private JRadioButton radioText;
    
    /** Used to display HTML files as web pages */
    private JEditorPane paneWeb;
    
    /** Used to display HTML files as text */
    private JTextPane paneText;
    
    /** Used to scroll document view */
    private JScrollPane scrollPane;
    
    /** Open file menu item */
    private JMenuItem menuOpenFile;
    
    /** Open URL item */
    private JMenuItem menuOpenUrl;
    
    /** Exit menu item */
    private JMenuItem menuExit;
    
    /** Status bar */
    private StatusBar statusBar;
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
        new BorderStatus();
    }
    
    
    
    /**
     * Creates the application's GUI.
     */
    
    public BorderStatus()
    {
        // Create frame
        frame = new JFrame("Border with a heading and a status bar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuOpenFile = menu.add("Open File");
        menuOpenUrl = menu.add("Open URL");
        menuExit = menu.add("Exit");
        menuOpenFile.addActionListener(this);
        menuOpenUrl.addActionListener(this);
        menuExit.addActionListener(this);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    
        // Create controls
        Heading heading = new Heading();
        JLabel labelFile = new JLabel("File:");
        labelFilename = new JLabel("");
        JLabel labelView = new JLabel("View as:");
        radioWebPage = new JRadioButton("Web Page");
        radioText = new JRadioButton("Text");
        ButtonGroup groupView = new ButtonGroup();
        groupView.add(radioWebPage);
        groupView.add(radioText);
        radioWebPage.setSelected(true);
        radioWebPage.addActionListener(this);
        radioText.addActionListener(this);
        paneWeb = new JEditorPane();
        paneWeb.setEditable(false);
        paneText = new JTextPane();
        paneText.setEditable(false);
        
        scrollPane = new JScrollPane(paneWeb);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setMinimumSize(new Dimension(10, 10));
        
        statusBar = new StatusBar();
        
        // Create and set layout
        double p = TableLayout.PREFERRED;
        double border = 10; 
        double [] columnSize = {border, p, p, p, TableLayout.FILL, border};
        double [] rowSize = {p, border, p, p, TableLayout.FILL, border, p};
        TableLayout layout = new TableLayout(columnSize, rowSize);
        layout.setHGap(5);
        Container container = frame.getContentPane();
        container.setLayout(layout);
        
        // Add controls
        container.add(heading,       "0, 0, 5, 0");
        container.add(labelFile,     "1, 2, RIGHT, BOTTOM");
        container.add(labelFilename, "2, 2, 4, 2");
        container.add(labelView,     "1, 3, RIGHT, CENTER");
        container.add(radioWebPage,  "2, 3");
        container.add(radioText,     "3, 3");
        container.add(scrollPane,    "1, 4, 4, 4");
        container.add(statusBar,     "0, 6, 5, 6");
        
        // Show one html file by default
        setPath("/sample.html");
        
        // Show frame
        frame.pack();
        frame.setVisible(true);
        frame.toFront();
    }
    
    
    
    /**
     * Sets the path of the document to view.
     *  
     * @param path    full or relative path of document
     */
    
    private void setPath (String path)
    {
        // Attempt to open file in jar
        URL url = BorderStatus.class.getResource(path);
        
        // Attempt to open a full URL
        if (url == null)
        {
            try
            {
                url = new URL(path);
            }
            catch (MalformedURLException e) {}
        }
        
        // Attempt to open a local file
        if (url == null)
        {
            try
            {
                File file = new File(path);
                url = file.toURL();
            }
            catch (MalformedURLException e) {}
        }
        
        // If any of the attempts succeeded, open the url
        if (url != null)
        {
            try
            {
                paneWeb.setPage(url);
                paneText.setText(getContent(url));
                labelFilename.setText(path);
                statusBar.setText("Loaded " + url);
            }
            catch (IOException e)
            {
                paneWeb.setText("");
                paneText.setText("");
                labelFilename.setText("");
                statusBar.setText("Attempted to read a bad URL: " + url);
            }
        }
        else
        {
            statusBar.setText("Couldn't find file: " + path);
        }
    }
    
    
    
    /**
     * Gets the contents of a URL as a string.
     * 
     * @param url    document to get
     * 
     * @return a string containing the URL's contents
     */
    
    private String getContent (URL url)
    {
        StringBuffer content = new StringBuffer();
        
        try
        {
            String line = "";
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            
            while (line != null)
            {
                content.append(line);
                
                if (line.length() > 0)
                    content.append('\n');
                
                line = input.readLine();
            }
            
            input.close();
        }
        catch (MalformedURLException me) {}
        catch (IOException e) {}
        
        return content.toString();
    }
    
    
    
    /**
     * Invoked when one of the radio buttons is selected.
     */
    
    public void actionPerformed (ActionEvent e)
    {
        Object source = e.getSource();
        
        if (source == radioWebPage)
            scrollPane.setViewportView(paneWeb);
        else if (source == radioText)
            scrollPane.setViewportView(paneText);
        else if (source == menuOpenFile)
        {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(menuOpenFile);
            
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                   setPath(file.getPath());
            }
        }
        else if (source == menuOpenUrl)
        {
            String message = "URL to open";
            String s = (String) JOptionPane.showInputDialog
                (frame, message, "Open URL", JOptionPane.PLAIN_MESSAGE, null, null, null);
            
            if ((s != null) && (s.length() > 0))
                setPath(s);
        }
        else if (source == menuExit)
            System.exit(0);
    }
    
    

    /**
     * A simple graphic to display at the top of the GUI, just below the menu
     * bar.
     */
    
    private class Heading extends Component
    {
        public Dimension getPreferredSize()
        {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(400, fm.getHeight() * 3);
        }
        
        public void paint (Graphics g)
        {
            Dimension d = getSize();
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint
                (0, 0, new Color(26, 80, 184), d.width, 0, frame.getBackground(), true);
            g2.setPaint(gradient);
            g2.fill(new Rectangle(d.width, d.height));
            
            g2.setPaint(Color.white);
            int x = 0;
            int i = 0;
            String [] letter = {"J", "a", "v", "a"};
            
            while (x < d.width)
            {
                double offset = (d.height * Math.sin(2 * Math.PI * x / 100) / 4);
                g2.translate(x, offset);
                g2.drawString(letter[i++], 0, d.height / 2);
                g2.translate(-x, -offset);
                x += 10;
                
                if (i == letter.length)
                    i = 0;
            }
            
            x = 0;
            while (x < d.width)
            {
                double offset = (d.height * -Math.sin(2 * Math.PI * x / 100) / 4);
                g2.translate(x, offset);
                g2.drawLine(0, d.height / 2, 0, d.height / 2);
                g2.translate(-x, -offset);
                x++;
            }
        }
    }
    
    
    
    /**
     * A simple status bar.
     */
    
    private class StatusBar extends Component
    {
        private String text = "";
        
        public StatusBar()
        {
            setForeground(Color.BLACK);
            setBackground(Color.LIGHT_GRAY);
        }
        
        public void setText (String text)
        {
            if (text != null)
            {
                this.text = text;
                repaint();
            }
        }
        
        public String getText()
        {
            return text;
        }
        
        public Dimension getPreferredSize()
        {
            FontMetrics fm = getFontMetrics(getFont());
            int width = fm.stringWidth(text) + 4;
            int height = fm.getMaxAscent() + fm.getMaxDescent() + 2;
            return new Dimension(width, height);
        }
        
        public void paint (Graphics g)
        {
            FontMetrics fm = getFontMetrics(getFont());
            Dimension d = getSize();
            int baseline = d.height - fm.getMaxDescent() - 1;
            
            g.setColor(getBackground());
            g.fillRect(0, 0, d.width, d.height);
            g.setColor(getForeground());
            g.drawString(text, 2, baseline);
            
            g.setColor(Color.black);
            g.drawRect(0, 0, d.width - 1, d.height - 1);
        }
    }
    
    
    
}
