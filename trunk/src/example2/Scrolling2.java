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
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import info.clearthought.layout.*;



/**
 * BorderLeftMenu extends the BorderStatus demonstration by adding a left
 * navigation menu.
 *
 * @author  Daniel E. Barbalace
 * @version 1.0, Jun 17, 2005
 */

public class Scrolling2 implements ActionListener, TreeSelectionListener
{
    
    
    
    /** Main frame */
    private JFrame frame;
    
    /** Main container */
    private Container container;
    
    /** Heading */
    private Heading heading;
    
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
    
    /** Left navigation menu */
    private JTree leftMenu;
    
    /** Layout used by the frame */
    private TableLayout layout;
    
    
    
    /**
     * Runs the program.
     */
    
    public static void main (String args[])
    {
        new Scrolling2();
    }
    
    
    
    /**
     * Creates the application's GUI.
     */
    
    public Scrolling2()
    {
        // Create frame
        frame = new JFrame("Border with a heading, status bar, and left menu");
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
        heading = new Heading();
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
        
        Vector v = new Vector();
        v.add("/sample.html");
        v.add("/hypercube.html");
        v.add("/itox.html");
        v.add("/TableLayout.html");
        v.add("/feedback.html");
        leftMenu = new JTree(v);
        leftMenu.addTreeSelectionListener(this);
        
        Color backgroundColor = new Color(26, 80, 184);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setBackground(backgroundColor);
        renderer.setBackgroundNonSelectionColor(backgroundColor);
        leftMenu.setCellRenderer(renderer);
        leftMenu.setBackground(backgroundColor);
        
        // Create and set layout
        double p = TableLayout.PREFERRED;
        double border = 10;
        double [] columnSize =
            {p, border, border, p, p, p, TableLayout.FILL, border};
        double [] rowSize = {p, border, p, p, TableLayout.FILL, border, p};
        layout = new TableLayout(columnSize, rowSize);
        layout.setHGap(5);
        container = frame.getContentPane();
        container.setLayout(layout);
        
        // Add controls
        container.add(heading,       "0, 0, 7, 0");
        container.add(labelFile,     "3, 2, RIGHT, BOTTOM");
        container.add(labelFilename, "4, 2, 6, 2");
        container.add(labelView,     "3, 3, RIGHT, BOTTOM");
        container.add(radioWebPage,  "4, 3");
        container.add(radioText,     "5, 3");
        container.add(scrollPane,    "3, 4, 6, 4");
        container.add(statusBar,     "0, 6, 7, 6");
        container.add(leftMenu,      "0, 1, 0, 5");
        
        // Show one html file by default
        setPath("/sample.html");
        
        // Inititialize frame and layout for transitions
        Component [] c = {labelFile, labelFilename, labelView, radioWebPage,
            radioText, scrollPane};
        for (int i = 0; i < c.length; i++)
            c[i].setVisible(false);
        
        frame.pack();
        layout.setRow(0, 0);
        layout.setColumn(0, 0);
        
        // Show frame
        frame.setVisible(true);
        frame.toFront();
        
        // Transitions
        performTransitions();
        for (int i = 0; i < c.length; i++)
            c[i].setVisible(true);
    }
    
    
    
    /**
     * Performs the initial layout transitions.
     */
    
    private void performTransitions()
    {
        // Heading
        Dimension d = heading.getPreferredSize();
        for (int i = 0; i < d.height; i++)
        {
            layout.setRow(0, i);
            container.invalidate();
            container.validate();
            sleep();
        }
        layout.setRow(0, TableLayout.PREFERRED);
        
        // Left menu
        d = leftMenu.getPreferredSize();
        for (int i = 0; i < d.width - 1; i += 2)
        {
            layout.setColumn(0, i);
            container.invalidate();
            container.validate();
            heading.repaint();
            sleep();
        }
        layout.setColumn(0, TableLayout.PREFERRED);
    }
    
    
    
    /**
     * Waits for a standard amount of time.
     */
    
    private void sleep()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e) {}
    }
    
    
    
    /**
     * Sets the path of the document to view.
     * 
     * @param path    full or relative path of document
     */
    
    private void setPath (String path)
    {
        // Attempt to open file in jar
        URL url = Scrolling2.class.getResource(path);
        
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
      * Invoked when the value of the left menu changes.
      */
    
    public void valueChanged (TreeSelectionEvent e)
    {
        TreePath path = e.getNewLeadSelectionPath();
        Object [] array = path.getPath();
        String item = (array.length == 2) ? "" + array[1] : "";
        
        if (item.length() > 0)
            setPath(item);
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
                (leftMenu.getSize().width, 0, new Color(26, 80, 184),
                 d.width, 0, frame.getBackground(), false);
            g2.setPaint(gradient);
            g2.fill(new Rectangle(d.width, d.height));
            
            g2.setPaint(Color.white);
            int x = 0;
            int i = 0;
            String [] letter = {"J", "a", "v", "a"};
            
            // Some optimizing done since we will call this method often
            // during the transitions
            double coeff = 0.02 * Math.PI;
            double vMax = 0.25 * d.height;
            double phaseDelta = coeff * 10;
            double phase = 0;
            
            double [] offset = new double[10];
            for (int c = 0; c < 10; c++)
            {
                offset[c] = vMax * Math.sin(phase);
                phase += phaseDelta;
            }
            
            int c = 0;
            int halfHeight = d.height >> 1;
            
            while (x < d.width)
            {
                g2.translate(x, offset[c]);
                g2.drawString(letter[i++], 0, halfHeight);
                g2.translate(-x, -offset[c++]);
                x += 10;
                
                if (i == letter.length)
                    i = 0;
                
                if (c == 10)
                    c = 0;
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
