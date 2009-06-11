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
