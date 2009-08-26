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

package info.clearthought.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * This builder creates a TableLayout based on an arrangement of components within a one-dimensional array.  The array given
 * by the user should contain a container at index 0, followed by a double or int for each column, followed by elements for
 * each row of the layout.  Each row should begin with a double or int for the row height, followed by zero or more components
 * that occupy cells in that row.  Components can span cells by being repeated in the array.  Cells can be skipped by using
 * nulls.  Rows can be ended by indicating the next row start with a double or int.  Below is an example that demonstrates all
 * of these principles.
 * <pre>
 * ComponentArranger.arrange(5, 5, new Object []
 *     {contentPane,            TableLayout.PREFERRED, TableLayout.FILL,   TableLayout.PREFERRED,
 *      TableLayout.PREFERRED,  labelSource,           textboxSource,      buttonBrowseSource,
 *      TableLayout.PREFERRED,  labelDestination,      textboxDestination, buttonBrowseDestination,
 *      TableLayout.FILL,       null,                  scrollPane,         scrollPane,
 *      TableLayout.PREFERRED,  labelHi,               scrollPane,         scrollPane,
 *      5,
 *      TableLayout.PREFERRED,  panelButton,           panelButton,        panelButton});
 * </pre>
 * 
 * @author Daniel E. Barbalace
 */

public final class ComponentArranger
{
    //----------------------------------------------------------------------------------------------------------------------------
    //--- Construction
    //----------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Enforce static pattern.
     */
    
    private ComponentArranger ()
    {
    }
    
    
    
    //----------------------------------------------------------------------------------------------------------------------------
    //--- ComponentArranger API
    //----------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Aligns the given component in the given layout.
     * 
     * @param layout
     *        Layout being altered.
     * @param component
     *        Component whose alignment is being set.
     * @param hAlign
     *        New horizontal alignment.
     * @param vAlign
     *        New vertical alignment.
     */
    
    public static void alignComponent (TableLayout layout, Component component, int hAlign, int vAlign)
    {
        TableLayoutConstraints constraints = layout.getConstraints(component);
        constraints.hAlign = hAlign;
        constraints.vAlign = vAlign;
        layout.setConstraints(component, constraints);
    }
    
    
    
    /**
     * Creates a TableLayout using the container, components, and row/column sizes in the given array.  When this method returns
     * the container in "array" will be using the TableLayout returned by this method.  That container will also have all the
     * components specified in "array".
     * 
     * @param hGap
     *        Horizontal gap between columns.
     * @param vGap
     *        Vertical gap between rows.
     * @param array
     *        See examples in ComponentArranger class JavaDoc for details on how to construct this array.
     * 
     * @return The TableLayout created.
     * 
     * @see ComponentArranger
     */
    
    public static TableLayout arrange (int hGap, int vGap, Object [] array)
    {
        Object [][] spec = parseArray(array);
        
        TableLayout layout = new TableLayout();
        layout.setHGap(hGap);
        layout.setVGap(vGap);
        Container container = (Container) spec[0][0];
        container.setLayout(layout);
        
        int numRow = spec.length - 1;
        int numColumn = spec[0].length - 1;
        
        for (int column = 0; column < numColumn; column++)
            layout.insertColumn(column, (Double) spec[0][column + 1]);
        
        for (int row = 0; row < numRow; row++)
            layout.insertRow(row, (Double) spec[row + 1][0]);
        
        // Now get controls from array
        Map <Component, TableLayoutConstraints> mapComponent = new LinkedHashMap <Component, TableLayoutConstraints> ();
        
        for (int row = 0; row < numRow; row++)
            for (int column = 0; column < numColumn; column++)
            {
                Component component = (Component) spec[row + 1][column + 1];
                
                if (component != null && !mapComponent.keySet().contains(component))
                {
                    int column2 = column;
                    
                    while (column2 < numColumn && spec[row + 1][column2 + 1] == component)
                        column2++;
                    
                    column2--;
                    
                    int row2 = row;
                    
                    while (row2 < numRow && spec[row2 + 1][column + 1] == component)
                        row2++;
                    
                    row2--;
                    
                    TableLayoutConstraints constraints = new TableLayoutConstraints(column, row, column2, row2);
                    mapComponent.put(component, constraints);
                }
            }
        
        // Add controls to container
        for (Component component : mapComponent.keySet())
            container.add(component, mapComponent.get(component));
        
        return layout;
    }
    
    
    
    //----------------------------------------------------------------------------------------------------------------------------
    //--- Class internal methods
    //----------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Converts the given one-dimensional array into a two-dimensional array that delimits the columns and rows in the layout.
     * 
     * @param sourceArray
     *        One-dimensional array that infers a two-dimensional grid.
     * 
     * @return The two-dimensional array that makes the grid explicit.
     */
    
