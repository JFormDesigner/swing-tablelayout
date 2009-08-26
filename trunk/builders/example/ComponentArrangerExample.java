/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 2.0
 *
 * Copyright (c) 2001 Daniel Barbalace.  All rights reserved.
 *
 * Project maintained at https://tablelayout.dev.java.net
 *
 * I. Terms for redistribution of original source and binaries
 *
 * Redistribution and use of unmodified source and/or binaries are
 * permitted provided that the following condition is met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to redistribute
 *    the original source; you may choose to redistribute only the
 *    binaries.
 *
 * Basically, if you distribute unmodified source, you meet
 * automatically comply with the license with no additional effort on
 * your part.
 *
 * II. Terms for distribution of derived works via subclassing and/or
 *     composition.
 *
 * You may generate derived works by means of subclassing and/or
 * composition (e.g., the Adaptor Pattern), provided that the following
 * conditions are met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to redistribute
 *    the original source; you may choose to redistribute only the
 *    binaries.
 *
 * 2. The original software is not altered.
 *
 * 3. Derived works are not contained in the info.clearthought
 *    namespace/package or any subpackage of info.clearthought.
 *
 * 4. Derived works do not use the class or interface names from the
 *    info.clearthought... packages
 *
 * For example, you may define a class with the following full name:
 *    org.nameOfMyOrganization.layouts.RowMajorTableLayout
 *
 * However, you may not define a class with the either of the
 * following full names:
 *    info.clearthought.layout.RowMajorTableLayout
 *    org.nameOfMyOrganization.layouts.TableLayout
 *
 * III. Terms for redistribution of source modified via patch files.
 *
 * You may generate derived works by means of patch files provided
 * that the following conditions are met:
 *
 * 1. Redistributions of original source code must retain the above
 *    copyright notice and license.  You are not required to
 *    redistribute the original source; you may choose to redistribute
 *    only the binaries resulting from the patch files.
 *
 * 2. The original source files are not altered.  All alteration is
 *    done in patch files.
 *
 * 3. Derived works are not contained in the info.clearthought
 *    namespace/package or any subpackage of info.clearthought.  This
 *    means that your patch files must change the namespace/package
 *    for the derived work.  See section II for examples.
 *
 * 4. Derived works do not use the class or interface names from the
 *    info.clearthought... packages.  This means your patch files
 *    must change the names of the interfaces and classes they alter.
 *    See section II for examples.
 *
 * 5. Derived works must include the following disclaimer.
 *    "This work is derived from Clearthought's TableLayout,
 *     https://tablelayout.dev.java.net, by means of patch files
 *     rather than subclassing or composition.  Therefore, this work
 *     might not contain the latest fixes and features of TableLayout."
 *
 * IV. Terms for repackaging, transcoding, and compiling of binaries.
 *
 * You may do any of the following with the binaries of the
 * original software.
 *
 * 1. You may move binaries (.class files) from the original .jar file
 *    to your own .jar file.
 *
 * 2. You may move binaries from the original .jar file to other
 *    resource containing files, including but not limited to .zip,
 *    .gz, .tar, .dll, .exe files.
 *
 * 3. You may backend compile the binaries to any platform, including
 *    but not limited to Win32, Win64, MAC OS, Linux, Palm OS, any
 *    handheld or embedded platform.
 *
 * 4. You may transcribe the binaries to other virtual machine byte
 *    code protocols, including but not limited to .NET.
 *
 * V. License Disclaimer.
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

import info.clearthought.layout.ComponentArranger;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


/**
 * Demonstrates how to use the ComponentArranger layout builder.
 * 
 * @author Daniel E. Barbalace
 */

public final class ComponentArrangerExample
{
    public static void main (String [] args)
    {
        example1();
    }
    
    
    
    private static void example1 ()
    {
        // Create frame
        JFrame frame = new JFrame("Example 1");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        
        // Create controls
        JLabel labelSource = new JLabel("Source file");
        JLabel labelDestination = new JLabel("Destination file");
        JTextField textboxSource = new JTextField(30);
        JTextField textboxDestination = new JTextField(30);
        JButton buttonBrowseSource = new JButton("Browse...");
        JButton buttonBrowseDestination = new JButton("Browse...");
        JLabel labelHi = new JLabel("Hello");
        JTextArea textboxBig = new JTextArea();
        textboxBig.setText("Just a big old text area control.");
        JScrollPane scrollPane = new JScrollPane(textboxBig);
        JButton buttonHelp = new JButton("Help");
        JButton buttonCancel = new JButton("Cancel");
        JButton buttonOk = new JButton("OK");
        JPanel panelButton = new JPanel();
        
        // Create a subpanel for the buttons because we want to lay them out separately
        ComponentArranger.arrange(5, 5, new Object []
            {panelButton,           TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
             TableLayout.PREFERRED, buttonHelp,       buttonCancel,     buttonOk});
        
        // Create layout and add controls in one step, then apply any alignments
        TableLayout layout = ComponentArranger.arrange(5, 5, new Object []
            {contentPane,            TableLayout.PREFERRED, TableLayout.FILL,   TableLayout.PREFERRED,
             TableLayout.PREFERRED,  labelSource,           textboxSource,      buttonBrowseSource,
             TableLayout.PREFERRED,  labelDestination,      textboxDestination, buttonBrowseDestination,
             TableLayout.FILL,       null,                  scrollPane,         scrollPane,
             TableLayout.PREFERRED,  labelHi,               scrollPane,         scrollPane,
             5,
             TableLayout.PREFERRED,  panelButton,           panelButton,        panelButton});
        
        ComponentArranger.alignComponent(layout, panelButton, TableLayoutConstants.CENTER, TableLayoutConstants.CENTER);
        
        // Show the frame we just made
        frame.pack();
        frame.setVisible(true);
    }
}
