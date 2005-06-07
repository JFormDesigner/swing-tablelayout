/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 1.0
 *
 * Copyright (c) 2002 Daniel Barbalace.  All rights reserved.
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

/*
 * ====================================================================
 * This is an extension of the info.clearthought.layout package.
 * This extension only works under J2SE 1.4 or later.
 * ====================================================================
 */

package info.clearthought.layout;



import java.awt.*;
import java.beans.*;
import java.io.*;
import java.util.*;



/*
 * TableLayoutPersistenceDelegate is used by the JavaBeans framework to
 * persist containers that use TableLayout to XML files.  A container, say a
 * frame, can be saved saved to an XML file.  Later, the container and all of
 * its components can be reconstructed.
 *
 * TableLayoutPersistenceDelegate is not part of the base TableLayout
 * distribution for several reasons.  First, this class will only work with
 * J2SE 1.4 or later.  Second, if the container persisted holds other
 * containers that use layout managers other than TableLayout, the nested
 * containers might not be properly written to the XML file.  Finally, this
 * class has a specific underlying implementation.  It will only write
 * containers to XML files.  It does not support other protocols, and there
 * is no way provided to write to a stream instead of a file.  Nevertheless,
 * this extension may prove useful to developers wishing to use the
 * persistence mechanism introduced in J2SE 1.4.
 *
 * @version 1.0 5/17/02
 * @version 1.1 4/8/05 fixed leak of XML Decoder
 * @author  Daniel E. Barbalace
 * @author  Jüergen Schwibs
 */

public class TableLayoutPersistenceDelegate extends DefaultPersistenceDelegate
{



/**
 * Writes a container to a file.
 *
 * @param filename     name of file to write
 * @param container    container to write
 */

public static void writeContainer (String filename, Object container) throws
    FileNotFoundException
{
    XMLEncoder encoder = new XMLEncoder
        (new BufferedOutputStream(new FileOutputStream(filename)));

    encoder.setPersistenceDelegate(TableLayoutConstraints.class,
        new DefaultPersistenceDelegate(new String[]
            {"col1", "row1", "col2", "row2", "hAlign", "vAlign"}));

    encoder.setPersistenceDelegate
        (TableLayout.class, new TableLayoutPersistenceDelegate());

    encoder.writeObject(container);
    encoder.close();
}



/**
 * Reads a container previously written by writeContainer.
 *
 * @param filename    name of the file to read
 *
 * @return the read container
 */

public static Container readContainer (String filename) throws
    FileNotFoundException
{
    XMLDecoder decoder =
        new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
    Container c = (Container) decoder.readObject();
	decoder.close();
	return c;
}



/**
 * Reads a container previously written by writeContainer.  The container
 * written must have been an instance of Window or this method will throw a
 * CastClassException.
 *
 * @param filename    name of the file to read
 *
 * @return the read container as a window
 */

public static Window readWindow (String filename) throws
    FileNotFoundException
{
    XMLDecoder decoder =
        new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
    Window w = (Window) decoder.readObject();
	decoder.close();
	return w;
}



/**
 * Writes an instance of TableLayout to a given encoder stream.
 *
 * @param oldInstance    instance to be copied
 * @param newInstance    instance that is to be modified
 * @param out            stream to which any initialization statements should
 *                       be written
 */

protected void initialize
    (Class type, Object oldInstance, Object newInstance, Encoder out)
{
    super.initialize(type, oldInstance, newInstance, out);

    int C = TableLayout.C;
    int R = TableLayout.R;

    try
    {
        LinkedList list = ((TableLayout) oldInstance).list;

        if (list != null)
        {
            ListIterator iterator = list.listIterator();

            while (iterator.hasNext())
            {
                TableLayout.Entry entry = (TableLayout.Entry) iterator.next();
                TableLayoutConstraints constraint = new TableLayoutConstraints
                    (entry.cr1[C], entry.cr1[R], entry.cr2[C], entry.cr2[R],
                     entry.alignment[C], entry.alignment[R]);
                
                Statement statement = new Statement
                    (oldInstance, "addLayoutComponent", 
                     new Object [] {entry.component, constraint});

                out.writeStatement(statement);
           }
        }
    }
    catch (Exception e)
    {
        out.getExceptionListener().exceptionThrown(e);
    }
}



}