    private static Object [][] parseArray (Object [] sourceArray)
    {
        replaceIntegerWithDouble(sourceArray);
        int numColumn = countColumns(sourceArray);
        int numRow = countRows(sourceArray, numColumn);
        
        Object [][] destArray = new Object[numRow + 1][numColumn + 1];
        copyContainer(sourceArray, destArray);
        copyColumnWidths(sourceArray, numColumn, destArray);
        copyRowHeightsAndComponents(sourceArray, numColumn, destArray);
        
        return destArray;
    }
    
    
    
    /**
     * Replaces integers with doubles in the given array.
     * 
     * @param sourceArray
     *        Array passed by user.
     */
    
    private static void replaceIntegerWithDouble (Object [] sourceArray)
    {
        int sourceLength = sourceArray.length;
        
        for (int index = 0; index < sourceLength; index++)
            if (sourceArray[index] instanceof Integer)
            {
                int value = (Integer) sourceArray[index];
                sourceArray[index] = new Double(value);
            }
    }
    
    
    
    /**
     * Counts the columns in "sourceArray".
     * 
     * @param sourceArray
     *        Array passed by user.
     * 
     * @return The number of columns specified in "sourceArray".
     */
    
    private static int countColumns (Object [] sourceArray)
    {
        int sourceLength = sourceArray.length;
        int index = 1;
        
        while (index < sourceLength && sourceArray[index] instanceof Double)
            index++;
        
        if (index == sourceLength)
            throw new IllegalArgumentException
                ("Array could not be properly parsed.  There was no component or null field to mark the end of the columns.");
        
        int numColumn = index - 2;
        
        return numColumn;
    }
    
    
    
    /**
     * Counts the rows in "sourceArray".
     * 
     * @param sourceArray
     *        Array passed by user.
     * @param numColumn
     *        Number of columns already calculated.
     * 
     * @return The number of rows specified in "sourceArray".
     */
    
    private static int countRows (Object [] sourceArray, int numColumn)
    {
        int sourceLength = sourceArray.length;
        int index = numColumn + 1;
        int numRow = 0;
        
        while (index < sourceLength)
            if (sourceArray[index++] instanceof Double)
                numRow++;
        
        return numRow;
    }
    
    
    
    /**
     * Copies the container from the source array to the destination.
     * 
     * @param sourceArray
     *        One-dimensional array specified by user.
     * @param destArray
     *        Two-dimensional parsed array.
     */
    
    private static void copyContainer (Object [] sourceArray, Object [][] destArray)
    {
        if (!(sourceArray[0] instanceof Container))
            throw new IllegalArgumentException
                ("Element 0 of the array must be an instance of container.  Element 0 was a " +
                 (sourceArray[0] == null ? "null" : sourceArray[0].getClass().getName()));
        
        destArray[0][0] = sourceArray[0];
    }
    
    
    
    /**
     * Copies the elements containing column widths from the source array to the destination.
     * 
     * @param sourceArray
     *        One-dimensional array specified by user.
     * @param destArray
     *        Two-dimensional parsed array.
     * @param numColumn
     *        Number of columns already calculated.
     */
    
    private static void copyColumnWidths (Object [] sourceArray, int numColumn, Object [][] destArray)
    {
        for (int index = 1; index <= numColumn; index++)
            destArray[0][index] = sourceArray[index];
    }
    
    
    
    /**
     * Copies the elements containing column heights or components from the source array to the destination.
     * 
     * @param sourceArray
     *        One-dimensional array specified by user.
     * @param destArray
     *        Two-dimensional parsed array.
     * @param numColumn
     *        Number of columns already calculated.
     */
    
    private static void copyRowHeightsAndComponents (Object [] sourceArray, int numColumn, Object [][] destArray)
    {
        destArray[1][0] = sourceArray[numColumn + 1];
        
        int sourceLength = sourceArray.length;
        int index = numColumn + 2;
        int r = 1;
        int c = 1;
        
        while (index < sourceLength)
        {
            // If it's a double, then it's a row height.
            if (sourceArray[index] instanceof Double)
            {
                // Fill in remaining cells in current row with nulls.
                while (c <= numColumn)
                    destArray[r][c++] = null;
                
                r++;
                c = 0;
                
                // Copy row height
                destArray[r][c++] = sourceArray[index];
            }
            // Copy component or null value encountered.
            else if (sourceArray[index] == null || sourceArray[index] instanceof Component)
                destArray[r][c++] = sourceArray[index];
            else
                throw new IllegalArgumentException
                    ("The given array contains an unexpected type at index " + index +
                     ".  Expected types are double, component, and null.  Actual type at index is " +
                     sourceArray[index].getClass().getName());
            
            index++;
        }
    }
}
